insert into akir_user (id, account, password, email, gender, status, create_by, create_time, update_by, update_time)
values (akir_snowflake_id(), 'admin', '$2a$10$CiqxrOugoB/lk6nk/jur0Oc91s6g8Sdqu3kYN1Ff/jldbeOXymsN2', 'admin@gmail.com', 'MALE', 'ENABLED', 'admin', now(), 'admin', now());