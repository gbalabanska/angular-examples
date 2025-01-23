-- foreign keys in mind!
DROP TABLE IF EXISTS message;
DROP TABLE IF EXISTS channel_user;
DROP TABLE IF EXISTS channel;
DROP TABLE IF EXISTS user_friend;
DROP TABLE IF EXISTS user;

CREATE TABLE `user` (
    `id` INT AUTO_INCREMENT PRIMARY KEY,
    `username` VARCHAR(255) NOT NULL,
    `email` VARCHAR(255) NOT NULL,
    `password` VARCHAR(255) NOT NULL,
    `roles` VARCHAR(255) NOT NULL
);

CREATE TABLE `user_friend` (
    `id` INT AUTO_INCREMENT PRIMARY KEY,
    `user_Id` INT NOT NULL,
    `friend_Id` INT NOT NULL,
    FOREIGN KEY (`user_Id`) REFERENCES `user`(`id`) ON DELETE CASCADE,
    FOREIGN KEY (`friend_Id`) REFERENCES `user`(`id`) ON DELETE CASCADE,
    CONSTRAINT `unique_user_friend` UNIQUE (`user_Id`, `friend_Id`)
);

CREATE TABLE `channel` (
    `id` INT AUTO_INCREMENT PRIMARY KEY,
    `name` VARCHAR(255) NOT NULL,
    `owner_Id` INT NOT NULL,
    `is_Deleted` BOOLEAN NOT NULL DEFAULT FALSE,
    FOREIGN KEY (`owner_Id`) REFERENCES `user`(`id`) ON DELETE CASCADE
);

CREATE TABLE `channel_user` (
    `id` INT AUTO_INCREMENT PRIMARY KEY,
    `channel_Id` INT NOT NULL,
    `user_Id` INT NOT NULL,
    `role` VARCHAR(50) NOT NULL,
    `is_Channel_Deleted` BOOLEAN NOT NULL DEFAULT FALSE,
    `is_User_Removed` BOOLEAN NOT NULL DEFAULT FALSE,
    FOREIGN KEY (`channel_Id`) REFERENCES `channel`(`id`) ON DELETE CASCADE,
    FOREIGN KEY (`user_Id`) REFERENCES `user`(`id`) ON DELETE CASCADE
);

CREATE TABLE `message` (
    `id` INT AUTO_INCREMENT PRIMARY KEY,
    `sender_Id` INT NOT NULL,
    `receiver_Id` INT DEFAULT NULL,
    `channel_Id` INT DEFAULT NULL,
    `message_Text` TEXT NOT NULL,
    `created_At` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (`sender_Id`) REFERENCES `user`(`id`) ON DELETE CASCADE
);

-- Step 3: Reset auto-increment values for primary keys
ALTER TABLE user AUTO_INCREMENT = 1;
ALTER TABLE user_friend AUTO_INCREMENT = 1;
ALTER TABLE channel AUTO_INCREMENT = 1;
ALTER TABLE channel_user AUTO_INCREMENT = 1;
ALTER TABLE message AUTO_INCREMENT = 1;

INSERT INTO user (username, email, password, roles) VALUES
  ('Gabby', 'gabby@example.com', '$2a$10$DMwC8.enNWEq4DmIZ7RtCO8G87GTV0PY195G8QIqZwulA7RlyUMte', 'USER'),
  ('gosho', 'gosho@example.com', '$2a$10$DMwC8.enNWEq4DmIZ7RtCO8G87GTV0PY195G8QIqZwulA7RlyUMte', 'USER'),
  ('tosho', 'tosho@example.com', '$2a$10$DMwC8.enNWEq4DmIZ7RtCO8G87GTV0PY195G8QIqZwulA7RlyUMte', 'USER'),
  ('pesho', 'pesho@example.com', '$2a$10$DMwC8.enNWEq4DmIZ7RtCO8G87GTV0PY195G8QIqZwulA7RlyUMte', 'USER'),
  ('penka', 'penka@example.com', '$2a$10$DMwC8.enNWEq4DmIZ7RtCO8G87GTV0PY195G8QIqZwulA7RlyUMte', 'USER'),
  ('radka', 'radka@example.com', '$2a$10$DMwC8.enNWEq4DmIZ7RtCO8G87GTV0PY195G8QIqZwulA7RlyUMte', 'USER'),
  ('goshka', 'goshka@example.com', '$2a$10$DMwC8.enNWEq4DmIZ7RtCO8G87GTV0PY195G8QIqZwulA7RlyUMte', 'USER'),
  ('mariyka', 'mariyka@example.com', '$2a$10$DMwC8.enNWEq4DmIZ7RtCO8G87GTV0PY195G8QIqZwulA7RlyUMte', 'USER'),
  ('pencho', 'pencho@example.com', '$2a$10$DMwC8.enNWEq4DmIZ7RtCO8G87GTV0PY195G8QIqZwulA7RlyUMte', 'USER'),
  ('tinka', 'tinka@example.com', '$2a$10$DMwC8.enNWEq4DmIZ7RtCO8G87GTV0PY195G8QIqZwulA7RlyUMte', 'USER');

  INSERT INTO user_friend (user_Id, friend_Id) VALUES
    (1, 2), -- Gabby and Gosho
    (1, 3), -- Gabby and Tosho
    (2, 4), -- Gosho and Pesho
    (3, 5), -- Tosho and Penka
    (4, 6); -- Pesho and Radka

  INSERT INTO channel (name, owner_Id) VALUES
    ('Chill Zone', 1), -- Gabby owns Chill Zone
    ('Gamers', 2),     -- Gosho owns Gamers
    ('Study Group', 3); -- Tosho owns Study Group

  INSERT INTO channel_user (channel_Id, user_Id, role) VALUES
    (1, 1, 'OWNER'), -- Gabby in Chill Zone
    (1, 2, 'MEMBER'), -- Gosho in Chill Zone
    (1, 3, 'MEMBER'), -- Tosho in Chill Zone
    (2, 2, 'OWNER'), -- Gosho in Gamers
    (2, 4, 'MEMBER'), -- Pesho in Gamers
    (3, 3, 'OWNER'), -- Tosho in Study Group
    (3, 5, 'MEMBER'), -- Penka in Study Group
    (3, 6, 'MEMBER'); -- Radka in Study Group

