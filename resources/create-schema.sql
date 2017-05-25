create table if not exists user (
       id int auto_increment primary key,
       name varchar(256),
       email varchar(256)
);

--;;

create table if not exists contact (
       id int auto_increment primary key,
       name varchar(256),
       user_id int,
       foreign key (user_id) references user(id)
);

--;;

create table if not exists phone_number (
       id int auto_increment primary key,
       number varchar(20),
       contact_id int,
       foreign key (contact_id) references contact(id)
);

