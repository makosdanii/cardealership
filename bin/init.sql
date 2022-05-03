DROP TABLE if exists store;
DROP TABLE if exists MANAGES;
DROP TABLE if exists BRAND;
DROP TABLE if exists REGION;
DROP TABLE if exists USERS;
DROP TABLE if exists ROLES;

CREATE TABLE if not exists ROLES(
	id int primary key auto_increment,
	role_name varchar(64) not null unique
);
    
INSERT INTO ROLES(role_name) VALUES ("global");
-- INSERT INTO ROLES(role_name) VALUES ("regional-DE");
-- INSERT INTO ROLES(role_name) VALUES ("regional-JP");

CREATE TABLE if not exists USERS(
	id int primary key auto_increment,
	email varchar(256) not null unique,
	name varchar(128) not null,
	drivers_license bool,
    password varchar(256) not null,
    role_id int not null,
    foreign key(role_id) references ROLES(id)
);
    
INSERT INTO USERS(email, name, password, role_id) VALUES("globalmanager@mydomain.com", "Global Manager", "Test123!", 1);
-- INSERT INTO USERS(email, name, password, drivers_license, role_id) VALUES("masteryi@mydomain.com", "Master Yi", "Test123!", 2, 3);

CREATE TABLE if not exists REGION(
	id int primary key auto_increment,
    region_name varchar(64) not null unique);
    
INSERT INTO REGION(region_name) VALUES ("Germany");
INSERT INTO REGION(region_name) VALUES ("Japan");

CREATE TABLE if not exists BRAND(
	id int primary key auto_increment,
	brand_name varchar(64) not null unique,
    region_id int not null,
    foreign key(region_id) references REGION(id)
);

INSERT INTO BRAND(brand_name, region_id) VALUES ("BMW", "1");
INSERT INTO BRAND(brand_name, region_id) VALUES ("Audi", "1");
INSERT INTO BRAND(brand_name, region_id) VALUES ("Porsche", "1");
INSERT INTO BRAND(brand_name, region_id) VALUES ("Nissan", "2");
INSERT INTO BRAND(brand_name, region_id) VALUES ("Honda", "2");
INSERT INTO BRAND(brand_name, region_id) VALUES ("Toyota", "2");

CREATE TABLE if not exists store(
	id int primary key auto_increment,
    model varchar(64) not null,
    quantity int,
	brand_id int not null,
    user_id int not null,
	CONSTRAINT FK_JoinUserBrands
    foreign key(brand_id) references BRAND(id),
    foreign key(user_id) references USERS(id)
);

CREATE TABLE if not exists MANAGES(
	role_id int not null,
    region_id int not null,
    CONSTRAINT FK_JoinRoleRegion
    foreign key(role_id) references ROLES(id),
    foreign key(region_id) references REGION(id)
);

INSERT INTO MANAGES(role_id, region_id) VALUES (1,1);
INSERT INTO MANAGES(role_id, region_id) VALUES (1,2);
-- INSERT INTO MANAGES(role_id, region_id) VALUES (2,1);
-- INSERT INTO MANAGES(role_id, region_id) VALUES (3,2);

-- select users.name, brand.brand_name, model, quantity
-- from store
-- inner join users on users.id = user_id
-- inner join brand on brand.id = brand_id;

-- select roles.role_name, region.region_name
-- from manages
-- inner join roles on roles.id = role_id
-- inner join region on region.id = region_id;

-- select * from users; 
-- select * from brand; 
-- select * from roles;
-- select * from store;