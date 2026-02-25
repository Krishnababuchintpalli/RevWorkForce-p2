import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';

@Component({
  selector: 'app-login',                 // Component selector (HTML tag)
  templateUrl: './login.component.html',  // UI template file
  styleUrls: ['./login.component.css']    // Styling file
})
export class LoginComponent {

  // Reactive form group for login inputs
  loginForm: FormGroup;

  // Flag to check whether form is submitted
  submitted = false;

  constructor(private fb: FormBuilder) {

    // Initialize login form with validation rules
    this.loginForm = this.fb.group({

      // Username / Email field → Required validation
      username: ['', Validators.required],

      // Password field → Required + Minimum length validation
      password: ['', [Validators.required, Validators.minLength(4)]]
    });
  }

  // Getter method → Simplifies form control access in HTML
  get f() {
    return this.loginForm.controls;
  }

  // Method triggered when user clicks Login button
  onLogin() {

    // Mark form as submitted → Enables validation messages
    this.submitted = true;

    // Stop login if form validation fails
    if (this.loginForm.invalid) return;

    // Print login data to console (development/debugging)
    console.log('Login Data:', this.loginForm.value);

    // Temporary success message (replace with API later)
    alert('Login successful (UI Demo)');
  }
}
