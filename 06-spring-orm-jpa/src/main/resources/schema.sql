-- tables
CREATE TABLE IF NOT EXISTS book
(
  id    BIGINT AUTO_INCREMENT PRIMARY KEY,
  title VARCHAR(100)
);

CREATE TABLE IF NOT EXISTS author
(
  id       BIGINT AUTO_INCREMENT PRIMARY KEY,
  fullname VARCHAR(100)
);

CREATE TABLE IF NOT EXISTS genre
(
  id   BIGINT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(100),
);

-- relationships
CREATE TABLE IF NOT EXISTS book_author
(
  book_id   BIGINT NOT NULL,
  author_id BIGINT NOT NULL,
  FOREIGN KEY (book_id) REFERENCES book (id),
  FOREIGN KEY (author_id) REFERENCES author (id),
);

CREATE TABLE IF NOT EXISTS book_genre
(
  book_id  BIGINT NOT NULL,
  genre_id BIGINT NOT NULL,
  FOREIGN KEY (book_id) REFERENCES book (id),
  FOREIGN KEY (genre_id) REFERENCES genre (id),
);
