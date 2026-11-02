# CIT4242 Assignment 1 — Spring Boot REST Service

A REST API for an online store product catalogue, built with Spring Boot 3, Maven, Spring Data JPA, Bean Validation, and PostgreSQL.

## Requirements

- Java 17+
- Maven 3.8+
- PostgreSQL 14+ (or compatible)
- Git (for submission)

## Database setup

Create a PostgreSQL database:

```sql
CREATE DATABASE online_store;
```

The default local configuration is:
- URL: `jdbc:postgresql://localhost:5432/online_store`
- Username: `postgres`
- Password: `postgres`

For a different setup, set environment variables before running:

```bash
export DB_URL=jdbc:postgresql://localhost:5432/online_store
export DB_USERNAME=your_username
export DB_PASSWORD=your_password
```

Windows PowerShell:

```powershell
$env:DB_URL="jdbc:postgresql://localhost:5432/online_store"
$env:DB_USERNAME="your_username"
$env:DB_PASSWORD="your_password"
```

Hibernate creates/updates the `products` table using `spring.jpa.hibernate.ddl-auto=update`.

## Run

From the project root:

```bash
mvn clean package
mvn spring-boot:run
```

The API runs at `http://localhost:8080`.

## Product JSON

Example create/update request:

```json
{
  "name": "Wireless Mouse",
  "description": "Bluetooth wireless mouse",
  "price": 29.99,
  "category": "Electronics",
  "inStock": true
}
```

`inStock` is optional and defaults to `true`. The client must not send `id`, `createdAt`, or `updatedAt`; these are server-managed.

## Endpoints

| Method | Endpoint | Description | Success |
|---|---|---|---|
| GET | `/api/products` | List all products | 200 |
| GET | `/api/products/{id}` | Get product by ID | 200 |
| POST | `/api/products` | Create product | 201 + Location |
| PUT | `/api/products/{id}` | Replace product | 200 |
| DELETE | `/api/products/{id}` | Delete product | 204, empty body |
| GET | `/api/products/category/{category}` | Filter by category (case-insensitive) | 200 |
| GET | `/api/products/search?name=mouse` | Search name (case-insensitive substring) | 200 |

No search matches returns `200 OK` with `[]`.

## curl examples

Create (expect 201 and `Location: /api/products/{id}`):

```bash
curl -i -X POST http://localhost:8080/api/products \
  -H "Content-Type: application/json" \
  -d '{"name":"Wireless Mouse","description":"Bluetooth mouse","price":29.99,"category":"Electronics"}'
```

List:

```bash
curl -i http://localhost:8080/api/products
```

Get by ID:

```bash
curl -i http://localhost:8080/api/products/1
```

Update (PUT replaces editable fields; omitted `inStock` defaults to true):

```bash
curl -i -X PUT http://localhost:8080/api/products/1 \
  -H "Content-Type: application/json" \
  -d '{"name":"Updated Mouse","description":"Updated description","price":34.50,"category":"Electronics","inStock":true}'
```

Filter by category:

```bash
curl -i http://localhost:8080/api/products/category/Electronics
```

Search by name:

```bash
curl -i "http://localhost:8080/api/products/search?name=mouse"
```

Delete (expect 204 and no body):

```bash
curl -i -X DELETE http://localhost:8080/api/products/1
```

Missing ID (expect 404):

```bash
curl -i http://localhost:8080/api/products/9999
```

Invalid request (expect 400 with field errors):

```bash
curl -i -X POST http://localhost:8080/api/products \
  -H "Content-Type: application/json" \
  -d '{"name":"","price":-10,"category":""}'
```

## Error response format

All handled errors use the same four fields:

```json
{
  "status": 400,
  "message": "Validation failed",
  "timestamp": "2026-10-27T10:30:00",
  "errors": {
    "name": "must not be blank",
    "price": "must be greater than 0",
    "category": "must not be blank"
  }
}
```

For 404, `errors` is `{}`. Error responses do not expose stack traces or SQL details.

## Validation rules

- `name`: required, 2–100 characters
- `description`: optional, maximum 1000 characters
- `price`: required and positive
- `category`: required
- `inStock`: optional, defaults to true

## Architecture

- **Controller**: HTTP routing and response status codes only.
- **Service**: application logic and entity/DTO mapping.
- **Repository**: Spring Data JPA persistence.
- **Entity**: database representation of Product.
- **DTOs**: separate request and response contracts.
- **GlobalExceptionHandler**: consistent 400/404 error bodies.

## Assumptions

- Category filtering and name search are case-insensitive.
- Name search is a substring search.
- `PUT` replaces all editable fields; omitted optional `inStock` defaults to `true`.
- Product IDs and timestamps are generated/managed by the server.
- The database schema is managed by Hibernate for this coursework setup.

## Final verification checklist

- [ ] Java 17 and PostgreSQL are available.
- [ ] Database `online_store` exists and credentials are correct.
- [ ] `mvn clean package` succeeds.
- [ ] Application starts and logs `Started OnlineStoreApplication`.
- [ ] All seven endpoints tested.
- [ ] POST returns 201 with Location.
- [ ] DELETE returns 204 with no body.
- [ ] Invalid payload returns 400 and field errors.
- [ ] Missing product returns 404 with the standard error shape.
- [ ] README commands work from a clean clone.
