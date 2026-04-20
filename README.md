# Lumina Library Management System

A production-grade, full-stack Library Management System built to demonstrate senior-level software engineering practices, robustness, and polished UX.

## Architecture & Tech Stack

This project is divided into two decoupled monolithic layers communicating over REST API.

### Backend (`/backend`)
- **Framework:** Spring Boot 3 + Java 21
- **Database:** PostgreSQL (H2 for local development/testing)
- **Security:** Spring Security 6 + JWT (Access/Refresh Token strategy)
- **Data Access:** Spring Data JPA + Flyway Migrations
- **API Documentation:** OpenAPI / Swagger UI
- **Key Patterns:** DTO mapping (MapStruct), Global Exception Handling, Strategy Pattern (Fines), Role-based Access Control.

### Frontend (`/frontend`)
- **Framework:** Angular 17+ (Standalone Components, Signals)
- **UI:** Angular Material + Custom SCSS (Glassmorphism & gradients)
- **State Management:** Reactive Signals & RxJS
- **Analytics:** Chart.js Integration

## DevOps & CI/CD
- GitHub Actions workflows configured in `.github/workflows/ci.yml` run automated tests and build verifications on every push or pull request to `main`.
- Separation of environments using profile configurations.

## Running Locally

### Backend
```bash
cd backend
mvn spring-boot:run
```
API runs on `http://localhost:8080`, Swagger documentation is available at `http://localhost:8080/swagger-ui.html`.

### Frontend
```bash
cd frontend
npm install --legacy-peer-deps
npm start
```
Frontend runs on `http://localhost:4200`.
