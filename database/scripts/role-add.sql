insert into akir_role(id, code, name, description, create_by, create_time, update_by, update_time, remark)
values (akir_snowflake_id(), 'admin', '超级管理员', '管理员角色', (select id from akir_user where account = 'admin'), now(), (select id from akir_user where account = 'admin'),
        now(),
        '');

insert into akir_role(id, code, name, description, create_by, create_time, update_by, update_time, remark)
values (akir_snowflake_id(), 'everyone', '所有用户', '默认角色', (select id from akir_user where account = 'admin'), now(), (select id from akir_user where account = 'admin'),
        now(),
        '');
