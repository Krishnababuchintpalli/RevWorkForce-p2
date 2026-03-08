import { Component, OnInit } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { ApiService } from '../services/api.service';

@Component({
  selector: 'app-directory',
  template: `
  <div class="card p-3">
    <h5>Employee Directory</h5>
    <div class="d-flex gap-2 mb-2">
      <input class="form-control" placeholder="Search by name or employee ID" [(ngModel)]="query">
      <button class="btn btn-outline-primary" (click)="search()">Search</button>
    </div>
    <table class="table table-sm">
      <thead><tr><th>Employee ID</th><th>Name</th><th>Email</th><th>Department</th><th>Designation</th></tr></thead>
      <tbody>
        <tr *ngFor="let u of users">
          <td>{{ u.employeeId }}</td>
          <td>{{ u.fullName }}</td>
          <td>{{ u.email }}</td>
          <td>{{ u.department || '-' }}</td>
          <td>{{ u.designation || '-' }}</td>
        </tr>
      </tbody>
    </table>
  </div>
  `
})
export class DirectoryComponent implements OnInit {
  users: any[] = [];
  query = '';

  constructor(private api: ApiService, private fb: FormBuilder) {}

  ngOnInit(): void { this.query = 'EMP'; this.search(); }

  search(): void {
    const q = this.query || 'EMP';
    this.api.get<any[]>(`/users/search?q=${encodeURIComponent(q)}`).subscribe(r => this.users = r);
  }
}
