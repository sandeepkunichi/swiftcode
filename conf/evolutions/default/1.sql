# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table app_user (
  id                            bigint auto_increment not null,
  email                         varchar(255),
  password                      varchar(255),
  role                          varchar(5),
  resume_submitted              tinyint(1) default 0,
  constraint ck_app_user_role check (role in ('ADMIN','USER')),
  constraint pk_app_user primary key (id)
);

create table program_submission (
  id                            bigint auto_increment not null,
  test_program_id               bigint,
  test_session_id               bigint,
  program_text                  TEXT,
  language_type                 integer,
  constraint ck_program_submission_language_type check (language_type in (0,1,2,3)),
  constraint pk_program_submission primary key (id)
);

create table test (
  id                            bigint auto_increment not null,
  title                         varchar(255),
  test_duration                 bigint,
  test_status                   varchar(6),
  constraint ck_test_test_status check (test_status in ('ACTIVE','DRAFT')),
  constraint pk_test primary key (id)
);

create table test_answer (
  id                            bigint auto_increment not null,
  answer                        varchar(255),
  test_question_id              bigint,
  is_correct                    char(1) DEFAULT '0',
  constraint pk_test_answer primary key (id)
);

create table test_program (
  id                            bigint auto_increment not null,
  test_id                       bigint not null,
  program_question              varchar(255),
  constraint pk_test_program primary key (id)
);

create table test_question (
  id                            bigint auto_increment not null,
  test_id                       bigint not null,
  question                      varchar(255),
  constraint pk_test_question primary key (id)
);

create table test_session (
  id                            bigint auto_increment not null,
  start_time                    datetime(6),
  end_time                      datetime(6),
  score                         bigint,
  test_taker_id                 bigint,
  test_id                       bigint,
  submitted                     tinyint(1) default 0,
  constraint pk_test_session primary key (id)
);

alter table program_submission add constraint fk_program_submission_test_program_id foreign key (test_program_id) references test_program (id) on delete restrict on update restrict;
create index ix_program_submission_test_program_id on program_submission (test_program_id);

alter table program_submission add constraint fk_program_submission_test_session_id foreign key (test_session_id) references test_session (id) on delete restrict on update restrict;
create index ix_program_submission_test_session_id on program_submission (test_session_id);

alter table test_answer add constraint fk_test_answer_test_question_id foreign key (test_question_id) references test_question (id) on delete restrict on update restrict;
create index ix_test_answer_test_question_id on test_answer (test_question_id);

alter table test_program add constraint fk_test_program_test_id foreign key (test_id) references test (id) on delete restrict on update restrict;
create index ix_test_program_test_id on test_program (test_id);

alter table test_question add constraint fk_test_question_test_id foreign key (test_id) references test (id) on delete restrict on update restrict;
create index ix_test_question_test_id on test_question (test_id);

alter table test_session add constraint fk_test_session_test_taker_id foreign key (test_taker_id) references app_user (id) on delete restrict on update restrict;
create index ix_test_session_test_taker_id on test_session (test_taker_id);

alter table test_session add constraint fk_test_session_test_id foreign key (test_id) references test (id) on delete restrict on update restrict;
create index ix_test_session_test_id on test_session (test_id);


# --- !Downs

alter table program_submission drop foreign key fk_program_submission_test_program_id;
drop index ix_program_submission_test_program_id on program_submission;

alter table program_submission drop foreign key fk_program_submission_test_session_id;
drop index ix_program_submission_test_session_id on program_submission;

alter table test_answer drop foreign key fk_test_answer_test_question_id;
drop index ix_test_answer_test_question_id on test_answer;

alter table test_program drop foreign key fk_test_program_test_id;
drop index ix_test_program_test_id on test_program;

alter table test_question drop foreign key fk_test_question_test_id;
drop index ix_test_question_test_id on test_question;

alter table test_session drop foreign key fk_test_session_test_taker_id;
drop index ix_test_session_test_taker_id on test_session;

alter table test_session drop foreign key fk_test_session_test_id;
drop index ix_test_session_test_id on test_session;

drop table if exists app_user;

drop table if exists program_submission;

drop table if exists test;

drop table if exists test_answer;

drop table if exists test_program;

drop table if exists test_question;

drop table if exists test_session;

