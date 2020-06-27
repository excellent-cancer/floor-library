create table if not exists owner
(
    id              bigint unsigned auto_increment
        primary key,
    username        varchar(32)     default ''  not null,
    organization    varchar(32)     default ''  not null,
    super_privilege enum ('N', 'Y') default 'N' not null,
    constraint username
        unique (username)
);

create table if not exists project_details
(
    origin_id    bigint                                                     not null,
    type         varchar(32)  default ''                                    not null,
    http         varchar(128) default ''                                    not null,
    version      varchar(128) default ''                                    not null,
    created_date timestamp    default CURRENT_TIMESTAMP                     not null,
    updated_date timestamp    default CURRENT_TIMESTAMP                     null on update CURRENT_TIMESTAMP,
    status       enum ('INVALID', 'INIT', 'SYNC', 'PENDING', 'UNAVAILABLE') not null,
    primary key (origin_id, type)
);

create table if not exists scope
(
    name        varchar(32)  default '' not null,
    description varchar(128) default '' not null,
    constraint scope_name_uindex
        unique (name)
);

alter table scope
    add primary key (name);

create table if not exists owner_project
(
    id           bigint unsigned auto_increment,
    owner_id     bigint unsigned                        null,
    created_date timestamp    default CURRENT_TIMESTAMP not null,
    updated_date timestamp    default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP,
    name         varchar(32)  default ''                not null,
    description  varchar(128) default ''                not null,
    scope        varchar(32)                            not null,
    home_page    varchar(128) default ''                not null,
    constraint owner_project_id_uindex
        unique (id),
    constraint owner_project_owner_id_fk
        foreign key (owner_id) references owner (id)
            on delete set null,
    constraint owner_project_scope_name_fk
        foreign key (scope) references scope (name)
            on delete cascade
);

alter table owner_project
    add primary key (id);

create table if not exists book_catalog
(
    uid              varchar(64)                                                    not null,
    owner_project_id bigint unsigned                                                not null,
    parent_uid       varchar(64)                                                    not null,
    created_date     timestamp                            default CURRENT_TIMESTAMP not null,
    updated_date     timestamp                            default CURRENT_TIMESTAMP not null,
    title            varchar(32)                          default ''                not null,
    folder           enum ('EMPTY', 'CATALOG', 'CHAPTER') default 'EMPTY'           not null,
    constraint document_catalog_uid_uindex
        unique (uid),
    constraint book_catalog_owner_project_id_fk
        foreign key (owner_project_id) references owner_project (id)
            on update cascade on delete cascade
);

alter table book_catalog
    add primary key (uid);

create table if not exists book_chapter
(
    uid              varchar(64)                            not null,
    created_date     timestamp    default CURRENT_TIMESTAMP not null,
    updated_date     timestamp    default CURRENT_TIMESTAMP not null,
    title            varchar(32)  default ''                not null,
    catalog_uid      varchar(64)                            not null,
    owner_project_id bigint unsigned                        not null,
    download_link    varchar(128) default ''                not null,
    constraint book_chapter_uid_uindex
        unique (uid),
    constraint book_chapter_book_catalog_uid_fk
        foreign key (catalog_uid) references book_catalog (uid)
            on update cascade on delete cascade,
    constraint book_chapter_owner_project_id_fk
        foreign key (owner_project_id) references owner_project (id)
            on delete cascade
);

alter table book_chapter
    add primary key (uid);

create table if not exists works_document
(
    works_id    bigint unsigned not null,
    document_id bigint unsigned not null,
    constraint works_document_document_id_uindex
        unique (document_id),
    constraint works_document_owner_project_id_fk
        foreign key (works_id) references owner_project (id)
            on update cascade on delete cascade,
    constraint works_document_owner_project_id_fk_2
        foreign key (document_id) references owner_project (id)
            on update cascade on delete cascade
);

alter table works_document
    add primary key (document_id);

