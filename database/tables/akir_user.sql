drop table if exists akir_user;
drop type if exists akir_user_status;

create type akir_user_status as enum ('ENABLED', 'DISABLED', 'DELETE');
create table akir_user
(
    id          bigserial primary key not null,
    account     varchar(20) unique    not null,
    password    varchar(100)          not null,
    email       varchar(50)           not null,
    status      akir_user_status,
    create_by   varchar(20),
    create_time timestamp with time zone,
    update_by   varchar(20),
    update_time timestamp with time zone,
    remark      text
);

comment on table akir_user is '用户表';
comment on column akir_user.id is '用户 ID';
comment on column akir_user.account is '账号';
comment on column akir_user.password is '密码';
comment on column akir_user.email is '邮箱';
comment on column akir_user.status is '状态: 是否启用, 是否删除, 是否禁用';
comment on column akir_user.create_by is '创建人';
comment on column akir_user.create_time is '创建时间';
comment on column akir_user.update_by is '更新人';
comment on column akir_user.update_time is '更新时间';

alter sequence akir_user_id_seq restart with 1000000001 increment by 1;
