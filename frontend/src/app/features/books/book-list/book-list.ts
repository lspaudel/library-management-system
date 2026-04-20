import { Component, inject, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormControl, ReactiveFormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { debounceTime, distinctUntilChanged } from 'rxjs/operators';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatPaginatorModule, PageEvent } from '@angular/material/paginator';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { Book, BookService } from '../../../core/services/book.service';
import { AuthService } from '../../../core/services/auth.service';

@Component({
  selector: 'app-book-list',
  standalone: true,
  imports: [
    CommonModule, ReactiveFormsModule, RouterModule,
    MatCardModule, MatButtonModule, MatIconModule,
    MatInputModule, MatFormFieldModule, MatPaginatorModule,
    MatProgressSpinnerModule
  ],
  templateUrl: './book-list.html',
  styles: [`
    .header-actions { display: flex; justify-content: space-between; align-items: center; }
    .page-title { font-size: 2.5rem; margin: 0; }
    .search-bar { padding: 1rem 1.5rem 0.2rem; margin-bottom: 2rem; }
    .search-field { width: 100%; margin-bottom: -15px; }
    
    .book-grid {
      display: grid;
      grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
      gap: 2rem;
      position: relative;
    }
    
    .loading-shade {
      display: flex; justify-content: center; align-items: center; min-height: 400px;
    }
    
    .book-card {
      display: flex;
      flex-direction: column;
      height: 100%;
      background: rgba(255, 255, 255, 0.8) !important;
      backdrop-filter: blur(10px);
    }
    .book-card:hover {
      transform: translateY(-5px);
    }
    
    .mat-card-image {
      height: 240px;
      object-fit: cover;
      margin: 0 !important;
      width: 100%;
      border-bottom: 1px solid var(--glass-border);
    }
    
    .category-badge {
      display: inline-block;
      padding: 4px 10px;
      border-radius: 20px;
      background: rgba(79, 70, 229, 0.1);
      color: var(--primary-color);
      font-size: 0.75rem;
      font-weight: 600;
      margin-bottom: 0.5rem;
      text-transform: uppercase;
      letter-spacing: 0.5px;
    }
    
    .book-title { margin: 0 0 0.25rem 0; font-size: 1.25rem; font-weight: 700; white-space: nowrap; overflow: hidden; text-overflow: ellipsis; }
    .book-author { margin: 0; color: var(--text-muted); font-size: 0.95rem; }
    .availability { font-weight: 500; font-size: 0.9rem; }
    .text-success { color: var(--secondary-color); }
    .text-danger { color: var(--accent-color); }
    
    .card-actions {
      margin-top: auto;
      padding: 1rem;
      background: rgba(248, 250, 252, 0.5);
      border-top: 1px solid var(--glass-border);
    }
    
    .empty-state {
      grid-column: 1 / -1;
      text-align: center;
      padding: 4rem 2rem;
      color: var(--text-muted);
    }
    .empty-state mat-icon { font-size: 4rem; width: 4rem; height: 4rem; margin-bottom: 1rem; opacity: 0.5; }
  `]
})
export class BookList implements OnInit {
  private bookService = inject(BookService);
  private authService = inject(AuthService);
  
  isAdmin = this.authService.isAdmin();
  
  books = signal<Book[]>([]);
  isLoading = signal<boolean>(true);
  totalElements = signal<number>(0);
  pageSize = signal<number>(10);
  pageIndex = signal<number>(0);
  
  searchControl = new FormControl('');

  ngOnInit() {
    this.loadBooks();
    
    this.searchControl.valueChanges.pipe(
      debounceTime(400),
      distinctUntilChanged()
    ).subscribe(() => {
      this.pageIndex.set(0);
      this.loadBooks();
    });
  }

  loadBooks() {
    this.isLoading.set(true);
    const q = this.searchControl.value || '';
    
    this.bookService.searchBooks(q, undefined, undefined, this.pageIndex(), this.pageSize())
      .subscribe({
        next: (res) => {
          this.books.set(res.data.content);
          this.totalElements.set(res.data.totalElements);
          this.isLoading.set(false);
        },
        error: () => this.isLoading.set(false)
      });
  }

  onPageChange(event: PageEvent) {
    this.pageIndex.set(event.pageIndex);
    this.pageSize.set(event.pageSize);
    this.loadBooks();
  }
}
