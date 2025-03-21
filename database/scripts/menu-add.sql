do
$$
    declare
        v_user_id      bigint;
        v_dashboard_id bigint;
        v_system_id    bigint;
        v_workflow_id  bigint;
        v_error_id     bigint;
    begin
        v_dashboard_id := snowflake_id();
        v_system_id := snowflake_id();
        v_workflow_id := snowflake_id();
        v_error_id := snowflake_id();

        select id into v_user_id from akir_user where account = 'admin';

        insert into akir_menu
        values (v_dashboard_id, 'dashboard', 'Dashboard', 1, true, false, null, '', v_user_id, now(), v_user_id, now(), ''),
               (v_system_id, 'system', '系统管理', 2, true, false, null, '', v_user_id, now(), v_user_id, now(), ''),
               (v_workflow_id, 'workflow', '流程管理', 3, true, false, null, '', v_user_id, now(), v_user_id, now(), ''),
               (v_error_id, 'error', '异常页面', 4, true, false, null, '', v_user_id, now(), v_user_id, now(), ''),
               (snowflake_id(), 'dashboard.console', '控制台', 1, true, false, v_dashboard_id, '', v_user_id, now(), v_user_id, now(), ''),
               (snowflake_id(), 'dashboard.workbench', '工作台', 2, true, false, v_dashboard_id, '', v_user_id, now(), v_user_id, now(), ''),
               (snowflake_id(), 'dashboard.monitor', '监控页', 3, true, false, v_dashboard_id, '', v_user_id, now(), v_user_id, now(), ''),
               (snowflake_id(), 'system.user', '用户管理', 1, true, false, v_system_id, '', v_user_id, now(), v_user_id, now(), ''),
               (snowflake_id(), 'system.role', '角色管理', 2, true, false, v_system_id, '', v_user_id, now(), v_user_id, now(), ''),
               (snowflake_id(), 'system.permission', '权限管理', 3, true, false, v_system_id, '', v_user_id, now(), v_user_id, now(), ''),
               (snowflake_id(), 'system.menu', '菜单管理', 4, true, false, v_system_id, '', v_user_id, now(), v_user_id, now(), ''),
               (snowflake_id(), 'workflow.define', '流程设计', 1, true, false, v_workflow_id, '', v_user_id, now(), v_user_id, now(), ''),
               (snowflake_id(), 'workflow.tester', '流程测试', 2, true, false, v_workflow_id, '', v_user_id, now(), v_user_id, now(), ''),
               (snowflake_id(), 'error.403', '403', 1, true, false, v_error_id, '_blank', v_user_id, now(), v_user_id, now(), ''),
               (snowflake_id(), 'error.404', '404', 2, true, false, v_error_id, '_blank', v_user_id, now(), v_user_id, now(), ''),
               (snowflake_id(), 'error.500', '500', 3, true, false, v_error_id, '_blank', v_user_id, now(), v_user_id, now(), '');
    end;
$$