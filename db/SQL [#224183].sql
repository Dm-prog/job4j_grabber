
create table meetings
(
    Id serial primary key,
    Name varchar(255)
);

create table users
(
    Id serial primary key,
    Name varchar(255),
    status boolean,
    meetings_id integer references meetings(id)
);

insert into users (Name, status, meetings_id) values
('Ivan', true, 1),
('Andrey', true, 2),
('Evgeniy', true, 2),
('Boris', false, 1);

insert into meetings (Name) values
('conference'),
('corporate_events');

-- Нужно написать запрос, который получит список
-- всех заяков и количество подтвердивших участников.
select m.Name as list_of_meetings, count(*) as count_users from meetings m
join users u on m.Id = u.meetings_id where u.status = true group by m.Name

-- Нужно получить все совещания, где не было ни одной заявки на посещения
select m.Name as meetings_without_users from meetings m
join users u on m.Id = u.meetings_id where u.status = false
