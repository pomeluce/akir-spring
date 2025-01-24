drop table if exists meagle_task;
drop type if exists meagle_task_misfire;
drop type if exists meagle_task_status;

create type meagle_task_misfire as enum ('DEFAULT');
create type meagle_task_status as enum ('NORMAL', 'PAUSE');
create table meagle_task
(
    id              serial primary key not null,
    name            varchar            not null,
    target          varchar            not null,
    group_id        varchar,
    cron_expression varchar            not null,
    misfire_policy  meagle_task_misfire default 'DEFAULT',
    status          meagle_task_status  default 'NORMAL',
    create_by       varchar(20),
    create_time     timestamp with time zone,
    update_by       varchar(20),
    update_time     timestamp with time zone,
    remark          text
);

comment on table meagle_task is '系统任务表';
comment on column meagle_task.id is '任务 id';
comment on column meagle_task.name is '任务名称';
comment on column meagle_task.group_id is '任务分组 id';
comment on column meagle_task.cron_expression is 'cron 执行表达式';
comment on column meagle_task.misfire_policy is '补偿机制';
comment on column meagle_task.status is '任务状态';
comment on column meagle_task.create_by is '创建人';
comment on column meagle_task.create_time is '创建时间';
comment on column meagle_task.update_by is '更新人';
comment on column meagle_task.update_time is '更新时间';

alter sequence meagle_task_id_seq restart with 10000001 increment by 1;
