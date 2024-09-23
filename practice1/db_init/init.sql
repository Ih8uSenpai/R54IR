-- db_init/init.sql

CREATE TABLE IF NOT EXISTS users (
    id SERIAL PRIMARY KEY,
    name VARCHAR(20) NOT NULL,
    surname VARCHAR(40) NOT NULL
);

INSERT INTO users (name, surname)
VALUES
    ('Alex', 'Rover'),
    ('Bob', 'Marley'),
    ('Kate', 'Yandson'),
    ('Lilo', 'Black')
ON CONFLICT DO NOTHING;
