drop table if exists akir_role;

create table akir_role
(
    id          bigserial primary key not null,
    code        varchar(50) unique    not null,
    name        varchar(50) unique    not null,
    description text,
    create_by   varchar(20),
    create_time timestamp with time zone,
    update_by   varchar(20),
    update_time timestamp with time zone,
    remark      text
);

comment on table akir_role is '角色表';
comment on column akir_role.id is '主键';
comment on column akir_role.code is '角色编码';
comment on column akir_role.name is '角色名称';
comment on column akir_role.description is '角色描述';
comment on column akir_role.create_by is '创建人';
comment on column akir_role.create_time is '创建时间';
comment on column akir_role.update_by is '更新人';
comment on column akir_role.update_time is '更新时间';
comment on column akir_role.remark is '备注';

alter sequence akir_role_id_seq restart with 100000000001 increment by 1;