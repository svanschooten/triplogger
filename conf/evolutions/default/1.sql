# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table drug (
  did                       integer not null,
  name                      varchar(255),
  erowid                    varchar(255),
  constraint pk_drug primary key (did))
;

create table measure (
  mid                       integer not null,
  name                      varchar(255),
  display                   varchar(255),
  constraint pk_measure primary key (mid))
;

create table trip (
  tid                       integer not null,
  comments                  varchar(255),
  dfrom                     timestamp,
  dtill                     timestamp,
  number                    integer,
  constraint pk_trip primary key (tid))
;

create table user (
  email                     varchar(255) not null,
  uid                       integer,
  alias                     varchar(255),
  trippoints                integer,
  password                  varchar(255),
  validated                 boolean,
  constraint pk_user primary key (email))
;

create sequence drug_seq;

create sequence measure_seq;

create sequence trip_seq;

create sequence user_seq;




# --- !Downs

SET REFERENTIAL_INTEGRITY FALSE;

drop table if exists drug;

drop table if exists measure;

drop table if exists trip;

drop table if exists user;

SET REFERENTIAL_INTEGRITY TRUE;

drop sequence if exists drug_seq;

drop sequence if exists measure_seq;

drop sequence if exists trip_seq;

drop sequence if exists user_seq;

