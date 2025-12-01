# Cash Card API

The Cash Card API is a simple RESTful web service for managing virtual cash cards. It allows users to view, create, update, and delete their cash cards. The API is built with Java and Spring Boot.

## Project Structure

The project follows a standard Maven project structure:

```
.
├── .gitattributes
├── .gitignore
├── mvnw
├── mvnw.cmd
├── pom.xml
├── README.md
├── .git
├── .idea
├── .mvn
    └── wrapper
        └── maven-wrapper.properties
├── src
    ├── main
    │   ├── java
    │   │   └── example
    │   │       └── cashcard
    │   │           ├── CashCard.java
    │   │           ├── CashCardApplication.java
    │   │           ├── CashCardController.java
    │   │           ├── CashCardRepository.java
    │   │           └── SecurityConfig.java
    │   └── resources
    │       ├── application.properties
    │       ├── static
    │       └── templates
    └── test
        ├── java
        │   └── example
        │       └── cashcard
        │           ├── CashCardApplicationTests.java
        │           └── CashCardJsonTest.java
        └── resources
            ├── data.sql
            └── example
                └── cashcard
                    ├── array.json
                    └── single.json
```

-   `src/main/java`: Contains the main application source code.
-   `src/main/resources`: Contains application resources like `application.properties`.
-   `src/test/java`: Contains the test source code.
-   `src/test/resources`: Contains test resources like `data.sql` for seeding test data.
-   `pom.xml`: The Project Object Model (POM) file for Maven, which defines the project's dependencies and build configuration.

## Classes

The main application code is located in the `example.cashcard` package:

-   `CashCard.java`: This is the entity that represents a cash card. It has an `id`, `amount`, and `owner`.
-   `CashCardApplication.java`: The main Spring Boot application class that bootstraps the application.
-   `CashCardController.java`: This is the REST controller that handles all incoming HTTP requests for cash card operations.
-   `CashCardRepository.java`: This is the repository interface that provides data access methods for `CashCard` entities. It extends Spring Data's `CrudRepository` and `PagingAndSortingRepository`.
-   `SecurityConfig.java`: This class configures the security for the application, including authentication and authorization.

## API Endpoints

The following endpoints are available:

| Method   | Path                  | Description                                                                          |
| :------- | :-------------------- | :----------------------------------------------------------------------------------- |
| `GET`    | `/cashcards/{id}`     | Returns a single cash card by its ID.                                                |
| `GET`    | `/cashcards`          | Returns a paginated and sorted list of all cash cards for the authenticated user.      |
| `POST`   | `/cashcards`          | Creates a new cash card. The owner is set to the authenticated user.                 |
| `PUT`    | `/cashcards/{id}`     | Updates an existing cash card.                                                       |
| `DELETE` | `/cashcards/{id}`     | Deletes an existing cash card.                                                       |

## Security

The API is secured using Spring Security. All endpoints under `/cashcards/**` require authentication. The security is configured in `SecurityConfig.java` to use HTTP Basic Authentication. For testing purposes, an in-memory user store is used with a few sample users.

## Testing

This project follows a Test-Driven Development (TDD) approach. The tests are written before the application code. This ensures that the application behaves as expected and helps to prevent regressions.

There are two main types of tests in this project:

-   **Component Tests (`CashCardJsonTest.java`):** These tests focus on a single component, in this case, the JSON serialization and deserialization of the `CashCard` object. The `@JsonTest` annotation is used to create a test context that is focused only on JSON testing. These tests ensure that the `CashCard` object can be correctly converted to and from JSON.

-   **Integration Tests (`CashCardApplicationTests.java`):** These tests focus on the integration of different components of the application. The `@SpringBootTest` annotation is used to create a full application context, including the web server. These tests make actual HTTP requests to the running application and verify the responses. This ensures that the different layers of the application (controller, repository, security) work correctly together.

The test suite covers the following scenarios:

-   Retrieving a single cash card.
-   Handling requests for non-existent cash cards.
-   Enforcing security with authentication and authorization.
-   Paginating and sorting the list of cash cards.
-   Creating, updating, and deleting cash cards.
-   Preventing users from accessing or modifying cash cards they do not own.

The tests use `TestRestTemplate` to make HTTP requests and `JsonPath` to parse and assert the JSON responses. The `@DirtiesContext` annotation is used to reset the application context after tests that modify the data, ensuring that the tests are independent of each other.
