# FoodyBuddy Payments Service

Payment processing service for the FoodyBuddy microservices application built with Spring Boot.

## Features

- Process payments for orders
- Payment status tracking
- Refund functionality
- Payment history
- H2 in-memory database for development

## Prerequisites

- Java 17+
- Gradle 8.5+

## Getting Started

### Development

1. Run the application:
   ```bash
   ./gradlew bootRun
   ```

2. The service will be available at http://localhost:8082

3. H2 Console will be available at http://localhost:8082/h2-console
   - JDBC URL: jdbc:h2:mem:paymentsdb
   - Username: sa
   - Password: password

### Building

1. Build the application:
   ```bash
   ./gradlew build
   ```

2. Run the JAR file:
   ```bash
   java -jar build/libs/foodybuddy-payments-0.0.1-SNAPSHOT.jar
   ```

## API Endpoints

### Payments
- `POST /api/payments/process` - Process a payment
- `GET /api/payments/{paymentId}` - Get payment by ID
- `GET /api/payments/order/{orderId}` - Get payments by order ID
- `GET /api/payments` - Get all payments
- `POST /api/payments/{paymentId}/refund` - Refund a payment

### Health
- `GET /api/payments/health` - Service health check
- `GET /actuator/health` - Application health

## Payment Status Values

- PENDING
- PROCESSING
- COMPLETED
- FAILED
- REFUNDED
- CANCELLED

## Payment Method Values

- CREDIT_CARD
- DEBIT_CARD
- PAYPAL
- CASH
- BANK_TRANSFER

## Example API Usage

### Process Payment
```bash
curl -X POST http://localhost:8082/api/payments/process \
  -H "Content-Type: application/json" \
  -d '{
    "orderId": "order-123",
    "amount": 25.98,
    "method": "CREDIT_CARD",
    "cardNumber": "4111111111111111",
    "cardHolderName": "John Doe",
    "expiryDate": "12/25",
    "cvv": "123"
  }'
```

### Get Payment
```bash
curl http://localhost:8082/api/payments/{paymentId}
```

### Refund Payment
```bash
curl -X POST http://localhost:8082/api/payments/{paymentId}/refund
```

## Docker

### Build the Docker image:
```bash
docker build -t foodybuddy-payments .
```

### Run the container:
```bash
docker run -p 8082:8082 foodybuddy-payments
```

## Database

This service uses H2 in-memory database for development. In production, you would typically use PostgreSQL or MySQL.

## Technologies Used

- Spring Boot 3.2.0
- Spring Data JPA
- H2 Database
- Gradle 8.5
- Java 17
