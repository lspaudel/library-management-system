import { Component, inject, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatCardModule } from '@angular/material/card';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatListModule } from '@angular/material/list';
import { BaseChartDirective } from 'ng2-charts';
import { ChartConfiguration, ChartData, ChartType } from 'chart.js';
import { DashboardService, DashboardStats } from '../../../core/services/dashboard.service';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [
    CommonModule, MatCardModule, MatIconModule, MatButtonModule, 
    MatProgressSpinnerModule, MatListModule, BaseChartDirective
  ],
  templateUrl: './dashboard.html',
  styles: [`
    .header-actions { display: flex; justify-content: space-between; align-items: center; }
    .page-title { font-size: 2.5rem; margin: 0; }
    .mt-4 { margin-top: 2rem; }
    .p-4 { padding: 1.5rem; }
    
    .loading-shade { display: flex; justify-content: center; align-items: center; min-height: 50vh; }
    
    .kpi-grid {
      display: grid;
      grid-template-columns: repeat(auto-fit, minmax(240px, 1fr));
      gap: 1.5rem;
    }
    
    .kpi-card {
      display: flex;
      align-items: center;
      gap: 1.5rem;
      padding: 1.5rem;
      transition: var(--transition);
      border-left: 5px solid transparent;
    }
    .kpi-card:hover { transform: translateY(-3px); }
    
    .kpi-books { border-left-color: var(--primary-color); }
    .kpi-members { border-left-color: #0ea5e9; } /* Sky */
    .kpi-borrows { border-left-color: var(--secondary-color); }
    .kpi-overdue { border-left-color: var(--accent-color); }
    
    .kpi-icon {
      width: 64px;
      height: 64px;
      border-radius: 50%;
      background: rgba(79, 70, 229, 0.1);
      color: var(--primary-color);
      display: flex;
      align-items: center;
      justify-content: center;
    }
    .kpi-icon mat-icon { font-size: 32px; width: 32px; height: 32px; }
    
    .kpi-books .kpi-icon { background: rgba(79, 70, 229, 0.1); color: var(--primary-color); }
    .kpi-members .kpi-icon { background: rgba(14, 165, 233, 0.1); color: #0ea5e9; }
    .kpi-borrows .kpi-icon { background: rgba(16, 185, 129, 0.1); color: var(--secondary-color); }
    .kpi-overdue .kpi-icon { background: rgba(244, 63, 94, 0.1); color: var(--accent-color); }
    
    .kpi-details h3 { margin: 0 0 0.25rem 0; color: var(--text-muted); font-size: 0.95rem; font-weight: 500; text-transform: uppercase; letter-spacing: 0.5px; }
    .kpi-details .kpi-value { font-size: 2.2rem; font-weight: 800; font-family: 'Outfit', sans-serif; line-height: 1; color: var(--text-main); }
    
    .charts-grid {
      display: grid;
      grid-template-columns: 1fr 1fr;
      gap: 1.5rem;
    }
    
    @media (max-width: 900px) {
      .charts-grid { grid-template-columns: 1fr; }
    }
    
    .chart-container, .recent-borrows-container {
      padding: 1.5rem;
      display: flex;
      flex-direction: column;
    }
    .chart-container h3, .recent-borrows-container h3 { margin-top: 0; margin-bottom: 1.5rem; font-size: 1.25rem; font-family: 'Outfit', sans-serif; }
    
    .chart-wrapper {
      flex: 1;
      position: relative;
      min-height: 300px;
      display: flex;
      justify-content: center;
      align-items: center;
    }
    
    .empty-state { text-align: center; color: var(--text-muted); padding: 2rem; }
    .empty-state mat-icon { font-size: 3rem; width: 3rem; height: 3rem; opacity: 0.5; margin-bottom: 0.5rem; }
  `]
})
export class Dashboard implements OnInit {
  private dashboardService = inject(DashboardService);
  
  stats = signal<DashboardStats | null>(null);
  isLoading = signal<boolean>(true);

  public pieChartOptions: ChartConfiguration['options'] = {
    responsive: true,
    plugins: {
      legend: { display: true, position: 'right' }
    }
  };
  
  public pieChartData: ChartData<'pie', number[], string | string[]> = {
    labels: [],
    datasets: [{ data: [] }]
  };

  ngOnInit() {
    this.loadStats();
  }

  loadStats() {
    this.isLoading.set(true);
    this.dashboardService.getStats().subscribe({
      next: (data) => {
        this.stats.set(data);
        
        // Populate chart
        if (data.categoryDistribution && data.categoryDistribution.length > 0) {
          this.pieChartData = {
            labels: data.categoryDistribution.map(c => c.categoryName),
            datasets: [{ 
              data: data.categoryDistribution.map(c => c.count),
              backgroundColor: [
                'rgba(79, 70, 229, 0.8)',   // Primary
                'rgba(16, 185, 129, 0.8)',  // Secondary
                'rgba(244, 63, 94, 0.8)',   // Accent
                'rgba(14, 165, 233, 0.8)',  // Sky
                'rgba(245, 158, 11, 0.8)',  // Amber
                'rgba(139, 92, 246, 0.8)',  // Violet
              ]
            }]
          };
        }
        
        this.isLoading.set(false);
      },
      error: () => this.isLoading.set(false)
    });
  }
}
