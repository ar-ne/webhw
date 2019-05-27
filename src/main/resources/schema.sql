/*==============================================================*/
/* DBMS name:      MySQL 5.0                                    */
/* Created on:     2019/5/27 20:34:19                           */
/*==============================================================*/


alter table Introduction 
   drop foreign key FK_INTRODUC_REFERENCE_MANAGER;

alter table Loan 
   drop foreign key FK_LOAN_REFERENCE_CUSTOMER;

alter table Notification 
   drop foreign key FK_NOTIFICA_REFERENCE_CUSTOMER;

alter table Notification 
   drop foreign key FK_NOTIFICA_REFERENCE_INTRODUC;

alter table Notification 
   drop foreign key FK_NOTIFICA_REFERENCE_TICKET;

alter table Notification 
   drop foreign key FK_NOTIFICA_REFERENCE_LOAN;

alter table Opinion 
   drop foreign key FK_OPINION_REFERENCE_CUSTOMER;

alter table Orders 
   drop foreign key FK_ORDERS_REFERENCE_PRODUCTI;

alter table Orders 
   drop foreign key FK_ORDERS_REFERENCE_CUSTOMER;

alter table Production 
   drop foreign key FK_PRODUCTI_REFERENCE_MANAGER;

alter table Ticket 
   drop foreign key FK_TICKET_REFERENCE_CUSTOMER;

alter table Ticket 
   drop foreign key FK_TICKET_REFERENCE_STUFF;

drop index Index_1 on Customer;

drop table if exists Customer;

drop index Index_1 on Introduction;


alter table Introduction 
   drop foreign key FK_INTRODUC_REFERENCE_MANAGER;

drop table if exists Introduction;

drop index Index_1 on Loan;


alter table Loan 
   drop foreign key FK_LOAN_REFERENCE_CUSTOMER;

drop table if exists Loan;

drop index Index_1 on Log;

drop table if exists Log;

drop index Index_1 on Login;

drop table if exists Login;

drop index Index_1 on Manager;

drop table if exists Manager;

drop index Index_1 on Notification;


alter table Notification 
   drop foreign key FK_NOTIFICA_REFERENCE_CUSTOMER;

alter table Notification 
   drop foreign key FK_NOTIFICA_REFERENCE_INTRODUC;

alter table Notification 
   drop foreign key FK_NOTIFICA_REFERENCE_TICKET;

alter table Notification 
   drop foreign key FK_NOTIFICA_REFERENCE_LOAN;

drop table if exists Notification;

drop index Index_1 on Opinion;


alter table Opinion 
   drop foreign key FK_OPINION_REFERENCE_CUSTOMER;

drop table if exists Opinion;

drop index Index_1 on Orders;


alter table Orders 
   drop foreign key FK_ORDERS_REFERENCE_PRODUCTI;

alter table Orders 
   drop foreign key FK_ORDERS_REFERENCE_CUSTOMER;

drop table if exists Orders;

drop index Index_1 on Production;


alter table Production 
   drop foreign key FK_PRODUCTI_REFERENCE_MANAGER;

drop table if exists Production;

drop index Index_1 on Stuff;

drop table if exists Stuff;

drop index Index_1 on Ticket;


alter table Ticket 
   drop foreign key FK_TICKET_REFERENCE_CUSTOMER;

alter table Ticket 
   drop foreign key FK_TICKET_REFERENCE_STUFF;

drop table if exists Ticket;

/*==============================================================*/
/* Table: Customer                                              */
/*==============================================================*/
create table Customer
(
   c_id                 bigint not null auto_increment  comment 'c_id',
   name                 varchar(200)  comment 'name',
   phoneNum             varchar(200)  comment 'phoneNum',
   age                  int  comment 'age',
   sex                  int  comment 'sex',
   pid                  varchar(200)  comment 'pid',
   adress               varchar(254)  comment 'adress',
   joinTime             datetime  comment 'joinTime',
   primary key (c_id)
);

alter table Customer comment 'Customer';

/*==============================================================*/
/* Index: Index_1                                               */
/*==============================================================*/
create index Index_1 on Customer
(
   c_id,
   name,
   phoneNum,
   age,
   sex,
   pid,
   adress,
   joinTime
);

/*==============================================================*/
/* Table: Introduction                                          */
/*==============================================================*/
create table Introduction
(
   i_id                 bigint not null auto_increment  comment 'i_id',
   m_id                 bigint  comment 'm_id',
   type                 varchar(200)  comment 'type',
   pubTime              datetime  comment 'pubTime',
   content              varchar(2000)  comment 'content',
   primary key (i_id)
);

