import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, of } from 'rxjs';
import { environment } from '../../../environments/environment';

export interface DashboardStats {
  totalBooks: number;
  totalMembers: number;
  activeBorrows: number;
  overdueBorrows: number;
  totalFinesCollected: number;
  recentBorrows: any[];
  categoryDistribution: {categoryName: string, count: number}[];
}

@Injectable({ providedIn: 'root' })
export class DashboardService {
  private http = inject(HttpClient);

  getStats(): Observable<DashboardStats> {
    return this.http.get<DashboardStats>(`${environment.apiUrl}/dashboard/stats`);
  }
}
