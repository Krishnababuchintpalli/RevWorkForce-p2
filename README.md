# RevWorkForce (P2)

RevWorkForce is a full-stack HRM application with Angular 16 frontend and Spring Boot backend.  
It supports role-based workflows for Employee, Manager, and Admin.

## Tech Stack
- Frontend: Angular 16, Bootstrap 5
- Backend: Java 17, Spring Boot 2.7, Spring Security, JWT, JPA/Hibernate
- Database: MySQL (`revworkforcep2`)
- Logging: Log4j2 (+ AOP method logging + request correlation id)
- Testing: JUnit4 + Mockito

## Repository Structure
- `backend/`: Spring Boot API
- `frontend/`: Angular web app
- `ERD.md`: Data model overview
- `ARCHITECTURE.md`: Component architecture
- `TESTING.md`: Test scope and commands

## Local Setup

### 1. Database (MySQL)
Create database:
```sql
CREATE DATABASE revworkforcep2;
```

Default backend DB config:
- URL: `jdbc:mysql://localhost:3306/revworkforcep2`
- Username: `root`
- Password: `root`

### 2. Backend
```bash
cd backend
mvn spring-boot:run
```
API base URL: `http://localhost:8080/api`

### 3. Frontend
```bash
cd frontend
npm install
npm start
```
App URL: `http://localhost:4200`

## Default Login Credentials
`DataLoader` seeds users only when `employees` table is empty.

- Admin: `admin@revworkforce.com` / `Admin@123`
- Manager: `manager@revworkforce.com` / `Manager@123`
- Employee: `employee@revworkforce.com` / `Employee@123`

If you already have records in DB, use your existing credentials.

## Implemented Core APIs
- Auth: `POST /api/auth/login`, `POST /api/auth/reset-password`
- User/Profile: `GET/PUT /api/users/me`, `GET /api/users/search`, `GET /api/users/team`
- Notifications: `GET /api/users/notifications`, `POST /api/users/notifications/send`, `PATCH /api/users/notifications/{id}/read`
- Leaves: apply/cancel/approve/reject + `GET /api/leaves/summary`
- Performance: create review, team/my review views, feedback endpoints
- Goals: create/list/update status
- Admin: departments, designations, holidays, announcements, user activation/manager assignment

## Security
- JWT stateless auth
- BCrypt password hashing
- Role-based authorization on protected endpoints
- Validation on DTOs
- Global exception handling with structured error responses

## Notes
- Leave apply is blocked when remaining leave balance is zero.
- Admin can review performance for both employees and managers.
- Manager can review only direct reportees.
