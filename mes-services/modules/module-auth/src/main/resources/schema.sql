drop table if exists oauth_client_details;
create table oauth_client_details
(
    client_id               varchar(255)  not null
        primary key,
    resource_ids            varchar(255)  null,
    client_secret           varchar(255)  null,
    scope                   varchar(255)  null,
    authorized_grant_types  varchar(255)  null,
    web_server_redirect_uri varchar(255)  null,
    authorities             varchar(255)  null,
    access_token_validity   int           null,
    refresh_token_validity  int           null,
    additional_information  varchar(4096) null,
    autoapprove             bit           null
);


drop table if exists oauth_access_token;
create table oauth_access_token
(
    token_id          varchar(255) null,
    token             mediumblob   null,
    authentication_id varchar(255) not null
        primary key,
    user_name         varchar(255) null,
    client_id         varchar(255) null,
    authentication    mediumblob   null,
    refresh_token     varchar(255) null
);


drop table if exists oauth_refresh_token;
create table oauth_refresh_token
(
    token_id       varchar(255) null,
    token          mediumblob   null,
    authentication mediumblob   null
);

