drop table if exists akir_user;
drop type if exists akir_user_status;
drop type if exists akir_user_gender;

create type akir_user_status as enum ('ENABLED', 'DISABLED', 'DELETED');
create type akir_user_gender as enum ('MALE', 'FEMALE');

create table akir_user
(
    id          bigint primary key not null,
    account     varchar(20) unique    not null,
    password    varchar(100)          not null,
    username    varchar(100)          not null,
    gender      akir_user_gender default 'MALE',
    status      akir_user_status default 'ENABLED',
    identity_id varchar(18),
    email       varchar(50),
    phone       varchar(20),
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
comment on column akir_user.username is '用户名';
comment on column akir_user.gender is '性别: 男, 女';
comment on column akir_user.status is '状态: 是否启用, 是否删除, 是否禁用';
comment on column akir_user.identity_id is '身份证号';
comment on column akir_user.email is '邮箱';
comment on column akir_user.phone is '手机号';
comment on column akir_user.create_by is '创建人';
comment on column akir_user.create_time is '创建时间';
comment on column akir_user.update_by is '更新人';
comment on column akir_user.update_time is '更新时间';