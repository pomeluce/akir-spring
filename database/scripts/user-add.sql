do
$$
    declare
        v_user_id bigint;
    begin
        v_user_id := snowflake_id();

        insert into akir_user
        values (v_user_id, 'admin', '$2a$10$CiqxrOugoB/lk6nk/jur0Oc91s6g8Sdqu3kYN1Ff/jldbeOXymsN2', 'admin', 'MALE', 'ENABLED', '', 'admin@gmail.com', '18888888888', v_user_id,
                now(), v_user_id, now(), '超级管理员');
    end;
$$;