alter table Introduction comment 'Introduction';

/*==============================================================*/
/* Index: Index_1                                               */
/*==============================================================*/
create index Index_1 on Introduction
(
   i_id,
   m_id,
   type,
   pubTime
);

/*==============================================================*/
/* Table: Loan                                                  */
/*==============================================================*/
create table Loan
(
   l_id                 bigint not null auto_increment  comment 'l_id',
   c_id                 bigint  comment 'c_id',
   time                 datetime  comment 'time',
   money                int  comment 'money',
   reason               varchar(2000)  comment 'reason',
   l_accept             int  comment 'l_accept',
   primary key (l_id)
);

alter table Loan comment 'Loan';

/*==============================================================*/
/* Index: Index_1                                               */
/*==============================================================*/
create index Index_1 on Loan
(
   l_id,
   c_id,
   time,
   money,
   l_accept
);

/*==============================================================*/
/* Table: Log                                                   */
/*==============================================================*/
create table Log
(
   log_id               bigint not null auto_increment  comment 'log_id',
   user_id              bigint  comment 'user_id',
   user_type            varchar(100)  comment 'user_type',
   operation            varchar(1000)  comment 'operation',
   result               varchar(1000)  comment 'result',
   time                 datetime  comment 'time',
   primary key (log_id)
);

alter table Log comment 'Log';

/*==============================================================*/
/* Index: Index_1                                               */
/*==============================================================*/
create index Index_1 on Log
(
   log_id,
   user_id,
   user_type,
   time
);

/*==============================================================*/
/* Table: Login                                                 */
/*==============================================================*/
create table Login
(
   loginId              bigint not null auto_increment  comment 'loginId',
   type                 int  comment 'type',
   pass                 varchar(200)  comment 'pass',
   primary key (loginId)
);

alter table Login comment 'Login';

/*==============================================================*/
/* Index: Index_1                                               */
/*==============================================================*/
create index Index_1 on Login
(
   loginId,
   type
);

/*==============================================================*/
/* Table: Manager                                               */
/*==============================================================*/
create table Manager
(
   m_id                 bigint not null auto_increment  comment 'm_id',
   name                 varchar(200)  comment 'name',
   phoneNum             varchar(200)  comment 'phoneNum',
   age                  int  comment 'age',
   sex                  int  comment 'sex',
   pid                  varchar(200)  comment 'pid',
   adress               varchar(254)  comment 'adress',
   workYear             int  comment 'workYear',
   joinTime             datetime  comment 'joinTime',
   primary key (m_id)
);

alter table Manager comment 'Manager';

/*==============================================================*/
/* Index: Index_1                                               */
/*==============================================================*/
create index Index_1 on Manager
(
   m_id,
   name,
   phoneNum,
   age,
   sex,
   pid,
   adress,
   workYear,
   joinTime
);

/*==============================================================*/
/* Table: Notification                                          */
/*==============================================================*/
create table Notification
(
   n_id                 bigint not null auto_increment  comment 'n_id',
   c_id                 bigint  comment 'c_id',
   i_id                 bigint  comment 'i_id',
   t_id                 bigint  comment 't_id',
   l_id                 bigint  comment 'l_id',
   time                 datetime  comment 'time',
   n_read               int  comment 'n_read',
   primary key (n_id)
);

alter table Notification comment 'Notification';

/*==============================================================*/
/* Index: Index_1                                               */
/*==============================================================*/
create index Index_1 on Notification
(
   n_id,
   c_id,
   i_id,
   t_id,
   l_id,
   time,
   n_read
);

/*==============================================================*/
/* Table: Opinion                                               */
/*==============================================================*/
create table Opinion
(
   c_id                 bigint  comment 'c_id',
   o_id                 bigint not null auto_increment  comment 'o_id',
   type                 varchar(200)  comment 'type',
   time                 datetime  comment 'time',
   content              varchar(2000)  comment 'content',
   o_accept             int  comment 'o_accept',
   primary key (o_id)
);

alter table Opinion comment 'Opinion';

/*==============================================================*/
/* Index: Index_1                                               */
/*==============================================================*/
create index Index_1 on Opinion
(
   c_id,
   o_id,
   type,
   time,
   o_accept
);

/*==============================================================*/
/* Table: Orders                                                */
/*==============================================================*/
create table Orders
(
   p_id                 int  comment 'p_id',
   c_id                 bigint  comment 'c_id',
   or_id                bigint not null auto_increment  comment 'or_id',
   buyTime              datetime  comment 'buyTime',
   buyMoney             datetime  comment 'buyMoney',
   buyDuration          int  comment 'buyDuration',
   primary key (or_id)
);

