# Haven APP
# BACKEND

A Spring Boot REST API service that provides authentication, payment processing, tracking devices management, and transaction handling functionality.

## Features

- **Authentication & Authorization**
  - User registration (Customer, Admin, Staff)
  - Login with JWT authentication
  - Password management
  - Role-based access control

- **Payment Integration**
  - Midtrans payment gateway integration
  - Payment notification webhook
  - Payment link generation

- **Device Management**
  - Tracker device CRUD operations
  - Device status management
  - Device assignment to transactions

- **Transaction Management**
  - Transaction creation and tracking
  - Status updates
  - Pagination and search functionality

- **User Management**
  - User profile management
  - Staff and customer management
  - Profile image handling

- **Price Management**
  - Ticket pricing CRUD operations
  - Price status management

## Technology Stack

- Java 17
- Spring Boot
- Spring Security
- Spring Data JPA
- Jakarta Validation
- Lombok
- Swagger/OpenAPI
- Midtrans Payment Gateway

## Prerequisites

- JDK 17 or later
- Maven
- MySQL/PostgreSQL Database
- Midtrans Account (for payment integration)

## Installation

1. Clone the repository:
```bash
git clone https://github.com/guteng083/hike-safe.git
```

2. Configure the application.properties:
```properties
spring.datasource.url=jdbc:mysql://localhost:5432/haven_db
spring.datasource.username=your_username
spring.datasource.password=your_password

# JWT Configuration
jwt.secret=your_jwt_secret
jwt.expiration=86400000

# Midtrans Configuration
midtrans.client.key=your_midtrans_client_key
midtrans.server.key=your_midtrans_server_key

# Admin Configuration
admin.secret.key=your_admin_secret_key
```

3. Build the project:
```bash
mvn clean install
```

4. Run the application:
```bash
mvn spring-boot:run
```

## API Documentation

The API documentation is available via Swagger UI at `/swagger-ui.html` when running the application.

### Main API Endpoints

#### Authentication
- `POST /api/auth/register-customer` - Register new customer
- `POST /api/auth/register-admin` - Register new admin
- `POST /api/auth/register-staff` - Register new staff
- `POST /api/auth/login` - User login
- `PATCH /api/auth/password/update` - Change password
- `GET /api/auth/me` - Get current user details

#### Payments
- `POST /api/payments/notification` - Midtrans webhook notification
- `POST /api/payments/{id}/create-payment-link` - Create payment link

#### Tracker Devices
- `POST /api/tracker-devices` - Create new tracker device
- `GET /api/tracker-devices` - Get all tracker devices
- `GET /api/tracker-devices/{id}` - Get tracker device by ID
- `PATCH /api/tracker-devices/{id}` - Update tracker device
- `DELETE /api/tracker-devices/{id}` - Delete tracker device

#### Transactions
- `POST /api/transactions` - Create new transaction
- `GET /api/transactions` - Get all transactions
- `GET /api/transactions/{id}` - Get transaction by ID
- `PATCH /api/transactions/{id}/status` - Update transaction status
- `PATCH /api/transactions/{id}/device/{deviceId}` - Assign device to transaction

## Security

- JWT-based authentication
- Role-based access control (ADMIN, STAFF, CUSTOMER)
- Admin secret key required for admin registration
- Password encryption for user security

## Contributing

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add some amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request
