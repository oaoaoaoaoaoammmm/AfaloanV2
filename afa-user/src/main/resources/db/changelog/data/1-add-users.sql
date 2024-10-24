insert into application.users (username,
                               password,
                               confirmed,
                               confirmed_username,
                               blocked,
                               role)
VALUES ('supervisor', '$2a$10$J4KjRrjR1SzK0aSB9ugSFOFuyUNZzWv6cIIjeSW8CT1TmlV15blZO', true, true, false, 'SUPERVISOR'),
       ('worker123', '$2a$10$92WKWFbkcadKpFHsLKEscexNZd17x.iLzUUtuBaPK9uoYUs/AllfK', true, true, false, 'WORKER'),
       ('customer123', '$2a$10$tEUzUTVex4BF6ZfB99GI4.TWankH01Ziovn3AcKQ7K67bqhh2AYfy', true, true, false, 'CUSTOMER');
