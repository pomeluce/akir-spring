drop table if exists akir_credential;
create table if not exists akir_credential
(
    id              bigint not null primary key,
    user_id         bigint not null,
    credential_id   text,
    public_key_cose bytea,
    signature_count int,
    create_by       varchar(20),
    create_time     timestamp with time zone,
    update_by       varchar(20),
    update_time     timestamp with time zone,
    remark          text
);

comment on table akir_credential is '用户凭证信息表';
comment on column akir_credential.id is '主键';
comment on column akir_credential.credential_id is 'Base64 编码的凭证 Id';
comment on column akir_credential.public_key_cose is '原始的 cose 公钥';
comment on column akir_credential.signature_count is '签名计数';
comment on column akir_user.create_by is '创建人';
comment on column akir_user.create_time is '创建时间';
comment on column akir_user.update_by is '更新人';
comment on column akir_user.update_time is '更新时间';
