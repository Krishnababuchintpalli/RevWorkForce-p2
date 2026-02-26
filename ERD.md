# Entity Relationship Diagram (Current Mapping)

```mermaid
erDiagram
    EMPLOYEES ||--o{ EMPLOYEES : manages
    EMPLOYEES ||--o{ LEAVE_REQUESTS : submits
    EMPLOYEES ||--o{ PERFORMANCE_REVIEWS : owns
    EMPLOYEES ||--o{ GOALS : owns
    EMPLOYEES ||--o{ NOTIFICATIONS : receives

    ROLES ||--o{ EMPLOYEES : assigned_role
    DEPARTMENTS ||--o{ EMPLOYEES : assigned_department
    DESIGNATIONS ||--o{ EMPLOYEES : assigned_designation

    EMPLOYEES {
      long employee_id PK
      string email UK
      string password
      string name
      string phone
      string address
      string emergency_contact
      long dept_id FK
      long desig_id FK
      long manager_id FK
      int role_id FK
      string status
      datetime created_at
    }

    ROLES {
      int role_id PK
      string role_name UK
    }

    DEPARTMENTS {
      long dept_id PK
      string dept_name UK
    }

    DESIGNATIONS {
      long desig_id PK
      string desig_name UK
    }

    LEAVE_REQUESTS {
      long id PK
      long employee_id FK
      date start_date
      date end_date
      string leave_type
      string reason
      string status
      string manager_comment
      datetime created_at
    }

    PERFORMANCE_REVIEWS {
      long id PK
      long employee_id FK
      string key_deliverables
      string accomplishments
      string areas_of_improvement
      int self_rating
      string manager_feedback
      int manager_rating
      string status
      datetime created_at
    }

    GOALS {
      long id PK
      long employee_id FK
      string description
      date deadline
      string priority
      string status
      string manager_comment
    }

    NOTIFICATIONS {
      long id PK
      long user_id FK
      string message
      boolean read_flag
      datetime created_at
    }

    HOLIDAYS {
      long id PK
      string name
      date date
    }

    ANNOUNCEMENTS {
      long id PK
      string title
      string content
      datetime created_at
    }
```

Note: Some environments may still contain legacy duplicate FK columns from earlier mapping attempts (for example `employee_employee_id` / `user_employee_id`). Current code keeps compatibility for those during persistence.
