drop table if exists akir_menu;

create table akir_menu
(
    menu_id     bigint primary key not null,
    code        varchar(50) unique not null,
    label       varchar(50) unique not null,
    sort        int                not null,
    show        boolean            not null default true,
    disabled    boolean            not null default false,
    parent_id   bigint,
    target      varchar(20),
    create_by   varchar(20),
    create_time timestamp with time zone,
    update_by   varchar(20),
    update_time timestamp with time zone,
    remark      text
);

comment on table akir_menu is '菜单表';
comment on column akir_menu.menu_id is '菜单 id';
comment on column akir_menu.code is '菜单编码';
comment on column akir_menu.label is '菜单名称';
comment on column akir_menu.sort is '菜单排序';
comment on column akir_menu.show is '是否显示';
comment on column akir_menu.disabled is '是否禁用';
comment on column akir_menu.parent_id is '父菜单 id';
comment on column akir_menu.target is '打开方式';
comment on column akir_menu.create_by is '创建人';
comment on column akir_menu.create_time is '创建时间';
comment on column akir_menu.update_by is '更新人';
comment on column akir_menu.update_time is '更新时间';
comment on column akir_menu.remark is '备注';
