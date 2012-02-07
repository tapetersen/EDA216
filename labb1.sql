select firstName, lastName
from Students;

select firstName, lastName
from Students
order by lastName, firstName;

select *
from Students
where pNbr like '75%';

select firstName, lastName, pNbr
where mod(substr(pNbr, 10, 1), 2) = 0;

select count(*)
from Students;

select * 
from Courses
where courseCode like 'FMA%';

select *
from Courses
where credits > 5;

select courseCode
from TakenCourses
where pNbr = '790101-1234';

select courseName, credits
from Courses
join TakenCourses on (TakenCourses.courseCode = Courses.courseCode)
where TakenCourses.pNbr = '790101-1234';

select sum(credits)
from Courses c
join TakenCourses tc on (tc.courseCode = c.courseCode)
where tc.pNbr = '790101-1234';

select avg(tc.grade)
from Courses c
join TakenCourses tc on (tc.courseCode = c.courseCode)
where tc.pNbr = '790101-1234';

select courseCode
from TakenCourses tc
join Students s on (s.pNbr = tc.pNbr)
where s.firstName = 'Eva' and s.lastName = 'Alm';

select courseName, credits
from Courses c
join TakenCourses tc on (tc.courseCode = c.courseCode)
join Students s on (tc.pNbr = s.pNbr)
where s.firstName = 'Eva' and s.lastName = 'Alm';

select sum(credits)
from Courses c
join TakenCourses tc on (tc.courseCode = c.courseCode)
join Students s on (tc.pNbr = s.pNbr)
where s.firstName = 'Eva' and s.lastName = 'Alm';

select avg(tc.grade)
from Courses c
join TakenCourses tc on (tc.courseCode = c.courseCode)
join Students s on (tc.pNbr = s.pNbr)
where s.firstName = 'Eva' and s.lastName = 'Alm';

select s.firstName, s.lastName, sum(COALESCE(c.credits, 0)) as c_sum
from Students s
left join TakenCourses tc on (s.pNbr = s.pNbr)
join Courses c on(tc.courseCode = c.courseCode)
group by s.firstName, s.lastName
having c_sum = 0;

select s.firstName, s.lastName, avg(tc.grade) as g_avg
from Students s
join TakenCourses tc on (tc.pNbr = s.pnBr)
order by g_avg DESC
limit 0, 5;

select s.pNbr, sum(COALESCE(c.credits, 0))
from Students s
left join TakenCourses tc on (s.pNbr = s.pNbr)
join Courses c on(tc.courseCode = c.courseCode)
group by s.firstName, s.lastName;

select s.firstName, s.lastName, sum(COALESCE(c.credits, 0))
from Students s
left join TakenCourses tc on (s.pNbr = s.pNbr)
join Courses c on(tc.courseCode = c.courseCode)
group by s.firstName, s.lastName;

select s.firstName, s.lastName, s.pNbr
from Students s
join Students ss on (s.firstName = ss.firstName and s.lastName = ss.lastName and s.pNbr != ss.pNbr);


create table Students (
    pNbr
    char(11),
    firstName varchar(20) not null,
    lastName varchar(20) not null,
    primary key (pNbr)
);
create table Courses (
    courseCode char(6),
    courseName varchar(50) not null,
    credits
    integer not null check (credits > 0),
    primary key (courseCode)
);
create table TakenCourses (
    pNbr
    char(11),
    courseCode char(6),
    grade
    integer not null check (grade >= 3 and grade <= 5),
    primary key (pNbr, courseCode),
    foreign key (pNbr) references Students(pNbr),
    foreign key (courseCode) references Courses(courseCode)
);

