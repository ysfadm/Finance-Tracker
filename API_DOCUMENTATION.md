# Finance Tracker API Documentation

## Base URL

```
http://localhost:8080/api
```

## Authentication

All endpoints except `/auth/register` and `/auth/login` require JWT authentication.

**Header Format:**

```
Authorization: Bearer <JWT_TOKEN>
```

---

## Authentication Endpoints

### Register User

**Endpoint:** `POST /auth/register`

**Description:** Create a new user account

**Request Body:**

```json
{
  "email": "user@example.com",
  "password": "SecurePassword123!",
  "firstName": "John",
  "lastName": "Doe",
  "currency": "USD"
}
```

**Success Response (201 Created):**

```json
{
  "success": true,
  "data": {
    "id": 1,
    "email": "user@example.com",
    "firstName": "John",
    "lastName": "Doe",
    "currency": "USD",
    "active": true,
    "createdAt": "2024-01-09T10:30:00"
  },
  "message": "User registered successfully"
}
```

**Error Response (400 Bad Request):**

```json
{
  "success": false,
  "message": "User already exists with this email"
}
```

---

### Login

**Endpoint:** `POST /auth/login`

**Description:** Authenticate user and receive JWT token

**Request Body:**

```json
{
  "email": "user@example.com",
  "password": "SecurePassword123!"
}
```

**Success Response (200 OK):**

```json
{
  "success": true,
  "data": {
    "token": "eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9...",
    "email": "user@example.com",
    "firstName": "John",
    "lastName": "Doe"
  },
  "message": "Login successful"
}
```

**Error Response (401 Unauthorized):**

```json
{
  "success": false,
  "message": "Invalid email or password"
}
```

---

## Transaction Endpoints

### Get All Transactions

**Endpoint:** `GET /transactions?page=0&size=10`

**Description:** Retrieve paginated list of transactions

**Query Parameters:**

- `page` (int): Page number (0-indexed), default: 0
- `size` (int): Items per page, default: 10

**Headers:**

```
Authorization: Bearer <TOKEN>
```

**Success Response (200 OK):**

```json
{
  "success": true,
  "data": {
    "content": [
      {
        "id": 1,
        "title": "Grocery Shopping",
        "description": "Weekly groceries",
        "amount": 75.5,
        "type": "EXPENSE",
        "categoryId": 1,
        "transactionDate": "2024-01-09T10:30:00",
        "createdAt": "2024-01-09T10:30:00"
      }
    ],
    "totalPages": 1,
    "totalElements": 1,
    "currentPage": 0
  },
  "message": "Transactions retrieved"
}
```

---

### Get Transaction by ID

**Endpoint:** `GET /transactions/{id}`

**Description:** Retrieve a specific transaction

**Path Parameters:**

- `id` (long): Transaction ID

**Headers:**

```
Authorization: Bearer <TOKEN>
```

**Success Response (200 OK):**

```json
{
  "success": true,
  "data": {
    "id": 1,
    "title": "Grocery Shopping",
    "description": "Weekly groceries",
    "amount": 75.5,
    "type": "EXPENSE",
    "categoryId": 1,
    "transactionDate": "2024-01-09T10:30:00",
    "createdAt": "2024-01-09T10:30:00"
  },
  "message": "Transaction retrieved"
}
```

---

### Create Transaction

**Endpoint:** `POST /transactions`

**Description:** Create a new transaction

**Request Body:**

```json
{
  "title": "Grocery Shopping",
  "description": "Weekly groceries",
  "amount": 75.5,
  "type": "EXPENSE",
  "categoryId": 1,
  "transactionDate": "2024-01-09T10:30:00"
}
```

**Headers:**

```
Authorization: Bearer <TOKEN>
Content-Type: application/json
```

**Success Response (201 Created):**

```json
{
  "success": true,
  "data": {
    "id": 1,
    "title": "Grocery Shopping",
    "description": "Weekly groceries",
    "amount": 75.5,
    "type": "EXPENSE",
    "categoryId": 1,
    "transactionDate": "2024-01-09T10:30:00",
    "createdAt": "2024-01-09T10:30:00"
  },
  "message": "Transaction created successfully"
}
```

---

### Update Transaction

**Endpoint:** `PUT /transactions/{id}`

**Description:** Update an existing transaction

**Path Parameters:**

- `id` (long): Transaction ID

**Request Body:**

```json
{
  "title": "Updated Grocery Shopping",
  "description": "Weekly groceries - Updated",
  "amount": 85.5,
  "type": "EXPENSE",
  "categoryId": 1,
  "transactionDate": "2024-01-09T10:30:00"
}
```

**Headers:**

```
Authorization: Bearer <TOKEN>
Content-Type: application/json
```

**Success Response (200 OK):**

```json
{
  "success": true,
  "data": {
    "id": 1,
    "title": "Updated Grocery Shopping",
    "amount": 85.5
  },
  "message": "Transaction updated successfully"
}
```

---

### Delete Transaction

