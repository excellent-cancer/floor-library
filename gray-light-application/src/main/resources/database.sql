create database experimental;

drop table if exists experimental.owner;
create table experimental.owner(
    id bigint unsigned not null auto_increment,
    username varchar(32) not null unique,
    organization varchar(128) not null,
    primary key (id)
);

drop table if exists experimental.owner_link;
create table experimental.owner_link(
    id bigint unsigned not null auto_increment,
    url varchar(128) not null,
    name varchar(32) not null,
    owner_id bigint unsigned not null references experimental.owner(id),
    primary key (id)
);

drop table if exists experimental.owner_project;
create table experimental.owner_project(
    id bigint unsigned not null auto_increment,
    name varchar(32) not null ,
    description varchar(128) default '',
    owner_id bigint unsigned not null references experimental.owner(id),
    created_date timestamp not null default CURRENT_TIMESTAMP,
    updated_date timestamp not null default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP,
    primary key (id)
);

drop table if exists experimental.document_catalog;
create table experimental.document_catalog(
    id bigint unsigned not null auto_increment,
    project_id bigint unsigned not null references experimental.owner_project(id),
    parent_id bigint unsigned not null,
    created_date timestamp not null default CURRENT_TIMESTAMP,
    updated_date timestamp not null default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP,
    title varchar(32) not null default '',
    has_docs boolean not null default false,
    primary key (id)
);

drop table if exists experimental.document;
create table  experimental.document(
    id bigint unsigned not null auto_increment,
    created_date timestamp null default CURRENT_TIMESTAMP,
    updated_date timestamp null default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP,
    title varchar(32) not null default '',
    isEmpty boolean not null default true,
    download_link varchar(128) not null default '',
    upload_link varchar(128) not null default '',
    catalog_id bigint unsigned not null references experimental.document_catalog(id),
    primary key (id)
);