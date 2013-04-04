# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table buddy_link (
  blid                      integer not null,
  user_id                   integer,
  buddy_id                  integer,
  validated                 boolean,
  constraint pk_buddy_link primary key (blid))
;

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
  tripper_id                integer,
  comments                  varchar(255),
  drug_id                   integer,
  dfrom                     timestamp,
  dtill                     timestamp,
  number                    integer,
  measure_id                integer,
  constraint pk_trip primary key (tid))
;

create table trip_link (
  trid                      integer not null,
  from_id                   integer,
  target_id                 integer,
  trip_id                   integer,
  validated                 boolean,
  constraint pk_trip_link primary key (trid))
;

create table user (
  uid                       integer not null,
  alias                     varchar(255),
  email                     varchar(255),
  trippoints                integer,
  password                  varchar(255),
  validated                 boolean,
  constraint pk_user primary key (uid))
;

create table validate_request (
  vrid                      integer not null,
  target_id                 integer,
  token                     varchar(255),
  constraint pk_validate_request primary key (vrid))
;

create sequence buddy_link_seq;

create sequence drug_seq;

create sequence measure_seq;

create sequence trip_seq;

create sequence trip_link_seq;

create sequence user_seq;

create sequence validate_request_seq;




# --- !Downs

SET REFERENTIAL_INTEGRITY FALSE;

drop table if exists buddy_link;

drop table if exists drug;

drop table if exists measure;

drop table if exists trip;

drop table if exists trip_link;

drop table if exists user;

drop table if exists validate_request;

SET REFERENTIAL_INTEGRITY TRUE;

drop sequence if exists buddy_link_seq;

drop sequence if exists drug_seq;

drop sequence if exists measure_seq;

drop sequence if exists trip_seq;

drop sequence if exists trip_link_seq;

drop sequence if exists user_seq;

drop sequence if exists validate_request_seq;

