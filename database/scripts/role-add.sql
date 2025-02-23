insert into akir_role(id, key, description, create_by, create_time, update_by, update_time, remark)
values (akir_snowflake_id(), 'everyone', '所有用户', (select id from akir_user where account = 'admin'), now(), (select id from akir_user where account = 'admin'), now(),
        '默认角色');

insert into akir_role(id, key, description, create_by, create_time, update_by, update_time, remark)
values (akir_snowflake_id(), 'admin', '管理员', (select id from akir_user where account = 'admin'), now(), (select id from akir_user where account = 'admin'), now(), '管理员角色');