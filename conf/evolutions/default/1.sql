# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table app_user (
  id                        bigint auto_increment not null,
  email                     varchar(255),
  password                  varchar(255),
  role                      varchar(5),
  constraint ck_app_user_role check (role in ('ADMIN','USER')),
  constraint pk_app_user primary key (id))
;

create table comment (
  id                        bigint auto_increment not null,
  post_id                   bigint,
  text                      varchar(255),
  comment_raiser_id         bigint,
  constraint pk_comment primary key (id))
;

create table post (
  id                        bigint auto_increment not null,
  text                      varchar(255),
  constraint pk_post primary key (id))
;

create table test (
  id                        bigint auto_increment not null,
  title                     varchar(255),
  constraint pk_test primary key (id))
;

create table test_answer (
  id                        bigint auto_increment not null,
  answer                    varchar(255),
  test_question_id          bigint,
  is_correct                CHAR(1) DEFAULT '0',
  constraint pk_test_answer primary key (id))
;

create table test_question (
  id                        bigint auto_increment not null,
  test_id                   bigint not null,
  question                  varchar(255),
  constraint pk_test_question primary key (id))
;

create table test_session (
  id                        bigint auto_increment not null,
  start_time                datetime(6),
  end_time                  datetime(6),
  score                     bigint,
  test_taker_id             bigint,
  test_id                   bigint,
  submitted                 tinyint(1) default 0,
  constraint pk_test_session primary key (id))
;

alter table comment add constraint fk_comment_post_1 foreign key (post_id) references post (id) on delete restrict on update restrict;
create index ix_comment_post_1 on comment (post_id);
alter table comment add constraint fk_comment_commentRaiser_2 foreign key (comment_raiser_id) references app_user (id) on delete restrict on update restrict;
create index ix_comment_commentRaiser_2 on comment (comment_raiser_id);
alter table test_answer add constraint fk_test_answer_testQuestion_3 foreign key (test_question_id) references test_question (id) on delete restrict on update restrict;
create index ix_test_answer_testQuestion_3 on test_answer (test_question_id);
alter table test_question add constraint fk_test_question_test_4 foreign key (test_id) references test (id) on delete restrict on update restrict;
create index ix_test_question_test_4 on test_question (test_id);
alter table test_session add constraint fk_test_session_testTaker_5 foreign key (test_taker_id) references app_user (id) on delete restrict on update restrict;
create index ix_test_session_testTaker_5 on test_session (test_taker_id);
alter table test_session add constraint fk_test_session_test_6 foreign key (test_id) references test (id) on delete restrict on update restrict;
create index ix_test_session_test_6 on test_session (test_id);



# --- !Downs

SET FOREIGN_KEY_CHECKS=0;

drop table app_user;

drop table comment;

drop table post;

drop table test;

drop table test_answer;

drop table test_question;

drop table test_session;

SET FOREIGN_KEY_CHECKS=1;

