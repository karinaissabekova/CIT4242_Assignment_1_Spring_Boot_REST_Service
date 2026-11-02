# CIT4242 Assignment 1 — Spring Boot REST Service

## Project Overview

This project is a RESTful API for an online store product catalogue, developed as part of CIT4242 Advanced Programming at KIMEP University.

The application allows users to create, retrieve, update, delete, search, and filter products.

## Technologies Used

- Java 17
- Spring Boot 3.3.5
- Maven
- Spring Web
- Spring Data JPA
- PostgreSQL
- Bean Validation

## Project Structure

```text
com.karina.onlinestore/
├── controller/
│   └── ProductController.java
├── service/
│   └── ProductService.java
├── repository/
│   └── ProductRepository.java
├── entity/
│   └── Product.java
├── dto/
│   ├── CreateProductRequest.java
│   ├── UpdateProductRequest.java
│   └── ProductResponse.java
├── exception/
│   ├── ApiError.java
│   ├── ProductNotFoundException.java
│   └── GlobalExceptionHandler.java
└── OnlineStoreApplication.java
```

## Database Configuration

The application uses PostgreSQL.

Create a database named:

```sql
CREATE DATABASE online_store;
```

Default configuration:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/online_store
spring.datasource.username=postgres
spring.datasource.password=postgres
```

You can override these settings using environment variables:

- `DB_URL`
- `DB_USERNAME`
- `DB_PASSWORD`

Hibernate automatically creates and updates the database table using the configured JPA settings.

## How to Run the Application

### Prerequisites

- Java 17 or higher
- Maven
- PostgreSQL

### Steps

1. Clone the repository.
2. Create the PostgreSQL database.
3. Configure the database credentials in `application.properties` or environment variables.
4. Open a terminal in the project directory.
5. Run:

```bash
mvn clean package
```

6. Start the application:

```bash
mvn spring-boot:run
```

The API will be available at:

`http://localhost:8080`

## REST API Endpoints

| Method | Endpoint | Description | Status |
|---|---|---|---|
| GET | `/api/products` | Retrieve all products | 200 |
| GET | `/api/products/{id}` | Retrieve a product by ID | 200 / 404 |
| POST | `/api/products` | Create a product | 201 |
| PUT | `/api/products/{id}` | Update a product | 200 / 404 |
| DELETE | `/api/products/{id}` | Delete a product | 204 / 404 |
| GET | `/api/products/category/{category}` | Filter products by category | 200 |
| GET | `/api/products/search?name=...` | Search products by name | 200 |

## Example Requests

### Create a Product

**POST** `/api/products`

Request:

```json
{
  "name": "Wireless Mouse",
  "description": "Bluetooth wireless mouse",
  "price": 29.99,
  "category": "Electronics",
  "inStock": true
}
```

Expected response: `201 Created`

The response includes the created product and a `Location` header.

### Retrieve All Products

**GET** `/api/products`

Expected response: `200 OK`

### Retrieve a Product by ID

**GET** `/api/products/1`

Expected response: `200 OK` or `404 Not Found`

### Update a Product

**PUT** `/api/products/1`

Request:

```json
{
  "name": "Updated Mouse",
  "description": "Updated description",
  "price": 34.50,
  "category": "Electronics",
  "inStock": true
}
```

Expected response: `200 OK`

### Delete a Product

**DELETE** `/api/products/1`

Expected response: `204 No Content` with an empty body.

### Filter by Category

**GET** `/api/products/category/Electronics`

Expected response: `200 OK`

### Search by Name

**GET** `/api/products/search?name=mouse`

Expected response: `200 OK`

If no products match, the API returns an empty array.

## Validation

The application validates incoming requests:

- Product name is required and must contain 2–100 characters.
- Description is optional and limited to 1000 characters.
- Price is required and must be positive.
- Category is required.
- `inStock` defaults to `true` if omitted.

Invalid requests return `400 Bad Request`.

## Error Handling

The application uses a global `@RestControllerAdvice
