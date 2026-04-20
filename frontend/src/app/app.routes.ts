import { Routes } from '@angular/router';
import { Layout } from './shared/ui/layout/layout';
import { authGuard } from './core/guards/auth.guard';

export const routes: Routes = [
  {
    path: 'auth',
    loadChildren: () => import('./features/auth/auth.routes').then(m => m.AUTH_ROUTES)
  },
  {
    path: '',
    component: Layout,
    canActivate: [authGuard],
    children: [
      { path: '', redirectTo: 'books', pathMatch: 'full' },
      { path: 'books', loadChildren: () => import('./features/books/books.routes').then(m => m.BOOKS_ROUTES) },
      // { path: 'dashboard', loadChildren: () => import('./features/dashboard/dashboard.routes').then(m => m.DASHBOARD_ROUTES), canActivate: [adminGuard] },
    ]
  },
  { path: '**', redirectTo: 'books' }
];
