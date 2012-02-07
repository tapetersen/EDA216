-- Delete the tables if they exist. Set foreign_key_checks = 0 to
-- disable foreign key checks, so the tables may be dropped in
-- arbitrary order.

set foreign_key_checks = 0;
drop table if exists theatre;

set foreign_key_checks = 1;

-- Create the tables.
create table Theater (
    name varchar(255) not null,
    seats integer not null,
    primary key (name)
);

create table Movie (
    name varchar(255) not null,
    primary key (name)
);

create table Performance (
    show_date date not null,
    movie_name varchar(255) not null,
    theatre_name varchar(255) not null,
    primary key (show_date, movie_name),
    foreign key (movie_name) references Movie(name)
        on delete cascade on update cascade,
    foreign key (theatre_name) references Theater(name)
        on delete cascade on update cascade
);

create table User (
    user_name varchar(255),
    name varchar(255),
    address varchar(255) default '',
    phone varchar(20),
    primary key (user_name)
);

create table Reservation (
    nbr integer auto_increment,
    user_name varchar(255),
    show_date date,
    movie_name varchar(255)
    primary key (nbr)
    foreign key (user_name) references User(name)
        on delete cascade on update cascade,
    foreign key (movie_name, show_date) references Performance(movie_name, show_date)
        on delete cascade on update cascade
);

-- Insert data into the tables.
insert into User(user_name, name, adress, phone) values
('A1', 'Anna', 'testgatan 1', '1234567'),
('P1', 'Panna', 'testgatan 2', '1234567'),
('S1', 'Sanna', 'testgatan 3', null);

insert into Theatre(name, seats) values
('Kino', 123),
('Filmstaden', 234),
('Royal', 2);

insert into Movie(name) values
('Pippi'),
('Bamse'),
('Den stora kapplöpningen');

insert into Performance(show_date, theatre_name, movie_name) values
(2011-02-12, 'Kino', 'Pippi'),
(2011-04-14, 'Kino', 'Den stora kapplöpningen'),
(2011-01-27, 'Royal', 'Den stora kapplöpningen');

-- List show movies
-- SELECT name, show_date, FROM Movie 
