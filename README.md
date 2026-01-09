# Finance Tracker

A full-stack personal finance management application built with Java Spring Boot backend and React frontend.

## Features

- User Authentication with JWT
- Transaction Management (CRUD operations)
- Category Management
- Budget Tracking
- Dashboard Overview
- Responsive Design
- Data Security with encryption

## Prerequisites

- Java JDK 21+
- Maven 3.9.12+
- PostgreSQL 15+
- Docker 20.10+
- Node.js 18+

## Quick Start

### Docker Compose (Recommended)

```bash
git clone <repository-url>
cd Finance-Tracker
cp .env.example .env
# Edit .env with your configuration
docker-compose up -d
```

Access at:

- Frontend: http://localhost:3000
- Backend API: http://localhost:8080/api

### Local Development

**Backend:**

```bash
cd backend
mvn clean install
mvn spring-boot:run
```

**Database:**

```bash
docker run -d --name finance_db \
  -e POSTGRES_DB=finance_tracker_db \
  -e POSTGRES_USER=postgres \
  -e POSTGRES_PASSWORD=password \
  -p 5432:5432 \
  postgres:15-alpine
```

**Frontend:**

```bash
cd frontend
npm install
npm start
```

## Application URLs

| Service     | URL                              |
| ----------- | -------------------------------- |
| Frontend    | http://localhost:3000            |
| Backend API | http://localhost:8080/api        |
| Admin Panel | http://localhost:3000/admin.html |

## API Endpoints

### Authentication

- `POST /api/auth/register` - User registration
- `POST /api/auth/login` - User login

### Transactions

- `GET /api/transactions` - List transactions
- `POST /api/transactions` - Create transaction
- `PUT /api/transactions/{id}` - Update transaction
- `DELETE /api/transactions/{id}` - Delete transaction

### Categories

- `GET /api/categories` - List categories
- `POST /api/categories` - Create category
- `PUT /api/categories/{id}` - Update category
- `DELETE /api/categories/{id}` - Delete category

## Architecture

```
Frontend (React)
├── Login/Register
├── Dashboard
└── Admin Panel
    ↓
Backend (Spring Boot)
├── REST API
├── JWT Authentication
└── PostgreSQL
```

## Security

- JWT-based authentication
- Bcrypt password encryption
- CORS protection
- Input validation
- SQL injection prevention

## Building & Deployment

```bash
# Build backend
cd backend
mvn clean package

# Build frontend
cd frontend
npm run build

# Deploy with Docker
docker-compose up -d
```

## Testing

```bash
cd backend
mvn test
```

## Environment Configuration

Create `.env` file:

```env
DB_URL=jdbc:postgresql://localhost:5432/finance_tracker_db
DB_USERNAME=postgres
DB_PASSWORD=password
JWT_SECRET=your_secret_key_minimum_32_characters
JWT_EXPIRATION=86400000
SERVER_PORT=8080
CORS_ALLOWED_ORIGINS=http://localhost:3000
```

## License

MIT License

## Support

For issues or questions, create an issue in GitHub.
