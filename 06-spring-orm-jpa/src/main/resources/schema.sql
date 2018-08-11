-- tables
DROP TABLE IF EXISTS book;
DROP SEQUENCE IF EXISTS book_id_seq;
CREATE SEQUENCE IF NOT EXISTS book_id_seq;
CREATE TABLE IF NOT EXISTS book
(
  id BIGINT DEFAULT book_id_seq.nextval PRIMARY KEY,
  title VARCHAR(100)
);

DROP TABLE IF EXISTS author;
DROP SEQUENCE IF EXISTS author_id_seq;
CREATE SEQUENCE IF NOT EXISTS author_id_seq;
CREATE TABLE IF NOT EXISTS author
(
  id BIGINT DEFAULT author_id_seq.nextval PRIMARY KEY,
  fullname VARCHAR(100)
);

DROP TABLE IF EXISTS genre;
DROP SEQUENCE IF EXISTS genre_id_seq;
CREATE SEQUENCE IF NOT EXISTS genre_id_seq;
CREATE TABLE IF NOT EXISTS genre
(
  id BIGINT DEFAULT genre_id_seq.nextval PRIMARY KEY,
  name VARCHAR(100),
);

-- relationships
DROP TABLE IF EXISTS book_author;
CREATE TABLE IF NOT EXISTS book_author
(
  book_id BIGINT NOT NULL,
  author_id BIGINT NOT NULL,
  FOREIGN KEY (book_id) REFERENCES book(id),
  FOREIGN KEY (author_id) REFERENCES author(id),
);

DROP TABLE IF EXISTS book_genre;
CREATE TABLE IF NOT EXISTS book_genre
(
  book_id BIGINT NOT NULL,
  genre_id BIGINT NOT NULL,
  FOREIGN KEY (book_id) REFERENCES book(id),
  FOREIGN KEY (genre_id) REFERENCES genre(id),
);
