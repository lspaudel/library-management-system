import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { AuthService } from '../../../core/services/auth.service';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [
    CommonModule, ReactiveFormsModule, RouterModule,
    MatFormFieldModule, MatInputModule, MatButtonModule, 
    MatIconModule, MatProgressSpinnerModule, MatSnackBarModule
  ],
  templateUrl: './login.html',
  styles: [`
    .auth-wrapper {
      min-height: 100vh;
      display: flex;
      align-items: center;
      justify-content: center;
      background: linear-gradient(135deg, #f3f4f6, #e5e7eb);
      position: relative;
      overflow: hidden;
    }
    .auth-wrapper::before {
      content: '';
      position: absolute;
      width: 40vw;
      height: 40vw;
      background: var(--primary-light);
      filter: blur(100px);
      border-radius: 50%;
      top: -10vw;
      right: -10vw;
      opacity: 0.5;
      z-index: 0;
    }
    .auth-wrapper::after {
      content: '';
      position: absolute;
      width: 30vw;
      height: 30vw;
      background: var(--secondary-color);
      filter: blur(100px);
      border-radius: 50%;
      bottom: -10vw;
      left: -10vw;
      opacity: 0.3;
      z-index: 0;
    }
    .auth-container {
      width: 100%;
      max-width: 440px;
      padding: 3rem 2.5rem;
      position: relative;
      z-index: 1;
      background: rgba(255, 255, 255, 0.85);
    }
    .auth-header { text-align: center; margin-bottom: 2rem; }
    .auth-header .logo { font-size: 3rem; width: 3rem; height: 3rem; color: var(--primary-color); margin-bottom: 1rem; }
    .auth-header h2 { font-size: 2rem; margin-bottom: 0.5rem; }
    .auth-header p { color: var(--text-muted); }
    .w-100 { width: 100%; }
    .mt-3 { margin-top: 1rem; }
    .mt-4 { margin-top: 1.5rem; }
    .auth-footer { text-align: center; color: var(--text-muted); font-size: 0.9rem; }
    .font-medium { font-weight: 500; text-decoration: none; }
  `]
})
export class Login {
  private fb = inject(FormBuilder);
  private authService = inject(AuthService);
  private router = inject(Router);
  private snackBar = inject(MatSnackBar);

  hidePassword = true;
  isLoading = false;

  loginForm = this.fb.group({
    email: ['', [Validators.required, Validators.email]],
    password: ['', [Validators.required, Validators.minLength(4)]]
  });

  onSubmit() {
    if (this.loginForm.invalid) return;

    this.isLoading = true;
    this.authService.login(this.loginForm.value).subscribe({
      next: () => {
        this.isLoading = false;
        this.snackBar.open('Logged in successfully!', 'Close', { duration: 3000, panelClass: 'success-snackbar' });
        this.router.navigate(['/books']);
      },
      error: (err) => {
        this.isLoading = false;
        const msg = err.error?.message || 'Login failed. Please check credentials.';
        this.snackBar.open(msg, 'Close', { duration: 5000, panelClass: 'error-snackbar' });
      }
    });
  }
}
