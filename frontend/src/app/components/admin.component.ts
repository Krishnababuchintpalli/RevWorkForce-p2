import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ApiService } from '../services/api.service';

@Component({
  selector: 'app-admin',
  template: `
  <div class="row g-3">
    <div class="col-lg-4">
      <div class="card p-3 mb-3">
        <h6>Add Employee</h6>
        <form class="rwf-form" [formGroup]="empForm" (ngSubmit)="saveEmployee()">
          <input class="form-control mb-2" formControlName="employeeId" placeholder="Employee ID">
          <input class="form-control mb-2" formControlName="fullName" placeholder="Full Name">
          <input class="form-control mb-2" formControlName="email" placeholder="Email">
          <input type="password" class="form-control mb-2" formControlName="password" placeholder="Password">
          <select class="form-select mb-2" formControlName="role">
            <option value="EMPLOYEE">Employee</option>
            <option value="MANAGER">Manager</option>
            <option value="ADMIN">Admin</option>
          </select>
          <div class="text-danger small mb-2" *ngIf="createEmpError">{{ createEmpError }}</div>
          <button class="btn btn-sm btn-primary w-100">Create Employee</button>
        </form>
      </div>

      <div class="card p-3 mb-3">
        <h6>Add Department</h6>
        <form class="rwf-form" [formGroup]="depForm" (ngSubmit)="saveDepartment()">
          <input class="form-control mb-2" formControlName="name">
          <button class="btn btn-sm btn-primary">Save</button>
        </form>
      </div>
      <div class="card p-3 mb-3">
        <h6>Add Designation</h6>
        <form class="rwf-form" [formGroup]="desForm" (ngSubmit)="saveDesignation()">
          <input class="form-control mb-2" formControlName="name">
          <button class="btn btn-sm btn-primary">Save</button>
        </form>
      </div>
      <div class="card p-3">
        <h6>Add Announcement</h6>
        <form class="rwf-form" [formGroup]="annForm" (ngSubmit)="saveAnnouncement()">
          <input class="form-control mb-2" formControlName="title" placeholder="Title">
          <textarea class="form-control mb-2" formControlName="content" placeholder="Content"></textarea>
          <button class="btn btn-sm btn-primary">Publish</button>
        </form>
      </div>
    </div>
    <div class="col-lg-8">
      <div class="card p-3 mb-3">
        <h6>Leave Approvals</h6>
        <div *ngIf="teamLeaves.length === 0" class="text-muted">No leave requests.</div>
        <div *ngFor="let l of teamLeaves" class="border rounded p-2 mb-2">
          <div><strong>{{ l.employee?.fullName }}</strong> ({{ l.employee?.employeeId }})</div>
          <div>{{ l.leaveType }}: {{ l.startDate }} to {{ l.endDate }}</div>
          <div>Reason: {{ l.reason }}</div>
          <div class="mb-2">Status: {{ l.status }}</div>
          <textarea class="form-control mb-2"
                    [ngModel]="leaveComments[l.id] || ''"
                    (ngModelChange)="leaveComments[l.id] = $event"
                    [ngModelOptions]="{standalone: true}"
                    placeholder="Comment (required for reject)"></textarea>
          <div class="d-flex gap-2" *ngIf="l.status === 'PENDING'">
            <button class="btn btn-sm btn-success" (click)="approveLeave(l.id)">Approve</button>
            <button class="btn btn-sm btn-outline-danger" (click)="rejectLeave(l.id)">Reject</button>
          </div>
        </div>
        <div class="text-danger small" *ngIf="leaveError">{{ leaveError }}</div>
      </div>

      <div class="card p-3 mb-3">
        <h6>Employees</h6>
        <div class="table-responsive">
          <table class="table table-sm align-middle">
            <thead><tr><th>ID</th><th>Name</th><th>Role</th><th>Status</th><th>Action</th></tr></thead>
            <tbody>
              <tr *ngFor="let u of users">
                <td>{{ u.employeeId }}</td>
                <td>{{ u.fullName }}</td>
                <td>{{ u.role }}</td>
                <td>
                  <span class="badge" [class.bg-success]="u.active" [class.bg-secondary]="!u.active">
                    {{ u.active ? 'Active' : 'Inactive' }}
                  </span>
                </td>
                <td>
                  <button class="btn btn-sm btn-outline-primary" *ngIf="u.active" (click)="setActive(u.id, false)">Deactivate</button>
                  <button class="btn btn-sm btn-outline-success" *ngIf="!u.active" (click)="setActive(u.id, true)">Activate</button>
                </td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>

      <div class="card p-3 mb-3">
        <h6>Departments</h6>
        <span class="badge bg-light text-dark me-2" *ngFor="let d of departments">{{ d.name }}</span>
      </div>
      <div class="card p-3 mb-3">
        <h6>Designations</h6>
        <span class="badge bg-light text-dark me-2" *ngFor="let d of designations">{{ d.name }}</span>
      </div>
      <div class="card p-3">
        <h6>Announcements</h6>
        <div *ngFor="let a of announcements">
          <strong>{{ a.title }}</strong>
          <p class="mb-2">{{ a.content }}</p>
        </div>
      </div>
    </div>
  </div>
  `
})
export class AdminComponent implements OnInit {
  users: any[] = [];
  departments: any[] = [];
  designations: any[] = [];
  announcements: any[] = [];
  teamLeaves: any[] = [];
  createEmpError = '';
  leaveError = '';
  leaveComments: { [key: number]: string } = {};

