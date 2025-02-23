insert into akir_user(id, account, password, username, gender, status, email, create_by, create_time, update_by, update_time, remark)
values (akir_snowflake_id(), 'admin', '$2a$10$CiqxrOugoB/lk6nk/jur0Oc91s6g8Sdqu3kYN1Ff/jldbeOXymsN2', 'admin', 'MALE', 'ENABLED', 'admin@gmail.com', '1', now(), '1', now(),
        '超级管理员');