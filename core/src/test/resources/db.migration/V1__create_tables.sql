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

create table if not exists gift_certificate_tag
(
    gift_certificate_id integer not null,
    tag_id integer not null,
    foreign key(gift_certificate_id) references gift_certificate(id) on delete cascade,
    foreign key(tag_id) references tag(id) on delete cascade
);

create table if not exists users
(
    id serial primary key,
    name character varying(30) not null
);

create table if not exists user_order
(
    id serial primary key,
    cost numeric not null,
    purchase_date timestamp without time zone NOT NULL,
    user_id integer not null,
    certificate_id integer not null,
    foreign key(user_id) references users(id),
    foreign key(certificate_id) references gift_certificate(id)
);



