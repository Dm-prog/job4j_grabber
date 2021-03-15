create table post (
    id serial primary key,
    title varchar(255) not null,
    author varchar(255) not null,
    date timestamp,
    description text not null
)