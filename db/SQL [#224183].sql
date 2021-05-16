
create table meetings
(
    id serial primary key,
    name varchar(255),
    request boolean
);

create table users
(
    id serial primary key,
    name varchar(255)
);

create table users_meetings
(
    user_id integer not null,
    meeting_id integer not null,
    foreign key(user_id) references users(id),
    foreign key(meeting_id) references meetings(id)
);

insert into users (name) values
('Ivan'),
('Andrey'),
('Evgeniy'),
('Boris');

insert into meetings (name, request) values
('conference', true),
('corporate_events', true),
('business_meeting', false);

insert into users_meetings values
(1, 1),
(1, 2),
(2, 2),
(3, 1),
(4, 3);

-- Нужно написать запрос, который получит список
-- всех заяков и количество подтвердивших участников.
select m.name as list_of_meetings, count(*) as count_users from meetings m
join users_meetings um on m.id = um.meeting_id
join users u on u.id = um.user_id
where m.request = true group by m.name

-- Нужно получить все совещания, где не было ни одной заявки на посещения
select m.name as meetings_without_users from meetings m
left join users_meetings um on m.id = um.meeting_id
left join users u on u.id = um.user_id
where m.request = false or m.request is null
