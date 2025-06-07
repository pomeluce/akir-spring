drop table if exists snowflake_state;

create table snowflake_state
(
    id             serial primary key not null, -- 单行记录
    last_timestamp bigint             not null, -- 最后时间戳
    last_sequence  int                not null  -- 最后序列号
);

comment on table snowflake_state is '雪花算法状态表';
comment on column snowflake_state.id is '主键';
comment on column snowflake_state.last_timestamp is '最后时间戳';
comment on column snowflake_state.last_sequence is '最后序列号';

alter sequence snowflake_state_id_seq restart with 1000000001 increment by 1;

insert into snowflake_state(last_timestamp, last_sequence)
values (0, 0);