create table post (
    id serial primary key,
    title varchar(255) not null,
    date timestamp,
    description text not null,
    link varchar(255) not null
)