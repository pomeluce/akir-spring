create schema if not exists akir;
set search_path to akir, public;

\i 'tables/snowflake_state.sql'
\i 'tables/akir_user.sql'
\i 'tables/akir_role.sql'
\i 'tables/akir_menu.sql'
\i 'tables/akir_task.sql'

\i 'functions/snowflake_id.sql'
\i 'scripts/user-add.sql'
\i 'scripts/role-add.sql'
\i 'script/menu-add.sql'