insert into akir_role
values (akir_snowflake_id(), 'admin', '超级管理员', '拥有所有权限', (select id from akir_user where account = 'admin'), now(), (select id from akir_user where account = 'admin'),
        now(), ''),
       (akir_snowflake_id(), 'everyone', '所有用户', '基础功能权限', (select id from akir_user where account = 'admin'), now(), (select id from akir_user where account = 'admin'),
        now(), '');
