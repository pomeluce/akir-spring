drop table if exists meagle_user;
drop type if exists meagle_user_status;

create type meagle_user_status as enum ('ENABLED', 'DISABLED', 'DELETE');
create table meagle_user
(
    id          serial primary key not null,
    account     varchar(20) unique not null,
    password    varchar(100)       not null,
    email       varchar(50)        not null,
    status      meagle_user_status,
    role        varchar(100),
    create_by   varchar(20),
    create_time timestamp with time zone,
    update_by   varchar(20),
    update_time timestamp with time zone,
    remark      text
);

comment on table meagle_user is '用户表';
comment on column meagle_user.id is '用户 ID';
comment on column meagle_user.account is '账号';
comment on column meagle_user.password is '密码';
comment on column meagle_user.email is '邮箱';
comment on column meagle_user.status is '状态: 是否启用, 是否删除, 是否锁定';
comment on column meagle_user.create_by is '创建人';
comment on column meagle_user.create_time is '创建时间';
comment on column meagle_user.update_by is '更新人';
comment on column meagle_user.update_time is '更新时间';

alter sequence meagle_user_id_seq restart with 1000000001 increment by 1;
