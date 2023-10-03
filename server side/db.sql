use growing_tree;

# 用户表
drop table if exists test_user;
create table test_user
(
    id        int primary key not null auto_increment comment 'Id',
    user_name varchar(64)     not null unique comment '用户名',
    password  varchar(64)     not null comment '密码',
    nick_name varchar(64)     not null comment '昵称',
    gender    tinyint comment '性别,1:男,0:女',
    age       int comment '年龄',
    address   varchar(512) comment '住址',
    email     varchar(128) comment '邮箱地址'
);


insert into test_user (user_name, password, nick_name)
values ('123', '123', 'TestNick'),
       ('456', '456', 'TestNick2');


#宝宝表
drop table if exists test_baby;
create table test_baby
(
    id           int primary key not null auto_increment comment 'Id',
    uid          int             not null comment '用户Id',
    nick_name    varchar(64)     not null unique comment '宝宝名字',
    gender       tinyint comment '性别,1:男,0:女',
    birthday     datetime comment '生日',
    head_img_url varchar(512) comment '头像url'
    /* age                int comment '年龄',
     height             varchar(12) comment '身高',
     weight             varchar(12) comment '体重',
     head_circumference varchar(12) comment '头围',
     bmi                varchar(12) comment '体重指数',
     photo_url          varchar(512) comment '图片url'*/
);

#记录表
drop table if exists test_baby_record;
create table test_baby_record
(
    id                 int primary key not null auto_increment comment 'Id',
    baby_id            int             not null comment '宝宝Id',
    height             varchar(12) comment '身高',
    weight             varchar(12) comment '体重',
    head_circumference varchar(12) comment '头围',
    bmi                varchar(12) comment '体重指数',
    photo_url          varchar(512) comment '图片url'
);


#里程碑表
drop table if exists test_milestone;
create table test_milestone
(
    id           int primary key not null auto_increment comment 'Id',
    title        varchar(255)    not null comment '标题',
    des          varchar(1024) comment '描述',
    assessment   varchar(1024) comment '建议',
    photo_url    varchar(512) comment '图片url',
    created_time datetime        not null default current_timestamp comment '创建时间'
);

insert into test_milestone(title, des, assessment, photo_url, created_time) value (
    '第一次叫爸爸', 'xxxxxxa','kad;fkk','http://www.xxabcd','2019-12-20 18:12'
    );
