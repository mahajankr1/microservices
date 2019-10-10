SET sql_mode = '';

create table if not exists  customer (
  id int(11) not null auto_increment,
  firstName varchar(512) default null,
  lastName varchar(512) default null,
  dlNumber varchar(512) default null,
  zipcode varchar(512) default null,  
  primary key (id)
) engine=innodb ;








