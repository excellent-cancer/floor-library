SET @database_name='experimental';

create database experimental;

use experimental;

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

create table if not exists owner_project
(
    id           bigint unsigned auto_increment
        primary key,
    name         varchar(32)                            not null,
    description  varchar(128) default ''                not null,
    owner_id     bigint unsigned                        null,
    created_date timestamp    default CURRENT_TIMESTAMP not null,
    updated_date timestamp    default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP,
    constraint owner_project_owner_id_fk
        foreign key (owner_id) references owner (id)
            on delete set null
);


/*create table if not exists document
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
    document_id   bigint unsigned                        not null,
    is_empty      tinyint(1)   default 1                 not null,
    download_link varchar(128) default ''                not null,
    upload_link   varchar(128) default ''                not null
);

create table if not exists findOwner
(
    id           bigint unsigned auto_increment
        primary key,
    username     varchar(32) not null default '',
    organization varchar(32) not null default '',
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

CREATE TABLE `user` (
  `Host` char(255) CHARACTER SET ascii COLLATE ascii_general_ci NOT NULL DEFAULT '',
  `User` char(32) COLLATE utf8_bin NOT NULL DEFAULT '',
  `Select_priv` enum('N','Y') CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT 'N',
  `Insert_priv` enum('N','Y') CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT 'N',
  `Update_priv` enum('N','Y') CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT 'N',
  `Delete_priv` enum('N','Y') CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT 'N',
  `Create_priv` enum('N','Y') CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT 'N',
  `Drop_priv` enum('N','Y') CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT 'N',
  `Reload_priv` enum('N','Y') CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT 'N',
  `Shutdown_priv` enum('N','Y') CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT 'N',
  `Process_priv` enum('N','Y') CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT 'N',
  `File_priv` enum('N','Y') CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT 'N',
  `Grant_priv` enum('N','Y') CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT 'N',
  `References_priv` enum('N','Y') CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT 'N',
  `Index_priv` enum('N','Y') CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT 'N',
  `Alter_priv` enum('N','Y') CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT 'N',
  `Show_db_priv` enum('N','Y') CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT 'N',
  `Super_priv` enum('N','Y') CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT 'N',
  `Create_tmp_table_priv` enum('N','Y') CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT 'N',
  `Lock_tables_priv` enum('N','Y') CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT 'N',
  `Execute_priv` enum('N','Y') CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT 'N',
  `Repl_slave_priv` enum('N','Y') CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT 'N',
  `Repl_client_priv` enum('N','Y') CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT 'N',
  `Create_view_priv` enum('N','Y') CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT 'N',
  `Show_view_priv` enum('N','Y') CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT 'N',
  `Create_routine_priv` enum('N','Y') CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT 'N',
  `Alter_routine_priv` enum('N','Y') CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT 'N',
  `Create_user_priv` enum('N','Y') CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT 'N',
  `Event_priv` enum('N','Y') CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT 'N',
  `Trigger_priv` enum('N','Y') CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT 'N',
  `Create_tablespace_priv` enum('N','Y') CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT 'N',
  `ssl_type` enum('','ANY','X509','SPECIFIED') CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '',
  `ssl_cipher` blob NOT NULL,
  `x509_issuer` blob NOT NULL,
  `x509_subject` blob NOT NULL,
  `max_questions` int unsigned NOT NULL DEFAULT '0',
  `max_updates` int unsigned NOT NULL DEFAULT '0',
  `max_connections` int unsigned NOT NULL DEFAULT '0',
  `max_user_connections` int unsigned NOT NULL DEFAULT '0',
  `plugin` char(64) COLLATE utf8_bin NOT NULL DEFAULT 'caching_sha2_password',
  `authentication_string` text COLLATE utf8_bin,
  `password_expired` enum('N','Y') CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT 'N',
  `password_last_changed` timestamp NULL DEFAULT NULL,
  `password_lifetime` smallint unsigned DEFAULT NULL,
  `account_locked` enum('N','Y') CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT 'N',
  `Create_role_priv` enum('N','Y') CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT 'N',
  `Drop_role_priv` enum('N','Y') CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT 'N',
  `Password_reuse_history` smallint unsigned DEFAULT NULL,
  `Password_reuse_time` smallint unsigned DEFAULT NULL,
  `Password_require_current` enum('N','Y') CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `User_attributes` json DEFAULT NULL,
  PRIMARY KEY (`Host`,`User`)
) /*!50100 TABLESPACE `mysql` */ ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin STATS_PERSISTENT=0 ROW_FORMAT=DYNAMIC COMMENT='Users and global privileges'

*/