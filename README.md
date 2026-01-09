# Finance Tracker - Complete Application

A full-stack personal finance management application built with Java Spring Boot backend and React Native frontend.

## 🎯 Features

- **User Authentication**: Secure JWT-based authentication
- **Transaction Management**: Create, read, update, delete financial transactions
- **Category Management**: Organize transactions by custom categories
- **Budget Tracking**: Set and monitor spending budgets
- **Dashboard**: Visual overview of income, expenses, and balance
- **Responsive Design**: Mobile-first design for all devices
- **Data Security**: Encrypted passwords, secure token handling
- **Performance**: Optimized database queries with proper indexing

## 📋 Prerequisites

- **Java**: JDK 21+
- **Maven**: 3.9.12+
- **PostgreSQL**: 15+
- **Docker**: 20.10+ (for containerized deployment)
- **Node.js**: 18+ (for frontend development)

## 🚀 Quick Start

### 1. Using Docker Compose (Recommended)

```bash
# Clone the repository
git clone <repository-url>
cd Finance-Tracker

# Create .env file from template
cp .env.example .env

# Update .env with your configuration
nano .env

# Start all services
docker-compose up -d

# Access the application
# Backend API: http://localhost:8080/api
# pgAdmin: http://localhost:5050
```

### 2. Local Development Setup

#### Backend Setup

```bash
cd backend

# Copy environment file
cp .env.example .env

# Install dependencies
mvn clean install

# Run the application
mvn spring-boot:run
```

The backend will start at `http://localhost:8080/api`

#### Database Setup

```bash
# Using Docker for PostgreSQL
docker run -d \
  --name finance_tracker_db \
  -e POSTGRES_DB=finance_tracker_db \
  -e POSTGRES_USER=postgres \
  -e POSTGRES_PASSWORD=password \
  -p 5432:5432 \
  postgres:15-alpine

# Initialize schema
psql -U postgres -d finance_tracker_db < database/schema.sql
```

#### Frontend Setup

```bash
cd frontend

# Install dependencies
npm install

# Run the development server
npm start

# For production build
npm run build
```

## 🔐 Environment Configuration

Create a `.env` file in the root directory:

```env
# Database
DB_URL=jdbc:postgresql://localhost:5432/finance_tracker_db
DB_USERNAME=postgres
DB_PASSWORD=secure_password
DB_PORT=5432

# JWT
JWT_SECRET=your_secret_key_minimum_32_characters
JWT_EXPIRATION=86400000

# Server
SERVER_PORT=8080

# Logging
LOG_LEVEL=INFO

# CORS
CORS_ALLOWED_ORIGINS=http://localhost:3000,http://localhost:8081
```

## 📚 API Endpoints

### Authentication

| Method | Endpoint         | Description        |
| ------ | ---------------- | ------------------ |
| POST   | `/auth/register` | Register new user  |
| POST   | `/auth/login`    | User login         |
| GET    | `/auth/validate` | Validate JWT token |
| POST   | `/auth/refresh`  | Refresh token      |

### Transactions

| Method | Endpoint              | Description                      |
| ------ | --------------------- | -------------------------------- |
| GET    | `/transactions`       | Get all transactions (paginated) |
| GET    | `/transactions/{id}`  | Get transaction by ID            |
| POST   | `/transactions`       | Create new transaction           |
| PUT    | `/transactions/{id}`  | Update transaction               |
| DELETE | `/transactions/{id}`  | Delete transaction               |
| GET    | `/transactions/range` | Get transactions by date range   |

### Categories

| Method | Endpoint           | Description         |
| ------ | ------------------ | ------------------- |
| GET    | `/categories`      | Get all categories  |
| GET    | `/categories/{id}` | Get category by ID  |
| POST   | `/categories`      | Create new category |
| PUT    | `/categories/{id}` | Update category     |
| DELETE | `/categories/{id}` | Delete category     |

### Users

| Method | Endpoint                 | Description         |
| ------ | ------------------------ | ------------------- |
| GET    | `/users/profile`         | Get user profile    |
| PUT    | `/users/profile`         | Update user profile |
| POST   | `/users/change-password` | Change password     |

## 🔒 Security Features

- **JWT Authentication**: Stateless API authentication
- **CORS Protection**: Configurable cross-origin requests
- **Password Encryption**: BCrypt with strength 12
- **Security Headers**: XSS, Clickjacking, CSP protections
- **HTTPS Ready**: Full support for SSL/TLS
- **Input Validation**: Server-side validation on all inputs
- **SQL Injection Prevention**: Parameterized queries via JPA

## 📊 Database Schema

### Tables

- **users**: User account information
- **categories**: Transaction categories
- **transactions**: Financial transactions
- **budgets**: Budget limits and tracking

### Key Features

- Proper indexing for query performance
- Foreign key constraints
- Cascade delete for data integrity
- Timestamp tracking (created_at, updated_at)

## 🧪 Testing

### Run Backend Tests

```bash
cd backend
mvn test
```

### Test Coverage

```bash
mvn jacoco:report
```

### API Testing with Postman

1. Import `backend/Finance-Tracker-API.postman_collection.json`
2. Set environment variables (token, user email, etc.)
3. Run the collection

## 📦 Building for Production

### Docker Build

```bash
docker build -f backend/Dockerfile -t finance-tracker-api:latest .
```

### Maven Build

```bash
cd backend
mvn clean package -DskipTests -P production
```

## 🚢 Deployment

### Docker Compose Deployment

```bash
docker-compose -f docker-compose.yml up -d
```

### Docker Swarm

```bash
docker stack deploy -c docker-compose.yml finance-tracker
```

### Kubernetes (optional)

Create Kubernetes manifests in `k8s/` directory for production deployment.

## 📈 CI/CD Pipeline

GitHub Actions workflows are configured in `.github/workflows/`:

- **build.yml**: Builds and tests the application on every push
- Runs Maven tests with PostgreSQL service
- Generates test reports
- Builds Docker image

## 🔧 Configuration

### Application Properties

Edit `backend/src/main/resources/application.yml`:

```yaml
spring:
  datasource:
    url: ${DB_URL}
  jpa:
    hibernate:
      ddl-auto: validate # or update/create

jwt:
  expiration: 86400000 # 24 hours in milliseconds
```

### Logging Configuration

```yaml
logging:
  level:
    root: INFO
    com.financetracker: DEBUG
```

## 🐛 Troubleshooting

### Database Connection Issues

```bash
# Check PostgreSQL is running
docker ps | grep postgres

# Verify credentials
psql -h localhost -U postgres -d finance_tracker_db
```

### Port Already in Use

```bash
# Find process using port 8080
lsof -i :8080

# Kill process
kill -9 <PID>
```

### Build Failures

```bash
# Clean Maven cache
mvn clean
mvn install

# Check Java version
java -version # Should be 21+
```

## 📝 Contributing

1. Create a feature branch
2. Make your changes
3. Run tests: `mvn test`
4. Commit with descriptive message
5. Push and create Pull Request

## 📄 License

This project is licensed under the MIT License - see LICENSE file for details.

## 👥 Support

For issues and questions:

- Create an issue in GitHub
- Check existing documentation
- Review API collection in Postman

## 🎓 Learning Resources

- Spring Boot Documentation: https://spring.io/projects/spring-boot
- React Native: https://reactnative.dev
- PostgreSQL: https://www.postgresql.org
- JWT: https://jwt.io

---

**Last Updated**: January 9, 2026
