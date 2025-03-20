create extension if not exists pgcrypto;

create or replace function snowflake_id() returns bigint as
$$
declare
    epoch          bigint := 1735689600000; -- 起始时间：2025-01-01 00:00:00
    current_ms     bigint; -- 当前时间戳（毫秒）
    sequence       int; -- 当前序列号
    last_ts        bigint; -- 上次记录的时间戳
    last_seq       int; -- 上次记录的序列号
    max_sequence   int    := 4095; -- 序列号最大值（12位）
    result         bigint; -- 生成的id
    host_ip        text   := inet_server_addr()::text; -- 获取数据库服务器 ip
    sha256_hash    bytea  := digest(host_ip, 'sha256'); -- 生成 sha-256 哈希
    hash_val       bigint := ('x' || right(encode(sha256_hash, 'hex'), 8))::bit(32)::int8; -- 取哈希值的前 4 字节
    data_center_id int    := (hash_val >> 27) & 31; -- 取高 5 位（0-31）
    machine_id     int    := (hash_val >> 22) & 31; -- 取中间 5 位（0-31）
begin

    -- 获取当前时间戳（转换为毫秒并减去起始时间）
    current_ms := (extract(epoch from clock_timestamp()) * 1000)::bigint - epoch;

    -- 获取最后记录的时间戳和序列号
    select last_timestamp, last_sequence into last_ts,last_seq from snowflake_state where id = 1000000001 for update;

    -- 处理时钟回拨（简单示例中抛出异常）
    if current_ms < last_ts then
        raise exception 'clock moved backward. time drifted: % ms', last_ts - current_ms;
    end if;

    -- 同一毫秒内递增序列号
    if current_ms = last_ts then
        sequence := last_seq + 10;
        if sequence > max_sequence then
            -- 等待至下一毫秒
            perform pg_sleep((last_ts + 1 - current_ms) / 1000.0);
            current_ms := (extract(epoch from clock_timestamp()) * 1000)::bigint - epoch;
            sequence := 0;
        end if;
    else
        -- 新时间戳，重置序列号
        sequence := 0;
    end if;

    -- 更新状态表
    update snowflake_state
    set last_timestamp = current_ms,
        last_sequence  = sequence
    where id = 1000000001;

    -- 组合生成id（移位并拼接各部分）
    result := (current_ms << 22) | ((data_center_id & 31) << 17) | ((machine_id & 31) << 12) | (sequence & 4095);
    return result;
end;
$$ language plpgsql;