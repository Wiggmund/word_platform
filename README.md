# Word Platform
This project provides REST API for Word Platform web application. The application aims to help learn new English words using flash cards method.

## Bootstrapping
To run application you must have:
1. PostgreSQL 15 or above and created database with name: wordplatform

Notes:
1. There is predefined HTTP requests in [Postman](https://www.postman.com/aviation-candidate-31353409/workspace/global/collection/22821354-66ce7213-a1bc-4bcf-b14b-dd3bf799b5f0?action=share&creator=22821354) for convenience
2. The sensitive information in application properties was not externalized for convenience during development. However, in the final development stage, it will be externalized and modified.

## Implemented Functionality
The following functionality has been implemented:

1. CRUD operations for entities
2. Unit tests: All repositories and one service have been tested
3. Logging
4. JWT authentication: The system employs JWT (JSON Web Token) authentication for user authentication and authorization. It utilizes both refresh and access tokens to enhance security
5. Exception handling

## Concept
### Word list
A word list is a collection of words **(with the same attributes)** used for learning purposes. Each word list is associated with a user (owner) and can contain one or more words.
### Word
A word is an English word included in some word list. Each word has at least one attribute associated with it. These attributes provide information about the word, such as its translation, usage examples, type (verb, noun, etc.), transcription, or any other relevant details. When a user adds a new word to a list, they have the flexibility to provide any attribute they desire.

**Here is the most basic example of word object:**

	id: 1,
	value: "Hello",
	attributes: [
		translation: "Привет",
		context: "Hello world"
	]

### Learning process
When a user chooses a word list or creates a custom one, they need to create questions for that word list in order to start learning. A question serves as a way to instruct the web application on what it should ask the user. Questions are created based on word attributes. Referring to our basic example, we can create two questions:

1.  Ask about the translation.
2.  Ask about the context.

There are two types of questions: **checked** and **unchecked**. Checked questions are validated by the client program through simple spell checking, nothing special. Unchecked questions require the user to validate them on their own. The UI should provide two buttons for a correct and wrong answer, and the user determines correctness. Following this approach, we can define the following questions:

1.  Translation - a checked question, allowing the user to type an answer while the client program checks for string equality.
2.  Context - an unchecked question, prompting the user to provide examples of word usage, and allowing the user to decide whether their answer is right or wrong.

After a learning session, the user can access statistics regarding their success. A statistical record includes the date, question, word, and a mark indicating correctness (true or false). This way, the user can review the words and questions they were asked and their accuracy.
