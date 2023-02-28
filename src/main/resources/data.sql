create table bd.users
(
    id bigint not null auto_increment,
    username varchar(255),
    name varchar(255),
    lastname varchar(255),
    age bigint,
    email varchar(255),
    password varchar(255),
    primary key (id)
);
create table bd.roles
(
    id bigint not null,
    name varchar(255),
    primary key (id)
);

create table bd.users_roles
(
    user_id bigint not null,
    roles_id bigint not null,
    primary key (user_id,roles_id),
    foreign key (user_id) references bd.users(id),
    foreign key (roles_id) references bd.roles(id)
);

        insert into bd.roles (id, name)
values  (1, 'ROLE_USER'),
        (2, 'ROLE_ADMIN');

insert into bd.users ( username, name, lastname, age, email, password)
values
    ('Admin', 'Pavel','Poltorak', 36, 'pavel@mail.ru', '$2a$12$9nD21fnPW85rFL/6t5YDPujPD8EwqWujgt23DSOhbNn6LT2E5UQ6.'),
    ('Oleg', 'Oleg','Bochkov', 40, 'oleg@mail.ru', '$2a$12$9nD21fnPW85rFL/6t5YDPujPD8EwqWujgt23DSOhbNn6LT2E5UQ6.'),
    ('ilnur', 'Ilnur','Batruha', 41, 'ilnur@mail.ru', '$2a$12$9nD21fnPW85rFL/6t5YDPujPD8EwqWujgt23DSOhbNn6LT2E5UQ6.'),
    ('qwerty', 'qwerty','qwerty', 37, 'qwerty@mail.ru', '$2a$12$9nD21fnPW85rFL/6t5YDPujPD8EwqWujgt23DSOhbNn6LT2E5UQ6.');

insert into bd.users_roles (user_id, roles_id)
values  (1, 1),
        (1, 2),
        (2, 1),
        (3, 1),
        (4, 1);

