create table if not exists gift_certificate
(
    id serial primary key,
    name character varying(30) not null,
    description text not null,
    price numeric NOT NULL,
    duration integer NOT NULL,
    create_date timestamp without time zone NOT NULL,
    last_update_date timestamp without time zone NOT NULL
);

create table if not exists tag
(
    id serial primary key,
    name character varying(30) not null
);

create table if not exists gift_certificate_tag
(
    gift_certificate_id integer not null,
    tag_id integer not null,
    foreign key(gift_certificate_id) references gift_certificate(id) on delete cascade,
    foreign key(tag_id) references tag(id) on delete cascade
);