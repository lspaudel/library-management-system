import { HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { Router } from '@angular/router';
import { catchError } from 'rxjs/operators';
import { throwError } from 'rxjs';

export const authInterceptor: HttpInterceptorFn = (req, next) => {
  const token = localStorage.getItem('token');
  const router = inject(Router);

  let modifiedReq = req;
  if (token) {
    modifiedReq = req.clone({
      headers: req.headers.set('Authorization', `Bearer ${token}`)
    });
  }

  return next(modifiedReq).pipe(
    catchError(error => {
      if (error.status === 401 && !req.url.includes('/auth/login')) {
        // Handle token refresh logic here or force logout
        localStorage.removeItem('token');
        router.navigate(['/auth/login']);
      }
      return throwError(() => error);
    })
  );
};
