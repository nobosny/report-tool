# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table report (
  rid                       bigint not null,
  recorddate                timestamp,
  mosquitoes                integer,
  cockroaches               integer,
  mice                      integer,
  biohazard                 integer,
  tick                      integer,
  othervectors              varchar(255),
  houses                    integer,
  businesses                integer,
  constructionsites         integer,
  othersources              varchar(255),
  imagefile                 varchar(255),
  description               varchar(255),
  lat                       float,
  lon                       float,
  addeddate                 timestamp,
  constraint pk_report primary key (rid))
;

create sequence report_seq;




# --- !Downs

drop table if exists report cascade;

drop sequence if exists report_seq;

