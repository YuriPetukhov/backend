-- liquibase formatted sql

-- changeset YuriPetukhov:1

-- Создание таблицы "images" для хранения путей к изображениям
CREATE TABLE images (
  id SERIAL PRIMARY KEY, -- Уникальный идентификатор изображения
  path VARCHAR(500) -- Путь к изображению
);

-- Создание таблицы "users" для хранения информации о пользователях
CREATE TABLE users (
  id SERIAL PRIMARY KEY, -- Уникальный идентификатор пользователя
  email VARCHAR(320), -- Адрес электронной почты пользователя
  first_name VARCHAR(60), -- Имя пользователя
  last_name VARCHAR(60), -- Фамилия пользователя
  phone VARCHAR(20), -- Номер телефона пользователя
  role VARCHAR(20), -- Роль пользователя
  image_id INT DEFAULT NULL, -- Идентификатор изображения пользователя
  FOREIGN KEY (image_id) REFERENCES images(id) -- Внешний ключ, связывающий с таблицей "images"
);

-- Создание таблицы "ads" для хранения информации о объявлениях
CREATE TABLE ads (
  pk SERIAL PRIMARY KEY, -- Уникальный идентификатор объявления
  price INT, -- Цена объявления
  title VARCHAR(150), -- Заголовок объявления
  description VARCHAR(255), -- Описание объявления
  image_id INT, -- Идентификатор изображения объявления
  FOREIGN KEY (image_id) REFERENCES images(id), -- Внешний ключ, связывающий с таблицей "images"
  user_id INT, -- Идентификатор пользователя, разместившего объявление
  FOREIGN KEY (user_id) REFERENCES users(id) -- Внешний ключ, связывающий с таблицей "users"
);

-- Создание таблицы "comments" для хранения комментариев к объявлениям
CREATE TABLE comments (
  pk SERIAL PRIMARY KEY, -- Уникальный идентификатор комментария
  created_at BIGINT, -- Время создания комментария
  text VARCHAR(255), -- Текст комментария
  user_id SERIAL, -- Идентификатор пользователя, оставившего комментарий
  FOREIGN KEY (user_id) REFERENCES users(id), -- Внешний ключ, связывающий с таблицей "users"
  ad_pk SERIAL, -- Идентификатор объявления, к которому оставлен комментарий
  FOREIGN KEY (ad_pk) REFERENCES ads(pk) -- Внешний ключ, связывающий с таблицей "ads"
);

-- Создание таблицы "authentications" для хранения информации аутентификации пользователей
CREATE TABLE authentications (
  id SERIAL PRIMARY KEY, -- Уникальный идентификатор аутентификации
  password_hash VARCHAR(100), -- Хэш пароля пользователя
  user_id SERIAL, -- Идентификатор пользователя
  FOREIGN KEY (user_id) REFERENCES users(id) -- Внешний ключ, связывающий с таблицей "users"
);
