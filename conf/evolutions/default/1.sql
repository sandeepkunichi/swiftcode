# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table app_user (
  id                            bigint auto_increment not null,
  email                         varchar(255),
  password                      varchar(255),
  candidate_information_id      bigint,
  role                          varchar(5),
  resume_submitted              tinyint(1) default 0,
  constraint ck_app_user_role check (role in ('ADMIN','USER')),
  constraint uq_app_user_candidate_information_id unique (candidate_information_id),
  constraint pk_app_user primary key (id)
);

create table candidate_information (
  id                            bigint auto_increment not null,
  first_name                    varchar(255),
  middle_name                   varchar(255),
  last_name                     varchar(255),
  date_of_birth                 datetime(6),
  contact_number                varchar(255),
  gender                        integer,
  mobile_number                 varchar(255),
  address                       TEXT,
  institution                   varchar(255),
  high_school_marks             double,
  high_school_year              integer,
  secondary_marks               double,
  secondary_year                integer,
  sem1marks                     double,
  sem2marks                     double,
  sem3marks                     double,
  sem4marks                     double,
  sem5marks                     double,
  sem6marks                     double,
  sem7marks                     double,
  sem8marks                     double,
  under_graduate_aggregate      double,
  engineering_stream            integer,
  under_graduate_university     varchar(255),
  constraint ck_candidate_information_gender check (gender in (0,1)),
  constraint ck_candidate_information_engineering_stream check (engineering_stream in (0,1,2)),
  constraint pk_candidate_information primary key (id)
);

create table code_session_configuration (
  id                            bigint auto_increment not null,
  execute                       tinyint(1) default 0,
  compile                       tinyint(1) default 0,
  constraint pk_code_session_configuration primary key (id)
);

create table code_session_configuration_language (
  code_session_configuration_id bigint not null,
  language_id                   bigint not null,
  constraint pk_code_session_configuration_language primary key (code_session_configuration_id,language_id)
);

create table language (
  id                            bigint auto_increment not null,
  name                          varchar(255),
  language_type                 integer,
  constraint ck_language_language_type check (language_type in (0,1,2,3)),
  constraint pk_language primary key (id)
);

create table profile_picture (
  id                            bigint auto_increment not null,
  file_data                     longblob,
  candidate_information_id      bigint,
  constraint uq_profile_picture_candidate_information_id unique (candidate_information_id),
  constraint pk_profile_picture primary key (id)
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

create table registration (
  id                            bigint auto_increment not null,
  name                          varchar(255),
  email                         varchar(255),
  constraint pk_registration primary key (id)
);

create table test (
  id                            bigint auto_increment not null,
  title                         varchar(255),
  test_duration                 bigint,
  code_session_configuration_id bigint,
  instructions                  TEXT,
  test_status                   varchar(6),
  constraint ck_test_test_status check (test_status in ('ACTIVE','DRAFT')),
  constraint uq_test_code_session_configuration_id unique (code_session_configuration_id),
  constraint pk_test primary key (id)
);

create table test_answer (
  id                            bigint auto_increment not null,
  answer                        TEXT,
  test_question_id              bigint,
  is_correct                    char(1) DEFAULT '0',
  constraint pk_test_answer primary key (id)
);

create table test_program (
  id                            bigint auto_increment not null,
  test_id                       bigint not null,
  program_question              TEXT,
  constraint pk_test_program primary key (id)
);

create table test_question (
  id                            bigint auto_increment not null,
  test_id                       bigint not null,
  question                      TEXT,
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

alter table app_user add constraint fk_app_user_candidate_information_id foreign key (candidate_information_id) references candidate_information (id) on delete restrict on update restrict;

alter table code_session_configuration_language add constraint fk_code_session_configuration_language_code_session_confi_1 foreign key (code_session_configuration_id) references code_session_configuration (id) on delete restrict on update restrict;
create index ix_code_session_configuration_language_code_session_confi_1 on code_session_configuration_language (code_session_configuration_id);

alter table code_session_configuration_language add constraint fk_code_session_configuration_language_language foreign key (language_id) references language (id) on delete restrict on update restrict;
create index ix_code_session_configuration_language_language on code_session_configuration_language (language_id);

alter table profile_picture add constraint fk_profile_picture_candidate_information_id foreign key (candidate_information_id) references candidate_information (id) on delete restrict on update restrict;

alter table program_submission add constraint fk_program_submission_test_program_id foreign key (test_program_id) references test_program (id) on delete restrict on update restrict;
create index ix_program_submission_test_program_id on program_submission (test_program_id);

alter table program_submission add constraint fk_program_submission_test_session_id foreign key (test_session_id) references test_session (id) on delete restrict on update restrict;
create index ix_program_submission_test_session_id on program_submission (test_session_id);

alter table test add constraint fk_test_code_session_configuration_id foreign key (code_session_configuration_id) references code_session_configuration (id) on delete restrict on update restrict;

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

alter table app_user drop foreign key fk_app_user_candidate_information_id;

alter table code_session_configuration_language drop foreign key fk_code_session_configuration_language_code_session_confi_1;
drop index ix_code_session_configuration_language_code_session_confi_1 on code_session_configuration_language;

alter table code_session_configuration_language drop foreign key fk_code_session_configuration_language_language;
drop index ix_code_session_configuration_language_language on code_session_configuration_language;

alter table profile_picture drop foreign key fk_profile_picture_candidate_information_id;

alter table program_submission drop foreign key fk_program_submission_test_program_id;
drop index ix_program_submission_test_program_id on program_submission;

alter table program_submission drop foreign key fk_program_submission_test_session_id;
drop index ix_program_submission_test_session_id on program_submission;

alter table test drop foreign key fk_test_code_session_configuration_id;

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

drop table if exists candidate_information;

drop table if exists code_session_configuration;

drop table if exists code_session_configuration_language;

drop table if exists language;

drop table if exists profile_picture;

drop table if exists program_submission;

drop table if exists registration;

drop table if exists test;

drop table if exists test_answer;

drop table if exists test_program;

drop table if exists test_question;

drop table if exists test_session;

