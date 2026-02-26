# Application Architecture

```mermaid
flowchart TD
    FE[Angular 16 + Bootstrap UI] -->|HTTP + JWT| API[Spring Boot REST Controllers]

    API --> SVC[Service Layer\nBusiness Rules + Transactions]
    SVC --> REPO[Repository Layer\nSpring Data JPA]
    REPO --> DB[(MySQL: revworkforcep2)]

    API --> SEC[Spring Security Filter Chain]
    SEC --> JWT[JwtAuthenticationFilter]
    JWT --> UDS[UserDetailsService]
    UDS --> REPO

    API --> EX[GlobalExceptionHandler]

    LOG1[RequestLoggingFilter\nHTTP_IN/HTTP_OUT + requestId] --> API
    LOG2[AOP Logging Aspect\nController/Service/Repository] --> SVC
    LOG3[Log4j2 Console + Rolling File] --> API
    LOG3 --> SVC
```

## Layers
- Controller: request mapping, validation boundary, response payloads.
- Service: authorization/business rules (leave workflow, goals, reviews, notifications).
- Repository: JPA access to MySQL tables.

## Security Flow
- `/api/auth/login` returns JWT.
- JWT is sent as `Authorization: Bearer <token>`.
- `JwtAuthenticationFilter` validates token and sets security context.
- `@PreAuthorize` guards role-specific endpoints.

## Logging Flow
- Per-request logs with correlation id (`requestId`).
- AOP logs method entry/exit and duration.
- Exception handler logs validation and runtime failures.
