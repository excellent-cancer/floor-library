create database experimental;

drop table if exists experimental.owner;

drop table if exists experimental.owner_link;

create table experimental.owner(
    id bigint unsigned not null auto_increment,
    username varchar(32) not null unique,
    organization varchar(128) not null,
    primary key (id)
);

create table experimental.owner_link(
    id bigint unsigned not null auto_increment,
    url varchar(128) not null,
    name varchar(32) not null,
    owner_id bigint unsigned not null references experimental.owner(id),
    primary key (id)
);