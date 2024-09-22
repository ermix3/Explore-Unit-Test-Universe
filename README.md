# Explore Unit Test Universe

**Explore Unit Test Universe** is a Spring Boot project that introduces developers to container technologies while
teaching essential unit testing practices. This project features a fully functional Spring Boot application, providing a
practical example for writing and executing unit tests with JUnit, using Testcontainers, Docker Compose, and Datafaker
to enhance the testing experience.

## Table of Contents

- [Features](#features)
- [Technologies](#technologies)
- [Installation](#installation)
- [Usage](#usage)
- [Running Tests](#running-tests)
- [Contributing](#contributing)
- [License](#license)

## Features

- Fully functional Spring Boot application
- Comprehensive unit tests using JUnit
- Integration with Testcontainers for container-based testing
- Docker Compose support for easy environment setup
- Datafaker for generating realistic test data

## Technologies

- Spring Boot ![Spring Boot](https://img.shields.io/badge/-Spring%20Boot-6DB33F?style=flat&logo=spring&logoColor=white)
- JUnit ![JUnit](https://img.shields.io/badge/-JUnit-25A162?style=flat&logo=junit5&logoColor=white)
- Testcontainers ![Testcontainers](https://img.shields.io/badge/-Testcontainers-FFA500?style=flat&logo=docker&logoColor=white)
- Docker ![Docker](https://img.shields.io/badge/-Docker-2496ED?style=flat&logo=docker&logoColor=white)
- Docker Compose ![Docker Compose](https://img.shields.io/badge/-Docker%20Compose-2496ED?style=flat&logo=docker&logoColor=white)
- Datafaker ![Datafaker](https://img.shields.io/badge/-Datafaker-FFA500?style=flat&logo=java&logoColor=white)
- Swagger ![Swagger](https://img.shields.io/badge/-Swagger-85EA2D?style=flat&logo=swagger&logoColor=white)

## Installation

1. Clone the repository:
   ```bash
   git clone https://github.com/ermix3/explore-unit-test-universe.git
   cd explore-unit-test-universe
    ```
2. Build the project:
3. ```bash
   mvn clean install
   ```
4. Run the application:
   ```bash
    mvn spring-boot:run
    ```
5. Access the application at swagger-ui:
   ```bash
   http://localhost:8080/api/v1/swagger-ui.html
   ```

## Usage

The application provides a RESTful API for managing users. You can use the Swagger UI to interact with the API
and perform CRUD operations on users.

## Running Tests

To run the unit tests, execute the following command:
```bash
mvn test
```

## Contributing

Contributions are welcome! Please refer to the [contributing guidelines](CONTRIBUTING.md) for detailed information.