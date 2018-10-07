-- demo data

INSERT INTO book (title, isbn)
VALUES ('book 1', '0-395-08254-1'),
       ('book 2', '0-395-08254-2'),
       ('book 3', '0-395-08254-3'),
       ('book 4', '0-395-08254-4');

INSERT INTO author (fullname)
VALUES ('author 1'),
       ('author 2'),
       ('author 3'),
       ('author 4');

INSERT INTO genre (name)
VALUES ('genre 1'),
       ('genre 2'),
       ('genre 3');


-- demo references

INSERT INTO book_author (book_id, author_id)
VALUES (1, 1),
       (1, 2),
       (1, 3),
       (1, 4),
       (2, 1),
       (2, 3),
--        (3, 3), for null
       (4, 4);

INSERT INTO book_genre (book_id, genre_id)
VALUES (1, 1),
       (1, 2),
       (2, 2),
--        (3, 3), for null
       (4, 1);
