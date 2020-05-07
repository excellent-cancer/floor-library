create table if not exists document
(
    id              bigint unsigned auto_increment
        primary key,
    project_id      bigint unsigned                                                    not null,
    title           varchar(32)                              default ''                not null,
    description     varchar(128)                             default ''                null,
    created_date    timestamp                                default CURRENT_TIMESTAMP not null,
    updated_date    timestamp                                default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP,
    repo_url        varchar(128)                             default ''                not null,
    version         varchar(64)                                                        null,
    document_status enum ('INVALID', 'EMPTY', 'SYNC', 'NEW') default 'INVALID'         null
);

create table if not exists document_catalog
(
    id           bigint unsigned auto_increment
        primary key,
    uid          varchar(64)                                                    not null,
    project_id   bigint unsigned                                                not null,
    document_id  bigint unsigned                                                not null,
    parent_uid   varchar(64)                                                    not null,
    created_date timestamp                            default CURRENT_TIMESTAMP not null,
    updated_date timestamp                            default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP,
    title        varchar(32)                          default ''                not null,
    folder       enum ('EMPTY', 'CATALOG', 'CHAPTER') default 'EMPTY'           null,
    constraint parent_uid
        unique (parent_uid),
    constraint uid
        unique (uid)
);

drop table if exists experimental.document_chapter;
create table if not exists document_chapter
(
    id            bigint unsigned auto_increment
        primary key,
    created_date  timestamp    default CURRENT_TIMESTAMP null,
    updated_date  timestamp    default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP,
    title         varchar(32)  default ''                not null,
    catalog_uid   varchar(64)                            not null,
    is_empty      tinyint(1)   default 1                 not null,
    download_link varchar(128) default ''                not null,
    upload_link   varchar(128) default ''                not null,
    constraint catalog_uid
        unique (catalog_uid)
);

create table if not exists owner
(
    id           bigint unsigned auto_increment
        primary key,
    username     varchar(32) not null,
    organization varchar(32) not null,
    constraint username
        unique (username)
);

create table if not exists owner_link
(
    id       bigint unsigned auto_increment
        primary key,
    url      varchar(128)    not null,
    name     varchar(32)     not null,
    owner_id bigint unsigned not null
);

create table if not exists owner_project
(
    id           bigint unsigned auto_increment
        primary key,
    name         varchar(32)                            not null,
    description  varchar(128) default ''                null,
    owner_id     bigint unsigned                        not null,
    created_date timestamp    default CURRENT_TIMESTAMP not null,
    updated_date timestamp    default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP
);

