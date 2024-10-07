insert into afaloan.application.roles (name)
values ('SUPERVISOR'),
       ('WORKER'),
       ('CUSTOMER');

insert into afaloan.application.users (username,
                                       password,
                                       confirmed,
                                       confirmed_username,
                                       blocked)
VALUES ('supervisor', '$2a$10$J4KjRrjR1SzK0aSB9ugSFOFuyUNZzWv6cIIjeSW8CT1TmlV15blZO', true, true, false),
       ('worker123', '$2a$10$92WKWFbkcadKpFHsLKEscexNZd17x.iLzUUtuBaPK9uoYUs/AllfK', true, true, false),
       ('customer123', '$2a$10$tEUzUTVex4BF6ZfB99GI4.TWankH01Ziovn3AcKQ7K67bqhh2AYfy', true, true, false);

insert into afaloan.application.user_roles (user_id,
                                            role_id)
VALUES ((select id from afaloan.application.users where username = 'supervisor'),
        (select id from afaloan.application.roles where name = 'SUPERVISOR')),
       ((select id from afaloan.application.users where username = 'worker123'),
        (select id from afaloan.application.roles where name = 'WORKER')),
       ((select id from afaloan.application.users where username = 'customer123'),
        (select id from afaloan.application.roles where name = 'CUSTOMER'))
on conflict do nothing;

insert into afaloan.application.profiles (name,
                                          surname,
                                          patronymic,
                                          phone_number,
                                          passport_series,
                                          passport_number,
                                          snils,
                                          inn,
                                          monthly_income,
                                          user_id)
VALUES ('vanya','vanin','vanichslav','+71408025486','1234','123456','snils','inn',300.00,
        (select id from afaloan.application.users where username = 'worker123'))
on conflict do nothing;