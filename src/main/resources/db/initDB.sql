-- important the order of table deleting (reverse order)
DROP TABLE IF EXISTS meals;
DROP TABLE IF EXISTS user_roles;
DROP TABLE IF EXISTS users;
DROP SEQUENCE IF EXISTS global_seq;
DROP SEQUENCE IF EXISTS meal_id;

CREATE SEQUENCE global_seq INCREMENT  BY 1
  START 100000; -- is used in user table
CREATE SEQUENCE meal_id INCREMENT  BY 1
  START 100000; -- is used in meals table

CREATE TABLE users
(
  id               INTEGER PRIMARY KEY DEFAULT nextval('global_seq'), -- id start with 100000
  name             VARCHAR                 NOT NULL,
  email            VARCHAR                 NOT NULL,
  password         VARCHAR                 NOT NULL,
  registered       TIMESTAMP DEFAULT now() NOT NULL,
  enabled          BOOL DEFAULT TRUE       NOT NULL,
  calories_per_day INTEGER DEFAULT 2000    NOT NULL
);
CREATE UNIQUE INDEX users_unique_email_idx
  ON users (email); -- email mast be unique

CREATE TABLE user_roles
(
  user_id INTEGER NOT NULL,
  role    VARCHAR,
  CONSTRAINT user_roles_idx UNIQUE (user_id, role), -- primary key with 2 fields
  FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);

CREATE TABLE public.meals
(
  id          INT PRIMARY KEY  DEFAULT nextval('meal_id'),
  user_id     INT              DEFAULT 100000,
  dateTime    TIMESTAMP        NOT NULL,
  description VARCHAR          NOT NULL,
  calories    INT DEFAULT 2000 NOT NULL,
  FOREIGN KEY (user_id) REFERENCES users (id)
);
CREATE UNIQUE INDEX meals_id_uindex
  ON public.meals (id);