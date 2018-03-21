-- important the order of table deleting (reverse order)
DELETE FROM meals;
DELETE FROM user_roles;
DELETE FROM users;

-- reset global sequences
ALTER SEQUENCE global_seq RESTART WITH 100000;
ALTER SEQUENCE meal_id RESTART WITH 100000;

INSERT INTO users (name, email, password) VALUES
  ('User', 'user@yandex.ru', 'password'),
  ('Admin', 'admin@gmail.com', 'admin');

INSERT INTO user_roles (role, user_id) VALUES
  ('ROLE_USER', 100000),
  ('ROLE_ADMIN', 100001);

/*INSERT INTO meals (datetime, description, calories) VALUES
  ('2015-05-30 10:00', 'Завтрак', 500),
  ('2015-05-30 13:00', 'Обед', 1000),
  ('2015-05-30 20:00', 'Ужин', 500),
  ('2015-05-31 10:00', 'Завтрак', 1000),
  ('2015-05-31 13:00', 'Обед', 500),
  ('2015-05-31 20:00', 'Ужин', 510);*/

-- to use in testing
INSERT INTO meals (user_id, datetime, description, calories) VALUES
  (100000, '2015-05-31 10:00', 'Завтрак', 500),
  (100000, '2015-05-30 13:00', 'Обед', 1000),
  (100001, '2015-05-29 15:00', 'Обед', 1000);