**Endpoint:** `DELETE /transactions/{id}`

**Description:** Delete a transaction

**Path Parameters:**

- `id` (long): Transaction ID

**Headers:**

```
Authorization: Bearer <TOKEN>
```

**Success Response (200 OK):**

```json
{
  "success": true,
  "data": null,
  "message": "Transaction deleted successfully"
}
```

---

## Category Endpoints

### Get All Categories

**Endpoint:** `GET /categories`

**Description:** Retrieve all categories for the authenticated user

**Headers:**

```
Authorization: Bearer <TOKEN>
```

**Success Response (200 OK):**

```json
{
  "success": true,
  "data": [
    {
      "id": 1,
      "name": "Groceries",
      "description": "Food and groceries",
      "type": "EXPENSE",
      "color": "#27ae60"
    },
    {
      "id": 2,
      "name": "Salary",
      "description": "Monthly salary",
      "type": "INCOME",
      "color": "#3498db"
    }
  ],
  "message": "Categories retrieved"
}
```

---

### Create Category

**Endpoint:** `POST /categories`

**Description:** Create a new category

**Request Body:**

```json
{
  "name": "Utilities",
  "description": "Electricity, water, gas",
  "type": "EXPENSE",
  "color": "#e74c3c"
}
```

**Headers:**

```
Authorization: Bearer <TOKEN>
Content-Type: application/json
```

**Success Response (201 Created):**

```json
{
  "success": true,
  "data": {
    "id": 3,
    "name": "Utilities",
    "description": "Electricity, water, gas",
    "type": "EXPENSE",
    "color": "#e74c3c"
  },
  "message": "Category created successfully"
}
```

---

### Update Category

**Endpoint:** `PUT /categories/{id}`

**Description:** Update a category

**Path Parameters:**

- `id` (long): Category ID

**Request Body:**

```json
{
  "name": "Utilities Updated",
  "description": "Updated description",
  "color": "#f39c12"
}
```

**Headers:**

```
Authorization: Bearer <TOKEN>
Content-Type: application/json
```

---

### Delete Category

**Endpoint:** `DELETE /categories/{id}`

**Description:** Delete a category

**Path Parameters:**

- `id` (long): Category ID

**Headers:**

```
Authorization: Bearer <TOKEN>
```

---

## User Endpoints

### Get Profile

**Endpoint:** `GET /users/profile`

**Description:** Get authenticated user's profile

**Headers:**

```
Authorization: Bearer <TOKEN>
```

**Success Response (200 OK):**

```json
{
  "success": true,
  "data": {
    "id": 1,
    "email": "user@example.com",
    "firstName": "John",
    "lastName": "Doe",
    "currency": "USD",
    "active": true
  },
  "message": "Profile retrieved"
}
```

---

### Update Profile

**Endpoint:** `PUT /users/profile`

**Description:** Update user profile

**Request Body:**

```json
{
  "firstName": "Jane",
  "lastName": "Smith",
  "currency": "EUR"
}
```

**Headers:**

```
Authorization: Bearer <TOKEN>
Content-Type: application/json
```

---

### Change Password

**Endpoint:** `POST /users/change-password`

**Description:** Change user password

**Request Body:**

```json
{
  "oldPassword": "CurrentPassword123!",
  "newPassword": "NewPassword456!"
}
```

**Headers:**

```
Authorization: Bearer <TOKEN>
Content-Type: application/json
```

---

## Error Handling

### Common Error Responses

**400 Bad Request:**

```json
{
  "success": false,
  "message": "Invalid input data"
}
```

**401 Unauthorized:**

```json
{
  "success": false,
  "message": "Invalid or expired token"
}
```

**403 Forbidden:**

```json
{
  "success": false,
  "message": "Access denied"
}
```

**404 Not Found:**

```json
{
  "success": false,
  "message": "Resource not found"
}
```

**500 Internal Server Error:**

```json
{
  "success": false,
  "message": "An error occurred while processing your request"
}
```

---

## Rate Limiting

**Limits (per minute):**

- `/auth/login`: 5 requests
- `/auth/register`: 3 requests
- Other endpoints: 100 requests

---

## Status Codes

| Code | Meaning                                |
| ---- | -------------------------------------- |
| 200  | OK - Request successful                |
| 201  | Created - Resource created             |
| 400  | Bad Request - Invalid input            |
| 401  | Unauthorized - Authentication required |
| 403  | Forbidden - Access denied              |
| 404  | Not Found - Resource not found         |
| 500  | Internal Server Error                  |

---

## Example cURL Commands

### Register

```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "email": "user@example.com",
    "password": "Password123!",
    "firstName": "John",
    "lastName": "Doe"
  }'
```

### Login

```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "user@example.com",
    "password": "Password123!"
  }'
```

### Get Transactions

```bash
curl -X GET http://localhost:8080/api/transactions?page=0&size=10 \
  -H "Authorization: Bearer <TOKEN>"
```

---

**Last Updated:** January 9, 2026
