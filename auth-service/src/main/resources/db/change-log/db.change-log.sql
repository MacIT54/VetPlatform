--liquibase formatted sql

-- changeset yourname:001_create_auth_user_table

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

-- rollback drop table if exists auth_user;
