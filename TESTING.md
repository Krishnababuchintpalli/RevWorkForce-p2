# Testing Artifacts

## Automated Tests (Backend)
Framework: `JUnit4 + Mockito`

### Service Unit Tests
- `AuthServiceTest`
- `UserServiceTest`
- `LeaveServiceTest`
- `NotificationServiceTest`
- `PerformanceServiceTest`
- `GoalServiceTest`

### Controller Unit Tests (Standalone MockMvc)
- `AuthControllerTest`
- `UserControllerTest`
- `LeaveControllerTest`
- `PerformanceControllerTest`
- `GoalControllerTest`
- `AdminControllerTest`

These controller tests validate endpoint mapping, request payload handling, and response structure without booting full Spring context.

## Manual API/Functional Test Checklist
1. Login with employee, manager, admin accounts.
2. Reset password with matching employee id + email and valid password pattern.
3. Employee leave apply/cancel.
4. Manager/Admin approve/reject leave from team/admin views.
5. Validate leave summary (`total`, `remaining`, `pending`) updates correctly.
6. Verify apply is blocked when remaining leaves becomes zero.
7. Send notification (admin to all/specific employee; manager/employee to one user).
8. Toggle notification read/unread.
9. Manager submits performance review for direct reportee.
10. Admin submits performance review for employee/manager.
11. Employee views performance summary and details panel.
12. Goal create + status update, and validate completed goal cannot revert.
13. Admin CRUD for departments/designations/holidays/announcements.

## Commands
```bash
cd backend
mvn test
```
or
```bash
cd backend
mvnd -q test
```

```bash
cd backend
mvn spring-boot:run
```

```bash
cd frontend
npm install
npm start
```

## Current Environment Note
If `mvnd` fails with permission error on `.m2/mvnd`, use Maven CLI (`mvn test`) or IDE test runner.
