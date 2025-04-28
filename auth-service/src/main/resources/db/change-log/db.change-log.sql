--liquibase formatted sql

-- changeset yourname:001_create_tables

create table if not exists auth_user (
    id bigserial primary key,
    login varchar(255) not null unique,
    password varchar(255) not null,
    user_type varchar(255) not null,
    email varchar(255) not null,
    name varchar(255) not null,
    surname varchar(255) not null,
    is_enabled boolean not null default true
);

create table if not exists token_session (
    id serial primary key,
    auth_user_id bigint not null references auth_user(id),
    token varchar(512) not null,
    issued_at timestamp not null,
    expires_at timestamp not null,
    revoked boolean not null default false
);

-- rollback drop table if exists auth_user, token_session;