-- Direct messages between friends
INSERT INTO message (sender_Id, receiver_Id, channel_Id, message_Text, created_At) VALUES
    (1, 2, 0, 'Ko staa brat?', '2014-06-15 10:00:00'), -- Gabby to Gosho
    (2, 1, 0, 'Vsichko e tochno! Pri teb?', '2014-06-15 10:05:00'), -- Gosho to Gabby
    (1, 2, 0, 'Rabota kato rabota, no da izlezem vecherта?', '2014-06-15 10:10:00'), -- Gabby to Gosho
    (2, 1, 0, 'Stava, kade predlagash?', '2014-06-15 10:15:00'), -- Gosho to Gabby
    (1, 2, 0, 'V obichainoto kafe na centara.', '2014-06-15 10:20:00'), -- Gabby to Gosho

    (3, 1, 0, 'Hey Gabby, kvo 6te robi6 dove4era?', '2014-06-15 10:30:00'), -- Tosho to Gabby
    (1, 3, 0, 'Ще гледам каквото дават по тв, ti?', '2014-06-15 10:35:00'), -- Gabby to Tosho
    (3, 1, 0, 'Az shte pisha kodove za edna zadacha...', '2014-06-15 10:40:00'), -- Tosho to Gabby
    (1, 3, 0, 'A, dobre, ako imash vuprosi, zvuni!', '2014-06-15 10:45:00'), -- Gabby to Tosho

    (4, 2, 0, 'Koga igraem CS?', '2014-06-15 10:50:00'), -- Pesho to Gosho
    (2, 4, 0, 'Vecher v 9 chasa!', '2014-06-15 10:55:00'), -- Gosho to Pesho
    (4, 2, 0, 'Taka li? Da si prigotvya tima!', '2014-06-15 11:00:00'), -- Pesho to Gosho
    (2, 4, 0, 'Haha, tozi put shte te pobedya!', '2014-06-15 11:05:00'), -- Gosho to Pesho

    (5, 3, 0, 'Tosho, tazi zadacha me razbi', '2014-06-15 11:10:00'), -- Penka to Tosho
    (3, 5, 0, 'Ne se trevoi, shte ti obyasnya!', '2014-06-15 11:15:00'), -- Tosho to Penka
    (5, 3, 0, 'Super, ama malko me strah che shte zakusneya.', '2014-06-15 11:20:00'), -- Penka to Tosho
    (3, 5, 0, 'Spokoino, vreme ima, zaedno shte uspesh.', '2014-06-15 11:25:00'); -- Tosho to Penka


INSERT INTO message (sender_Id, receiver_Id, channel_Id, message_Text, created_At) VALUES
    (1, 0, 1, 'Welcome to the Chill Zone!', '2014-06-15 10:00:00'), -- Gabby in Chill Zone
    (2, 0, 1, 'Blagodarya, Gabby!', '2014-06-15 10:05:00'), -- Gosho in Chill Zone
    (3, 0, 1, 'Shte organizirame neshto zabavno tuk?', '2014-06-15 10:10:00'), -- Tosho in Chill Zone
    (1, 0, 1, 'Da, ideyata e vseki da predlozhi tema.', '2014-06-15 10:15:00'), -- Gabby in Chill Zone
    (2, 0, 1, 'Ok, az sam za!', '2014-06-15 10:20:00'), -- Gosho in Chill Zone

    (3, 0, 2, 'Study session utree li e?', '2014-06-15 10:30:00'), -- Tosho in Study Group
    (6, 0, 2, 'Da, shte zapochnem sutrin ot 10 chasa.', '2014-06-15 10:35:00'), -- Radka in Study Group
    (5, 0, 2, 'Moga li da zakusneya s 15 minuti?', '2014-06-15 10:40:00'), -- Penka in Study Group
    (3, 0, 2, 'Da, no probvai da si tam na vreme.', '2014-06-15 10:45:00'), -- Tosho in Study Group
    (6, 0, 2, 'Shte ti pratya materialite, ako ne uspeesh.', '2014-06-15 10:50:00'), -- Radka in Study Group
    (5, 0, 2, 'Blagodarya, Radka!', '2014-06-15 10:55:00'), -- Penka in Study Group
    (6, 0, 2, 'Nema problem!', '2014-06-15 11:00:00'); -- Radka in Study Group



