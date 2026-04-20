import { Injectable, signal, computed, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { JwtHelperService } from '@auth0/angular-jwt';
import { tap, catchError } from 'rxjs/operators';
import { Observable, throwError } from 'rxjs';
import { environment } from '../../../environments/environment';

export interface UserToken {
  sub: string;
  role: string;
  exp: number;
}

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private http = inject(HttpClient);
  private router = inject(Router);
  private jwtHelper = new JwtHelperService();

  // Signals for reactive state
  public currentUser = signal<UserToken | null>(null);
  public isAuthenticated = computed(() => this.currentUser() !== null);
  public isAdmin = computed(() => this.currentUser()?.role === 'ROLE_ADMIN');

  constructor() {
    this.checkToken();
  }

  private checkToken() {
    const token = localStorage.getItem('token');
    if (token && !this.jwtHelper.isTokenExpired(token)) {
      this.currentUser.set(this.jwtHelper.decodeToken(token));
    } else {
      this.logout(false);
    }
  }

  login(credentials: any): Observable<any> {
    debugger;
    return this.http.post<any>(`${environment.apiUrl}/auth/login`, credentials).pipe(
      tap(res => {
        const token = res.data.accessToken;
        const refreshToken = res.data.refreshToken;
        localStorage.setItem('token', token);
        localStorage.setItem('refreshToken', refreshToken);
        this.currentUser.set(this.jwtHelper.decodeToken(token));
      })
    );
  }

  register(data: any): Observable<any> {
    return this.http.post<any>(`${environment.apiUrl}/auth/register`, data);
  }

  logout(redirect = true) {
    localStorage.removeItem('token');
    localStorage.removeItem('refreshToken');
    this.currentUser.set(null);
    if (redirect) {
      this.router.navigate(['/auth/login']);
    }
  }
}
