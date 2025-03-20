do
$$
    declare
        v_user_id bigint;
    begin
        select id into v_user_id from akir_user where account = 'admin';

        insert into akir_role
        values (snowflake_id(), 'admin', '超级管理员', '拥有所有权限', v_user_id, now(), v_user_id, now(), ''),
               (snowflake_id(), 'everyone', '所有用户', '基础功能权限', v_user_id, now(), v_user_id, now(), '');
    end;
$$;

