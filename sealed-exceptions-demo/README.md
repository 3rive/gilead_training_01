# Sealed exceptions in a Spring Boot API

A small order API that throws a **sealed** exception hierarchy and maps every subtype in one `@RestControllerAdvice`.

## Why seal the exceptions

An open `RuntimeException` hierarchy lets any class extend the base type. A global handler then needs a catch-all, and a new failure can slip through as HTTP 500.

`OrderException` is sealed, so only these types may extend it:

| Exception | When it is thrown | HTTP status |
| --- | --- | --- |
| `OrderNotFoundException` | `GET /api/orders/{id}` and the id is unknown | 404 |
| `InsufficientStockException` | requested quantity is above the in-memory stock | 409 |
| `InvalidOrderRequestException` | quantity is not positive, or the SKU is unknown | 400 |

`GlobalExceptionHandler` switches on the sealed type:

```java
return switch (exception) {
    case OrderNotFoundException notFound -> /* 404 */;
    case InsufficientStockException stock -> /* 409 */;
    case InvalidOrderRequestException invalid -> /* 400 */;
};
```

The switch has no `default`. If you add another subtype to `permits` and forget a branch, `javac` rejects the handler. That is the practical use of a sealed class here: the HTTP mapping stays complete.

## Run

```bash
cd sealed-exceptions-demo
mvn spring-boot:run
```

```bash
curl -s -X POST localhost:8080/api/orders \
  -H 'Content-Type: application/json' \
  -d '{"sku":"BOOK-1","quantity":2}'

curl -s localhost:8080/api/orders/missing

curl -s -X POST localhost:8080/api/orders \
  -H 'Content-Type: application/json' \
  -d '{"sku":"PEN-2","quantity":4}'
```

Stock starts at `BOOK-1` = 5 and `PEN-2` = 1, held in memory for the life of the process.

```bash
mvn test
```
