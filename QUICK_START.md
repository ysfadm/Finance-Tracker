# Quick Start Guide - Finance Tracker

## Prerequisites
- Java 21 LTS
- Maven 3.9.12
- Node.js 18+
- npm 9+
- PostgreSQL 15
- Docker (optional)

## Local Development Setup

### 1. Backend Setup (Java 21)
```bash
cd backend

# Build the project
mvn clean package

# Run the application
mvn spring-boot:run

# The backend will start on http://localhost:8080
```

**Backend Services**:
- Auth Service: `/api/auth/*`
- User Service: `/api/users/*`
- Category Service: `/api/categories/*`
- Transaction Service: `/api/transactions/*`

### 2. Database Setup (PostgreSQL)
```bash
# Using PostgreSQL directly
createdb finance_tracker
psql finance_tracker < database/schema.sql

# Or using Docker Compose (see Docker section below)
```

**Database Connection**:
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/finance_tracker
spring.datasource.username=postgres
spring.datasource.password=your_password
```

### 3. Frontend Setup (React Native + TypeScript)
```bash
cd frontend

# Install dependencies
npm install

# Run TypeScript compiler (validation)
npx tsc --noEmit

# Run linter
npm run lint

# Start the development server
npm start

# For web preview
npm run web

# For Android
npm run android

# For iOS (macOS only)
npm run ios
```

## Docker Deployment

### Option 1: Docker Compose (Recommended for local testing)
```bash
# Start all services (PostgreSQL, Backend, PgAdmin)
docker-compose up -d

# Backend: http://localhost:8080
# PgAdmin: http://localhost:5050
# Database: localhost:5432

# Stop all services
docker-compose down
```

### Option 2: Build Individual Docker Image
```bash
# Build backend Docker image
cd backend
docker build -t finance-tracker:latest .

# Run container
docker run -p 8080:8080 \
  -e DB_HOST=postgres \
  -e DB_NAME=finance_tracker \
  -e DB_USER=postgres \
  -e DB_PASSWORD=secret \
  finance-tracker:latest
```

## Environment Configuration

### Backend (`backend/src/main/resources/application.yml`)
```yaml
server:
  port: 8080

spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/finance_tracker
    username: postgres
    password: secret
  jpa:
    hibernate:
      ddl-auto: validate
```

### Frontend (`.env` file)
```bash
REACT_APP_API_URL=http://localhost:8080/api
REACT_APP_ENV=development
```

## API Testing

### Using curl:
```bash
# Register a user
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "email": "user@example.com",
    "password": "password123",
    "firstName": "John",
    "lastName": "Doe"
  }'

# Login
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "user@example.com",
    "password": "password123"
  }'

# Create a category
curl -X POST http://localhost:8080/api/categories \
  -H "Authorization: Bearer YOUR_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Food",
    "type": "EXPENSE"
  }'

# Add a transaction
curl -X POST http://localhost:8080/api/transactions \
  -H "Authorization: Bearer YOUR_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Lunch",
    "amount": 15.50,
    "type": "EXPENSE",
    "categoryId": 1,
    "transactionDate": "2024-01-09"
  }'
```

### Using Postman:
1. Import the provided Postman collection
2. Set the `{{base_url}}` variable to `http://localhost:8080/api`
3. Login to get a token and it will be automatically added to requests

## Build & Test

### Backend:
```bash
cd backend

# Build with tests
mvn clean package

# Skip tests
mvn clean package -DskipTests

# Run tests only
mvn test

# Check code quality
mvn spotbugs:check
```

### Frontend:
```bash
cd frontend

# TypeScript type checking
npx tsc --noEmit

# Lint code
npm run lint

# Run tests
npm test
```

## Deployment

### AWS Deployment
```bash
# See DEPLOYMENT.md for detailed AWS deployment steps
# Includes: EC2, RDS, and ALB setup
```

### Docker Hub Deployment
```bash
# Tag and push to Docker Hub
docker tag finance-tracker:latest username/finance-tracker:1.0
docker push username/finance-tracker:1.0
```

### Kubernetes Deployment
```bash
# Apply Kubernetes manifests (if created)
kubectl apply -f k8s/
kubectl get pods
kubectl get svc
```

## Troubleshooting

### Backend Issues:
```bash
# Check if port 8080 is in use
netstat -an | grep 8080

# Clear Maven cache
mvn clean

# Force Java 21
export JAVA_HOME=/path/to/java21
```

### Frontend Issues:
```bash
# Clear npm cache
npm cache clean --force

# Reinstall dependencies
rm -rf node_modules package-lock.json
npm install

# Clear TypeScript cache
rm -rf node_modules/.cache
```

### Database Issues:
```bash
# Connect to PostgreSQL
psql -U postgres -d finance_tracker

# Check tables
\dt

# Recreate database
dropdb finance_tracker
createdb finance_tracker
psql finance_tracker < database/schema.sql
```

## Documentation

- **README.md** - Project overview and features
- **API_DOCUMENTATION.md** - Complete API reference
- **DEPLOYMENT.md** - Multi-platform deployment guides
- **COMPLETION_STATUS.md** - Project status and statistics

## Common Commands

```bash
# Backend
mvn spring-boot:run          # Run backend
mvn clean package            # Build backend
mvn test                      # Run backend tests

# Frontend
npm start                     # Start development server
npm run web                   # Start web preview
npm run lint                  # Lint code
npm test                      # Run frontend tests

# Database
psql -U postgres             # Connect to PostgreSQL
docker-compose up -d         # Start Docker services
docker-compose down          # Stop Docker services

# Git
git status                   # Check git status
git log --oneline           # View commit history
git push origin main        # Push changes
```

## Support & Resources

- Java 21 Docs: https://docs.oracle.com/en/java/javase/21/
- Spring Boot 3.3: https://spring.io/projects/spring-boot
- React Native: https://reactnative.dev/
- PostgreSQL: https://www.postgresql.org/docs/
- Docker: https://docs.docker.com/

---

**Project Status**: ✅ Production Ready
**Last Updated**: 2024-01-09
