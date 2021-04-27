CREATE TABLE company
(
    id integer NOT NULL,
    name character varying,
    CONSTRAINT company_pkey PRIMARY KEY (id)
);

CREATE TABLE person
(
    id integer NOT NULL,
    name character varying,
    company_id integer,
    CONSTRAINT person_pkey PRIMARY KEY (id)
);

-- имена всех person, которые не состоят в компании с id = 5 и
-- название компании для каждого человека.
select p.name, c.name from person p join company c on c.id = p.company_id and p.company_id != 5;

-- Необходимо выбрать название компании с максимальным количеством
-- человек + количество человек в этой компании.
select c.name, count(p.company_id) as max_count_people_in_company
from person p join company c on c.id = p.company_id GROUP BY c.name order by count(*) desc
limit 1;