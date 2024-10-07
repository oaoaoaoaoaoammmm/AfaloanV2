create schema if not exists migration;
create schema if not exists application;
create schema if not exists shedlock;

create table if not exists shedlock.shedlock
(
    name       varchar(64),
    lock_until timestamp(3) not null,
    locked_at  timestamp(3) not null,
    locked_by  varchar(255),
    primary key (name)
)
