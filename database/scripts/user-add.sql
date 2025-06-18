do
$$
    declare
        v_user_id bigint;
    begin
        v_user_id := snowflake_id();

        insert into akir_user
        values (v_user_id, 'admin', '$2a$10$uASpEDmu85AqZFKw1tTZr.iA9VRmNewGr6hYQLknK9DnoAXaTAnWu', 'admin', 'MALE', 'ENABLED', '', 'admin@gmail.com', '18888888888', '', '', v_user_id,
                now(), v_user_id, now(), '超级管理员');
    end;
$$;