-- tables
CREATE TABLE IF NOT EXISTS book
(
  id INT AUTO_INCREMENT PRIMARY KEY,
  title VARCHAR(100)
);

CREATE TABLE IF NOT EXISTS author
(
  id INT AUTO_INCREMENT PRIMARY KEY,
  full_name VARCHAR(100)
);

CREATE TABLE IF NOT EXISTS genre
(
  id INT AUTO_INCREMENT PRIMARY KEY,
  title VARCHAR(100),
);


-- relationships
CREATE TABLE IF NOT EXISTS book_author
(
  book_id INT ,
  author_id INT,
  FOREIGN KEY (book_id) REFERENCES book(id),
  FOREIGN KEY (author_id) REFERENCES author(id),
);

CREATE TABLE IF NOT EXISTS book_genre
(
  book_id INT ,
  genre_id INT,
  FOREIGN KEY (book_id) REFERENCES book(id),
  FOREIGN KEY (genre_id) REFERENCES genre(id),
);
