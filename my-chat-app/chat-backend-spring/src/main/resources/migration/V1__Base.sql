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
  ('Gosho', 'gosho@example.com', '$2a$10$DMwC8.enNWEq4DmIZ7RtCO8G87GTV0PY195G8QIqZwulA7RlyUMte', 'USER'),
  ('Tosho', 'tosho@example.com', '$2a$10$DMwC8.enNWEq4DmIZ7RtCO8G87GTV0PY195G8QIqZwulA7RlyUMte', 'USER'),
  ('Pesho', 'pesho@example.com', '$2a$10$DMwC8.enNWEq4DmIZ7RtCO8G87GTV0PY195G8QIqZwulA7RlyUMte', 'USER'),
  ('Penka', 'penka@example.com', '$2a$10$DMwC8.enNWEq4DmIZ7RtCO8G87GTV0PY195G8QIqZwulA7RlyUMte', 'USER'),
  ('Radka', 'radka@example.com', '$2a$10$DMwC8.enNWEq4DmIZ7RtCO8G87GTV0PY195G8QIqZwulA7RlyUMte', 'USER'),
  ('Goshka', 'goshka@example.com', '$2a$10$DMwC8.enNWEq4DmIZ7RtCO8G87GTV0PY195G8QIqZwulA7RlyUMte', 'USER'),
  ('Mariyka', 'mariyka@example.com', '$2a$10$DMwC8.enNWEq4DmIZ7RtCO8G87GTV0PY195G8QIqZwulA7RlyUMte', 'USER'),
  ('Pencho1', 'pencho1@example.com', '$2a$10$DMwC8.enNWEq4DmIZ7RtCO8G87GTV0PY195G8QIqZwulA7RlyUMte', 'USER'),
  ('Pencho2', 'pencho2@example.com', '$2a$10$DMwC8.enNWEq4DmIZ7RtCO8G87GTV0PY195G8QIqZwulA7RlyUMte', 'USER'),
  ('Pencho3', 'pencho3@example.com', '$2a$10$DMwC8.enNWEq4DmIZ7RtCO8G87GTV0PY195G8QIqZwulA7RlyUMte', 'USER'),
  ('Pencho4', 'pencho4@example.com', '$2a$10$DMwC8.enNWEq4DmIZ7RtCO8G87GTV0PY195G8QIqZwulA7RlyUMte', 'USER'),
  ('Pencho5', 'pencho5@example.com', '$2a$10$DMwC8.enNWEq4DmIZ7RtCO8G87GTV0PY195G8QIqZwulA7RlyUMte', 'USER'),
  ('Pencho6', 'pencho6@example.com', '$2a$10$DMwC8.enNWEq4DmIZ7RtCO8G87GTV0PY195G8QIqZwulA7RlyUMte', 'USER'),
  ('Pencho7', 'pencho7@example.com', '$2a$10$DMwC8.enNWEq4DmIZ7RtCO8G87GTV0PY195G8QIqZwulA7RlyUMte', 'USER'),
  ('Pencho8', 'pencho8@example.com', '$2a$10$DMwC8.enNWEq4DmIZ7RtCO8G87GTV0PY195G8QIqZwulA7RlyUMte', 'USER'),
  ('Pencho9', 'pencho9@example.com', '$2a$10$DMwC8.enNWEq4DmIZ7RtCO8G87GTV0PY195G8QIqZwulA7RlyUMte', 'USER'),
  ('Pencho10', 'pencho10@example.com', '$2a$10$DMwC8.enNWEq4DmIZ7RtCO8G87GTV0PY195G8QIqZwulA7RlyUMte', 'USER'),
  ('Tinka', 'tinka@example.com', '$2a$10$DMwC8.enNWEq4DmIZ7RtCO8G87GTV0PY195G8QIqZwulA7RlyUMte', 'USER');

