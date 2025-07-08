# Spring Boot Authentication REST API

A professional REST API built with Spring Boot featuring secure user authentication using **BCrypt password encryption** and JWT tokens. This project demonstrates industry best practices for building secure authentication systems.

## 🔐 Security Features

- **BCrypt Password Encryption** - Industry-standard password hashing with configurable strength
- **JWT Authentication** - Stateless authentication using JSON Web Tokens
- **Role-based Access Control** - Support for USER and ADMIN roles
- **Input Validation** - Comprehensive validation using Jakarta Bean Validation
- **Global Exception Handling** - Consistent error responses across all endpoints
- **CORS Configuration** - Ready for frontend integration

## 🚀 Quick Start

### Prerequisites

- Java 17 or higher
- Gradle 8.5+ (or use the included wrapper)

### Installation

1. Clone the repository:
```bash
git clone <repository-url>
cd spring-boot-auth-api
```

2. Build the project:
```bash
./gradlew build
```

3. Run the application:
```bash
./gradlew bootRun
# Or with development profile
./gradlew runDev
```

4. **🔐 Test BCrypt Password Encryption:**
```bash
./gradlew runBCryptDemo
```

The API will be available at `http://localhost:8080`

## 📚 API Documentation

### Authentication Endpoints

#### Register User
```http
POST /api/auth/register
Content-Type: application/json

{
  "username": "johndoe",
  "email": "john@example.com",
  "password": "SecurePass123!",
  "confirmPassword": "SecurePass123!"
}
```

**Password Requirements:**
- Minimum 8 characters
- Contains uppercase letter
- Contains lowercase letter
- Contains digit
- Contains special character

#### Login User
```http
POST /api/auth/login
Content-Type: application/json

{
  "username": "johndoe",
  "password": "SecurePass123!"
}
```

#### Response Format
```json
{
  "success": true,
  "message": "Login successful",
  "data": {
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "type": "Bearer",
    "user": {
      "id": 1,
      "username": "johndoe",
      "email": "john@example.com",
      "role": "USER"
    }
  },
  "timestamp": "2024-01-01T12:00:00"
}
```

### Protected Endpoints

All protected endpoints require the JWT token in the Authorization header:
```http
Authorization: Bearer <your-jwt-token>
```

#### Get User Profile
```http
GET /api/users/profile
Authorization: Bearer <token>
```

#### Change Password
```http
POST /api/users/change-password
Authorization: Bearer <token>
Content-Type: application/json

{
  "oldPassword": "SecurePass123!",
  "newPassword": "NewSecurePass456!"
}
```

#### Get User Statistics
```http
GET /api/users/stats
Authorization: Bearer <token>
```

#### Admin Only Endpoint
```http
GET /api/users/admin/info
Authorization: Bearer <admin-token>
```

## 🔒 Password Encryption Details

This API uses **BCrypt** for password hashing, which provides:

1. **Salt Generation**: Each password gets a unique salt
2. **Configurable Cost Factor**: Set to 12 for enhanced security
3. **One-way Hashing**: Passwords cannot be reversed
4. **Time-constant Verification**: Prevents timing attacks

### BCrypt Configuration

The BCrypt strength is configured in `application.properties`:
```properties
app.security.bcrypt.strength=12
```

### Password Security Implementation

```java
// Registration - Password is encrypted before saving
String encryptedPassword = passwordEncoder.encode(plainPassword);
user.setPassword(encryptedPassword);

// Login - Password verification using BCrypt
boolean isValid = passwordEncoder.matches(plainPassword, encryptedPassword);
```

## 🛠️ Configuration

### Database Configuration (H2 for Development)
```properties
spring.datasource.url=jdbc:h2:mem:authdb
spring.h2.console.enabled=true
spring.h2.console.path=/h2-console
```

Access H2 Console: `http://localhost:8080/h2-console`

### JWT Configuration
```properties
app.jwt.secret=your-secret-key
app.jwt.expiration=86400000  # 24 hours
```

**⚠️ Important:** Change the JWT secret in production!

## 🏗️ Architecture

```
├── controller/          # REST Controllers
├── service/            # Business Logic
├── repository/         # Data Access Layer
├── security/           # JWT & Security Configuration
├── dto/               # Data Transfer Objects
├── entity/            # JPA Entities
├── exception/         # Exception Handling
└── config/            # Configuration Classes
```

## 🧪 Testing the API

### Using cURL

1. **Register a new user:**
```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "email": "test@example.com",
    "password": "TestPass123!",
    "confirmPassword": "TestPass123!"
  }'
```

2. **Login:**
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "password": "TestPass123!"
  }'
```

3. **Access protected endpoint:**
```bash
curl -X GET http://localhost:8080/api/users/profile \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

## 🔧 Production Considerations

1. **Change JWT Secret**: Generate a strong, unique secret key
2. **Use PostgreSQL/MySQL**: Replace H2 with a production database
3. **Enable HTTPS**: Configure SSL/TLS certificates
4. **Environment Variables**: Use environment variables for sensitive configuration
5. **Logging**: Configure appropriate logging levels
6. **Rate Limiting**: Implement API rate limiting
7. **Monitoring**: Add health checks and metrics

### Environment Variables Example
```bash
export JWT_SECRET=your-production-secret-key
export DB_URL=jdbc:postgresql://localhost:5432/authdb
export DB_USERNAME=your-db-username
export DB_PASSWORD=your-db-password
```

## 📈 Features Implemented

- ✅ BCrypt Password Encryption
- ✅ JWT Authentication
- ✅ User Registration & Login
- ✅ Role-based Authorization
- ✅ Input Validation
- ✅ Global Exception Handling
- ✅ CORS Configuration
- ✅ Password Strength Validation
- ✅ Secure Password Change
- ✅ Clean Architecture Pattern

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch
3. Commit your changes
4. Push to the branch
5. Create a Pull Request

## 📄 License

This project is licensed under the MIT License - see the LICENSE file for details.

## 🆘 Support

If you encounter any issues or have questions, please create an issue in the repository.

---

**Built with ❤️ using Spring Boot and industry-standard security practices**