# City Manager - Backend Component

Welcome to the backend component of the City Manager application, a ticket management system designed to streamline city
improvement initiatives. This repository contains the server-side logic and APIs to support the frontend application.

**Note:** This is an MVP version of the application, developed as a pet project. It serves as a
foundation for future enhancements and features.

## Table of Contents

- [Features](#features)
- [Technologies](#technologies)
- [API Endpoints](#api-endpoints)
- [Running locally](#running-locally)

## Features

- **User Authentication**: Secure access for users via token-based authentication, allowing users to register, log in,
  and manage their sessions securely.
- **Ticket Management**: Create and view tickets for city improvement projects.
- **Voting**: Users can vote on tickets to express their support for city improvement initiatives, helping prioritize
  which projects should be addressed first.

## Technologies

This backend is built using:

- Java
- Spring Boot
- Spring Web
- Spring Data JPA
- PostgreSQL
- JWT (JSON Web Tokens)
- Swagger

## API Endpoints

The following are the primary API endpoints for the City Manager backend:

### Authentication

- **POST** `/api/auth/register` - Register a new user
- **POST** `/api/auth/login` - Login an existing user
- **POST** `/api/auth/refresh` - Refresh an access token by existing refresh token

### Tickets

- **POST** `/api/ticket/new` - Create a new ticket
- **GET** `/api/ticket` - Retrieve all tickets in a paged manner
- **GET** `/api/ticket/:id` - Retrieve a specific ticket by ID

### Voting

- **POST** `/api/ticket/:id/vote` - Vote for a specific ticket by ID
- **POST** `/api/ticket/:id/unvote` - Remote a vote for a specific ticket by ID

## Running locally

### Prerequisites

Ensure you have the following installed on your machine before running the application:

- Java (version 17 or higher)
- Maven
- PostgreSQL (should be started when you run the application)

### Clone the repository

Run this command to clone the repository:

```shell
git clone https://github.com/blabla/city-manager-backend.git
```

### Prepare the database

Execute the next SQL scripts in order to set up the database with necessary schema and tables. This is needed only
before the first run.

- [create_database.sql](src/main/resources/sql/create_database.sql)
- [create_schema_user_data.sql](src/main/resources/sql/create_schema_user_data.sql)
- [create_schema_request_data.sql](src/main/resources/sql/create_schema_request_data.sql)

### Use local profile

Set the profile to `local` in [application.properties](src/main/resources/application.properties)

```properties
spring.profiles.active=local
```

### Run the application

You can start the application in one of two ways. The API will be exposed on port 8080

#### Run in your IDE

Open the project in your preferred IDE and click the run button.

#### Run via maven

You can also run the application from the command line with Maven:

```properties
mvn spring-boot:run
```