INSERT INTO user_friend (user_id, friend_id) VALUES
    (1, 2), -- Gabby and Gosho
    (1, 3), -- Gabby and Tosho
    (1, 4), -- Gabby and Pesho
    (1, 5), -- Gabby and Penka
    (1, 6), -- Gabby and Radka
    (1, 7), -- Gabby and Goshka
    (1, 8), -- Gabby and Mariyka
    (1, 9), -- Gabby and Pencho1
    (1, 10), -- Gabby and Pencho2
    (1, 11), -- Gabby and Pencho3
    (1, 12), -- Gabby and Pencho4
    (1, 13), -- Gabby and Pencho5
    (1, 14), -- Gabby and Pencho6
    (1, 15), -- Gabby and Pencho7
    (1, 16), -- Gabby and Pencho8
    (1, 17), -- Gabby and Pencho9
    (1, 18), -- Gabby and Pencho10
    (1, 19), -- Gabby and Tinka
    (2, 4), -- Gosho and Pesho
    (3, 5), -- Tosho and Penka
    (4, 6); -- Pesho and Radka

  INSERT INTO channel (name, owner_Id) VALUES
    ('Chill Zone', 1), -- Gabby owns Chill Zone
    ('Gamers', 2),     -- Gosho owns Gamers
    ('Study Group', 3), -- Tosho owns Study Group
    ('Music Lovers', 4), -- Pesho owns Music Lovers
    ('Fitness Freaks', 1), -- Penka owns Fitness Freaks
    ('Tech Talks', 1), -- Radka owns Tech Talks
    ('Foodies', 1), -- Goshka owns Foodies
    ('Movie Buffs', 1), -- Mariyka owns Movie Buffs
    ('Travel Enthusiasts', 1), -- Pencho1 owns Travel Enthusiasts
    ('Book Club', 1), -- Pencho2 owns Book Club
    ('DIY Crafts', 1), -- Pencho3 owns DIY Crafts
    ('Sports Fans', 1), -- Pencho4 owns Sports Fans
    ('Nature Lovers', 1), -- Pencho5 owns Nature Lovers
    ('Pet Owners', 14), -- Pencho6 owns Pet Owners
    ('Photography', 15), -- Pencho7 owns Photography
    ('Coding Club', 16); -- Pencho8 owns Coding Club

  INSERT INTO channel_user (channel_Id, user_Id, role) VALUES
     (2, 1, 'MEMBER'), -- Gabby in Gamers
     (3, 1, 'MEMBER'), -- Gabby in Study Group
     (4, 1, 'MEMBER'), -- Gabby in Music Lovers
     (5, 1, 'MEMBER'), -- Gabby in Fitness Freaks
     (6, 1, 'MEMBER'), -- Gabby in Tech Talks
     (7, 1, 'MEMBER'), -- Gabby in Foodies
     (8, 1, 'MEMBER'), -- Gabby in Movie Buffs
     (9, 1, 'MEMBER'), -- Gabby in Travel Enthusiasts
     (10, 1, 'MEMBER'), -- Gabby in Book Club
     (11, 1, 'MEMBER'), -- Gabby in DIY Crafts
     (12, 1, 'MEMBER'), -- Gabby in Sports Fans
     (13, 1, 'MEMBER'), -- Gabby in Nature Lovers
     (14, 1, 'MEMBER'), -- Gabby in Pet Owners
     (15, 1, 'MEMBER'), -- Gabby in Photography
     (16, 1, 'MEMBER'), -- Gabby in Coding Club
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
    (1, 2, 0, 'Mnogo rabota tezi dni, ama inache dobre.', '2014-06-15 10:06:00'), -- Gabby to Gosho
    (1, 2, 0, 'Ti kak si s proektite?', '2014-06-15 10:07:00'), -- Gabby to Gosho
    (2, 1, 0, 'Minalata sedmica be chaos, no sega e po-spokoino.', '2014-06-15 10:08:00'), -- Gosho to Gabby
    (2, 1, 0, 'Dnes imame kraen srok za edin modul.', '2014-06-15 10:09:00'), -- Gosho to Gabby
    (1, 2, 0, 'Dano vse minete gladko.', '2014-06-15 10:10:00'), -- Gabby to Gosho
    (1, 2, 0, 'Ako ti trjabva neshto, kazvai.', '2014-06-15 10:11:00'), -- Gabby to Gosho
    (2, 1, 0, 'Blagodarya, brat! Shte se opravim. A vecherта?', '2014-06-15 10:12:00'), -- Gosho to Gabby
    (1, 2, 0, 'Mislq da izlezem. Idvash li?', '2014-06-15 10:13:00'), -- Gabby to Gosho
    (2, 1, 0, 'Stava, no da ne e kafe na centara.', '2014-06-15 10:14:00'), -- Gosho to Gabby
    (2, 1, 0, 'Shtoto poslednija pyt tam be mnogo shumno.', '2014-06-15 10:15:00'), -- Gosho to Gabby
    (1, 2, 0, 'Da, prava si. Kak ti zvuchi novijat bar do parka?', '2014-06-15 10:16:00'), -- Gabby to Gosho
    (2, 1, 0, 'Super ideja! Kolko chasa?', '2014-06-15 10:17:00'), -- Gosho to Gabby
    (1, 2, 0, '8:00 vecherта.', '2014-06-15 10:18:00'), -- Gabby to Gosho
    (1, 2, 0, 'Taka shte imame vreme i za razhodka sled tova.', '2014-06-15 10:19:00'), -- Gabby to Gosho
    (2, 1, 0, 'Stava! Shte donesesh li neshto za pocherpka?', '2014-06-15 10:20:00'), -- Gosho to Gabby
    (1, 2, 0, 'Shte vzema chips i sokove. Ti?', '2014-06-15 10:21:00'), -- Gabby to Gosho
    (2, 1, 0, 'Shte donesa bira. Ako ne zabravq.', '2014-06-15 10:22:00'), -- Gosho to Gabby
    (1, 2, 0, 'Haha, ne zabravjai, brat! Do skoro.', '2014-06-15 10:23:00'), -- Gabby to Gosho
    (2, 1, 0, 'Do skoro!', '2014-06-15 10:24:00'), -- Gosho to Gabby
    (1, 2, 0, 'Zdrasti pak. Kafeto dnes e mnogo dobre, nova marka probvam.', '2014-06-15 15:00:00'), -- Gabby to Gosho
    (2, 1, 0, 'Kak se kazva? Trqbva da go opitam.', '2014-06-15 15:01:00'), -- Gosho to Gabby
    (1, 2, 0, 'Arabika Special Blend.', '2014-06-15 15:02:00'), -- Gabby to Gosho
    (1, 2, 0, 'Mnogo fin aromat ima.', '2014-06-15 15:03:00'), -- Gabby to Gosho
    (2, 1, 0, 'Zvezhi ideq. Shte pisha, kato probvam.', '2014-06-15 15:04:00'), -- Gosho to Gabby
    (2, 1, 0, 'A pri teb kak varvi proekta?', '2014-06-15 15:05:00'), -- Gosho to Gabby
    (1, 2, 0, 'Mnogo e slojen. Nov klient iska da promenim napalno designa.', '2014-06-15 15:06:00'), -- Gabby to Gosho
    (1, 2, 0, 'I se nalozhi da vzimam dopalnitelni konsultatsii.', '2014-06-15 15:07:00'), -- Gabby to Gosho
    (2, 1, 0, 'Zvuchi kato test za tvoite umeniya.', '2014-06-15 15:08:00'), -- Gosho to Gabby
    (1, 2, 0, 'Da, no se spravqm. Nali taka?', '2014-06-15 15:09:00'), -- Gabby to Gosho
    (2, 1, 0, 'Absolyutno! Tova e tvojat stil.', '2014-06-15 15:10:00'), -- Gosho to Gabby
    (1, 2, 0, 'Blagodarya! Mnogo mi pomaga takava podkrepa.', '2014-06-15 15:11:00'), -- Gabby to Gosho
    (2, 1, 0, 'Priqteli sme vse pak. Do vecher!', '2014-06-15 15:12:00'), -- Gosho to Gabby
    (1, 2, 0, 'Do vecher! :D', '2014-06-15 15:13:00'), -- Gabby to Gosho

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