alter table Orders comment 'Orders';

/*==============================================================*/
/* Index: Index_1                                               */
/*==============================================================*/
create index Index_1 on Orders
(
   p_id,
   c_id,
   or_id,
   buyTime,
   buyMoney,
   buyDuration
);

/*==============================================================*/
/* Table: Production                                            */
/*==============================================================*/
create table Production
(
   m_id                 bigint  comment 'm_id',
   p_id                 int not null auto_increment  comment 'p_id',
   type                 varchar(200)  comment 'type',
   risk                 int  comment 'risk',
   profit               double  comment 'profit',
   pMin                 double  comment 'pMin',
   content              varchar(2000)  comment 'content',
   primary key (p_id)
);

alter table Production comment 'Production';

/*==============================================================*/
/* Index: Index_1                                               */
/*==============================================================*/
create index Index_1 on Production
(
   m_id,
   p_id,
   type,
   risk,
   profit,
   pMin
);

/*==============================================================*/
/* Table: Stuff                                                 */
/*==============================================================*/
create table Stuff
(
   s_id                 bigint not null auto_increment  comment 's_id',
   name                 varchar(200)  comment 'name',
   phoneNum             varchar(200)  comment 'phoneNum',
   age                  int  comment 'age',
   sex                  int  comment 'sex',
   pid                  varchar(200)  comment 'pid',
   adress               varchar(254)  comment 'adress',
   workYear             int  comment 'workYear',
   joinTime             datetime  comment 'joinTime',
   primary key (s_id)
);

alter table Stuff comment 'Stuff';

/*==============================================================*/
/* Index: Index_1                                               */
/*==============================================================*/
create index Index_1 on Stuff
(
   s_id,
   name,
   phoneNum,
   age,
   sex,
   pid,
   adress,
   workYear,
   joinTime
);

/*==============================================================*/
/* Table: Ticket                                                */
/*==============================================================*/
create table Ticket
(
   t_id                 bigint not null auto_increment  comment 't_id',
   c_id                 bigint  comment 'c_id',
   s_id                 bigint  comment 's_id',
   tpye                 varchar(200)  comment 'tpye',
   content              varchar(2000)  comment 'content',
   time                 datetime  comment 'time',
   ansTime              datetime  comment 'ansTime',
   reply                varchar(2000)  comment 'reply',
   primary key (t_id)
);

alter table Ticket comment 'Ticket';

/*==============================================================*/
/* Index: Index_1                                               */
/*==============================================================*/
create index Index_1 on Ticket
(
   t_id,
   c_id,
   s_id,
   tpye,
   time,
   ansTime
);

alter table Introduction add constraint FK_INTRODUC_REFERENCE_MANAGER foreign key (m_id)
      references Manager (m_id) on delete restrict on update restrict;

alter table Loan add constraint FK_LOAN_REFERENCE_CUSTOMER foreign key (c_id)
      references Customer (c_id) on delete restrict on update restrict;

alter table Notification add constraint FK_NOTIFICA_REFERENCE_CUSTOMER foreign key (c_id)
      references Customer (c_id) on delete restrict on update restrict;

alter table Notification add constraint FK_NOTIFICA_REFERENCE_INTRODUC foreign key (i_id)
      references Introduction (i_id) on delete restrict on update restrict;

alter table Notification add constraint FK_NOTIFICA_REFERENCE_TICKET foreign key (t_id)
      references Ticket (t_id) on delete restrict on update restrict;

alter table Notification add constraint FK_NOTIFICA_REFERENCE_LOAN foreign key (l_id)
      references Loan (l_id) on delete restrict on update restrict;

alter table Opinion add constraint FK_OPINION_REFERENCE_CUSTOMER foreign key (c_id)
      references Customer (c_id) on delete restrict on update restrict;

alter table Orders add constraint FK_ORDERS_REFERENCE_PRODUCTI foreign key (p_id)
      references Production (p_id) on delete restrict on update restrict;

alter table Orders add constraint FK_ORDERS_REFERENCE_CUSTOMER foreign key (c_id)
      references Customer (c_id) on delete restrict on update restrict;

alter table Production add constraint FK_PRODUCTI_REFERENCE_MANAGER foreign key (m_id)
      references Manager (m_id) on delete restrict on update restrict;

alter table Ticket add constraint FK_TICKET_REFERENCE_CUSTOMER foreign key (c_id)
      references Customer (c_id) on delete restrict on update restrict;

alter table Ticket add constraint FK_TICKET_REFERENCE_STUFF foreign key (s_id)
      references Stuff (s_id) on delete restrict on update restrict;