  empForm: FormGroup;
  depForm: FormGroup;
  desForm: FormGroup;
  annForm: FormGroup;

  constructor(private api: ApiService, private fb: FormBuilder) {
    this.empForm = this.fb.group({
      employeeId: ['', Validators.required],
      fullName: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.minLength(8)]],
      role: ['EMPLOYEE', Validators.required]
    });
    this.depForm = this.fb.group({ name: ['', Validators.required] });
    this.desForm = this.fb.group({ name: ['', Validators.required] });
    this.annForm = this.fb.group({ title: ['', Validators.required], content: ['', Validators.required] });
  }

  ngOnInit(): void { this.load(); }

  load(): void {
    this.api.get<any[]>('/users').subscribe(r => this.users = r);
    this.api.get<any[]>('/leaves/team').subscribe({
      next: r => this.teamLeaves = r,
      error: () => this.teamLeaves = []
    });
    this.api.get<any[]>('/admin/departments').subscribe(r => this.departments = r);
    this.api.get<any[]>('/admin/designations').subscribe(r => this.designations = r);
    this.api.get<any[]>('/admin/announcements').subscribe(r => this.announcements = r);
  }

  saveEmployee(): void {
    if (this.empForm.invalid) return;
    this.api.post('/users', this.empForm.value).subscribe({
      next: () => {
        this.createEmpError = '';
        this.empForm.reset({ role: 'EMPLOYEE' });
        this.load();
      },
      error: (err) => {
        const backendError = err?.error;
        if (backendError?.error) {
          this.createEmpError = backendError.error;
        } else if (backendError && typeof backendError === 'object') {
          const firstMessage = Object.values(backendError)[0] as string;
          this.createEmpError = firstMessage || 'Unable to create employee.';
        } else {
          this.createEmpError = 'Unable to create employee.';
        }
      }
    });
  }

  setActive(id: number, active: boolean): void {
    this.api.patch(`/users/${id}/active?active=${active}`, {}).subscribe(() => this.load());
  }

  saveDepartment(): void {
    if (this.depForm.invalid) return;
    this.api.post('/admin/departments', this.depForm.value).subscribe(() => { this.depForm.reset(); this.load(); });
  }

  saveDesignation(): void {
    if (this.desForm.invalid) return;
    this.api.post('/admin/designations', this.desForm.value).subscribe(() => { this.desForm.reset(); this.load(); });
  }

  saveAnnouncement(): void {
    if (this.annForm.invalid) return;
    this.api.post('/admin/announcements', this.annForm.value).subscribe(() => { this.annForm.reset(); this.load(); });
  }

  approveLeave(id: number): void {
    this.leaveError = '';
    const comment = (this.leaveComments[id] || '').trim();
    this.api.patch(`/leaves/${id}/approve`, { comment }).subscribe({
      next: () => this.load(),
      error: (err) => this.leaveError = err?.error?.error || 'Unable to approve leave.'
    });
  }

  rejectLeave(id: number): void {
    this.leaveError = '';
    const comment = (this.leaveComments[id] || '').trim();
    if (!comment) {
      this.leaveError = 'Reject comment is required.';
      return;
    }
    this.api.patch(`/leaves/${id}/reject`, { comment }).subscribe({
      next: () => this.load(),
      error: (err) => this.leaveError = err?.error?.error || 'Unable to reject leave.'
    });
  }
}

