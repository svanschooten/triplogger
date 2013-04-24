# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table buddy_link (
  blid                      integer not null,
  id                        integer,
  user_id                   integer,
  buddy_id                  integer,
  validated                 boolean,
  constraint pk_buddy_link primary key (blid))
;

create table drug (
  did                       integer not null,
  id                        integer,
  name                      varchar(255),
  erowid                    varchar(255),
  standard_measure_id       integer,
  constraint pk_drug primary key (did))
;

create table measure (
  mid                       integer not null,
  id                        integer,
  name                      varchar(255),
  display                   varchar(255),
  constraint pk_measure primary key (mid))
;

create table tluid (
  tluid                     integer not null,
  constraint pk_tluid primary key (tluid))
;

create table trip_head (
  tid                       integer not null,
  id                        integer,
  drug_id                   integer,
  dfrom                     timestamp,
  dtill                     timestamp,
  constraint pk_trip_head primary key (tid))
;

create table trip_link (
  trid                      integer not null,
  id                        integer,
  tripper_id                integer,
  trip_id                   integer,
  validated                 boolean,
  declined                  boolean,
  amount                    integer,
  measure_id                integer,
  comments                  varchar(255),
  constraint pk_trip_link primary key (trid))
;

create table user_model (
  uid                       integer not null,
  id                        integer,
  alias                     varchar(255),
  email                     varchar(255),
  trippoints                integer,
  password                  varchar(255),
  validated                 boolean,
  constraint pk_user_model primary key (uid))
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

create sequence tluid_seq;

create sequence trip_head_seq;

create sequence trip_link_seq;

create sequence user_model_seq;

create sequence validate_request_seq;




# --- !Downs

SET REFERENTIAL_INTEGRITY FALSE;

drop table if exists buddy_link;

drop table if exists drug;

drop table if exists measure;

drop table if exists tluid;

drop table if exists trip_head;

drop table if exists trip_link;

drop table if exists user_model;

drop table if exists validate_request;

SET REFERENTIAL_INTEGRITY TRUE;

drop sequence if exists buddy_link_seq;

drop sequence if exists drug_seq;

drop sequence if exists measure_seq;

drop sequence if exists tluid_seq;

drop sequence if exists trip_head_seq;

drop sequence if exists trip_link_seq;

drop sequence if exists user_model_seq;

drop sequence if exists validate_request_seq;

