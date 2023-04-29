--ATTRIBUTES
INSERT INTO attributes (name, type) VALUES ('translation', 'base');
INSERT INTO attributes (name, type) VALUES ('context', 'custom');

--USERS
INSERT INTO users (username, email) VALUES ('Sem', 'semwork@gmail.com');

--WORDLIST
INSERT INTO wordlists (title, description, user_id) VALUES ('Top 100 verbs', 'The most popular verbs', 1);
INSERT INTO wordlists_attributes (attribute_id, wordlist_id) VALUES (1, 1);
INSERT INTO wordlists_attributes (attribute_id, wordlist_id) VALUES (2, 1);

--WORDS
INSERT INTO words (definition, user_id, wordlist_id) VALUES ('weekend', 1, 1);
INSERT INTO words_attributes (word_id, attribute_id, text) VALUES (1, 1, 'free time');
INSERT INTO words_attributes (word_id, attribute_id, text) VALUES (1, 2, 'I am enjoying my weekends');

--QUESTIONS
INSERT INTO questions (text, type, attribute_id, wordlist_id, user_id) VALUES ('Translate it', 'checked', 1, 1, 1);

--STATS
INSERT INTO stats (testing_date, correct, user_id, wordlist_id, word_id, question_id) VALUES ('2023-04-29', TRUE, 1, 1, 1, 1);