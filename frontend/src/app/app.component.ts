import { Component } from '@angular/core';
import { NavigationCancel, NavigationEnd, NavigationError, NavigationStart, Router } from '@angular/router';
import { AuthService } from './services/auth.service';
import { UiStateService } from './services/ui-state.service';

@Component({
  selector: 'app-root',
  template: `
  <nav class="navbar navbar-expand-lg navbar-dark rwf-navbar">
    <div class="container">
      <a class="navbar-brand d-flex align-items-center gap-2" [routerLink]="auth.isLoggedIn() ? '/dashboard' : '/'">
        <img src="assets/rwf-logo.svg" alt="RevWorkforce" class="rwf-logo">
        <span>RevWorkforce</span>
      </a>

      <div class="navbar-nav" *ngIf="auth.isLoggedIn()">
        <a class="nav-link" routerLink="/dashboard">Dashboard</a>
        <a class="nav-link" routerLink="/leaves">Leaves</a>
        <a class="nav-link" routerLink="/performance">Performance</a>
        <a class="nav-link" routerLink="/goals">Goals</a>
        <a class="nav-link" *ngIf="auth.role() !== 'EMPLOYEE'" routerLink="/directory">Directory</a>
        <a class="nav-link" *ngIf="auth.role() === 'ADMIN'" routerLink="/admin">Admin</a>
        <a class="nav-link" href="#" (click)="logout($event)">Logout</a>
      </div>

      <div class="d-flex gap-2 ms-auto" *ngIf="!auth.isLoggedIn()">
        <button class="btn btn-light btn-sm rwf-header-btn" (click)="openHeaderLogin()">Login</button>
      </div>
    </div>
  </nav>

  <div class="scanner-wrap" *ngIf="loading">
    <div class="scanner-line"></div>
  </div>

  <main class="container py-4 page-shell">
    <router-outlet></router-outlet>
  </main>
  `
})
export class AppComponent {
  loading = false;

  constructor(public auth: AuthService, private router: Router, private uiState: UiStateService) {
    this.router.events.subscribe(event => {
      if (event instanceof NavigationStart) {
        this.loading = true;
      }
      if (event instanceof NavigationEnd || event instanceof NavigationCancel || event instanceof NavigationError) {
        setTimeout(() => this.loading = false, 180);
      }
    });
  }

  openHeaderLogin(): void {
    this.router.navigate(['/']).then(() => this.uiState.openLoginPanel());
  }

  logout(event: Event): void {
    event.preventDefault();
    this.auth.logout();
  }
}
