CREATE TABLE attributes (
  id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
   name VARCHAR(255),
   type VARCHAR(255),
   CONSTRAINT pk_attributes PRIMARY KEY (id)
);

CREATE TABLE users (
  id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
   username VARCHAR(255),
   email VARCHAR(255),
   CONSTRAINT pk_users PRIMARY KEY (id)
);

CREATE TABLE wordlists (
  id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
   title VARCHAR(255),
   description VARCHAR(255),
   user_id BIGINT,
   CONSTRAINT pk_wordlists PRIMARY KEY (id)
);
ALTER TABLE wordlists ADD CONSTRAINT FK_WORDLISTS_ON_USER FOREIGN KEY (user_id) REFERENCES users (id);

CREATE TABLE wordlists_attributes (
  attribute_id BIGINT NOT NULL,
   wordlist_id BIGINT NOT NULL
);
ALTER TABLE wordlists_attributes ADD CONSTRAINT fk_woratt_on_attribute FOREIGN KEY (attribute_id) REFERENCES attributes (id);
ALTER TABLE wordlists_attributes ADD CONSTRAINT fk_woratt_on_wordlist FOREIGN KEY (wordlist_id) REFERENCES wordlists (id);

CREATE TABLE words (
  id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
   value VARCHAR(255),
   user_id BIGINT,
   wordlist_id BIGINT,
   CONSTRAINT pk_words PRIMARY KEY (id)
);
ALTER TABLE words ADD CONSTRAINT FK_WORDS_ON_USER FOREIGN KEY (user_id) REFERENCES users (id);
ALTER TABLE words ADD CONSTRAINT FK_WORDS_ON_WORDLIST FOREIGN KEY (wordlist_id) REFERENCES wordlists (id);

CREATE TABLE words_attributes (
  word_id BIGINT NOT NULL,
   attribute_id BIGINT NOT NULL,
   value VARCHAR(255),
   CONSTRAINT pk_words_attributes PRIMARY KEY (word_id, attribute_id)
);
ALTER TABLE words_attributes ADD CONSTRAINT FK_WORDS_ATTRIBUTES_ON_ATTRIBUTE FOREIGN KEY (attribute_id) REFERENCES attributes (id);
ALTER TABLE words_attributes ADD CONSTRAINT FK_WORDS_ATTRIBUTES_ON_WORD FOREIGN KEY (word_id) REFERENCES words (id);

CREATE TABLE questions (
  id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
   text VARCHAR(255),
   type VARCHAR(255),
   attribute_id BIGINT,
   wordlist_id BIGINT,
   user_id BIGINT,
   CONSTRAINT pk_questions PRIMARY KEY (id)
);
ALTER TABLE questions ADD CONSTRAINT FK_QUESTIONS_ON_ATTRIBUTE FOREIGN KEY (attribute_id) REFERENCES attributes (id);
ALTER TABLE questions ADD CONSTRAINT FK_QUESTIONS_ON_USER FOREIGN KEY (user_id) REFERENCES users (id);
ALTER TABLE questions ADD CONSTRAINT FK_QUESTIONS_ON_WORDLIST FOREIGN KEY (wordlist_id) REFERENCES wordlists (id);

CREATE TABLE stats (
  id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
   date date,
   correct BOOLEAN,
   user_id BIGINT,
   wordlist_id BIGINT,
   word_id BIGINT,
   question_id BIGINT,
   CONSTRAINT pk_stats PRIMARY KEY (id)
);
ALTER TABLE stats ADD CONSTRAINT FK_STATS_ON_QUESTION FOREIGN KEY (question_id) REFERENCES questions (id);
ALTER TABLE stats ADD CONSTRAINT FK_STATS_ON_USER FOREIGN KEY (user_id) REFERENCES users (id);
ALTER TABLE stats ADD CONSTRAINT FK_STATS_ON_WORD FOREIGN KEY (word_id) REFERENCES words (id);
ALTER TABLE stats ADD CONSTRAINT FK_STATS_ON_WORDLIST FOREIGN KEY (wordlist_id) REFERENCES wordlists (id);