# E-commerce Application

A basic e-commerce application built with Spring Boot.

## Prerequisites

- Java 17 or higher
- Maven 3.6 or higher

## Features

- Product management
- User authentication and authorization
- Shopping cart functionality
- Order management
- H2 in-memory database

## Getting Started

1. Clone the repository
2. Navigate to the project directory
3. Run the application:
   ```bash
   mvn spring-boot:run
   ```

## Access Points

- Application: http://localhost:8080
- H2 Console: http://localhost:8080/h2-console
  - JDBC URL: jdbc:h2:mem:ecommercedb
  - Username: sa
  - Password: password

## API Endpoints

### Products
- GET /api/products - List all products
- GET /api/products/{id} - Get a specific product
- POST /api/products - Create a new product (Admin only)
- PUT /api/products/{id} - Update a product (Admin only)
- DELETE /api/products/{id} - Delete a product (Admin only)

### Users
- POST /api/auth/register - Register a new user
- POST /api/auth/login - Login user

### Orders
- POST /api/orders - Create a new order
- GET /api/orders - Get user's orders
- GET /api/orders/{id} - Get specific order details 