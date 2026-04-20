import { Component, inject, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, RouterModule, Router } from '@angular/router';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatDividerModule } from '@angular/material/divider';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { Book, BookService } from '../../../core/services/book.service';
import { AuthService } from '../../../core/services/auth.service';

@Component({
  selector: 'app-book-detail',
  standalone: true,
  imports: [
    CommonModule, RouterModule, MatButtonModule, 
    MatIconModule, MatProgressSpinnerModule, MatDividerModule,
    MatSnackBarModule
  ],
  templateUrl: './book-detail.html',
  styles: [`
    .mb-4 { margin-bottom: 2rem; }
    .mt-4 { margin-top: 2rem; }
    .my-4 { margin: 2rem 0; }
    .text-primary { color: var(--primary-color); }
    .text-success { color: var(--secondary-color); }
    .text-danger { color: var(--accent-color); }
    
    .loading-shade { display: flex; justify-content: center; align-items: center; min-height: 50vh; }
    
    .book-detail-wrapper {
      display: grid;
      grid-template-columns: 350px 1fr;
      gap: 3rem;
      align-items: start;
    }
    
    @media (max-width: 900px) {
      .book-detail-wrapper { grid-template-columns: 1fr; }
    }
    
    .book-cover-container {
      padding: 1rem;
      border-radius: var(--radius-lg);
      overflow: hidden;
      display: flex;
      justify-content: center;
      background: white;
    }
    
    .book-cover {
      width: 100%;
      height: auto;
      border-radius: var(--radius-md);
      box-shadow: var(--shadow-md);
      object-fit: cover;
    }
    
    .category-badge {
      display: inline-block;
      padding: 6px 14px;
      border-radius: 20px;
      background: rgba(79, 70, 229, 0.1);
      color: var(--primary-color);
      font-size: 0.85rem;
      font-weight: 700;
      margin-bottom: 1rem;
      text-transform: uppercase;
      letter-spacing: 1px;
    }
    
    .book-title {
      font-size: 3rem;
      margin: 0 0 0.5rem 0;
      line-height: 1.1;
    }
    
    .author-name {
      font-size: 1.5rem;
      color: var(--text-muted);
      font-weight: 400;
      margin: 0;
    }
    
    .metadata {
      display: grid;
      grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
      gap: 1rem;
    }
    
    .metadata-item {
      display: flex;
      align-items: center;
      gap: 1rem;
      padding: 1rem 1.5rem;
    }
    
    .metadata-item mat-icon { width: 32px; height: 32px; font-size: 32px; flex-shrink: 0; }
    
    .meta-content { display: flex; flex-direction: column; }
    .meta-label { font-size: 0.8rem; color: var(--text-muted); text-transform: uppercase; letter-spacing: 0.5px; }
    .meta-value { font-weight: 600; font-size: 1.1rem; }
    
    .description p {
      font-size: 1.1rem;
      line-height: 1.7;
      color: var(--text-main);
      opacity: 0.9;
    }
    
    .action-buttons {
      display: flex;
      gap: 1rem;
      align-items: center;
    }
    .spacer { flex: 1; }
  `]
})
export class BookDetail implements OnInit {
  private route = inject(ActivatedRoute);
  private router = inject(Router);
  private bookService = inject(BookService);
  private snackBar = inject(MatSnackBar);
  public authService = inject(AuthService);

  book = signal<Book | null>(null);
  isLoading = signal<boolean>(true);
  isAdmin = this.authService.isAdmin;

  ngOnInit() {
    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      this.loadBook(Number(id));
    }
  }

  loadBook(id: number) {
    this.bookService.getBook(id).subscribe({
      next: (res) => {
        this.book.set(res.data);
        this.isLoading.set(false);
      },
      error: (err) => {
        this.snackBar.open('Failed to load book', 'Close', { duration: 3000 });
        this.router.navigate(['/books']);
        this.isLoading.set(false);
      }
    });
  }

  borrowBook() {
    // Basic interaction simulation - full borrow flow could be an explicit backend call
    this.snackBar.open('Borrow feature is currently limited in demo.', 'OK', {duration: 3000});
  }

  reserveBook() {
    this.snackBar.open('Reservation submitted successfully!', 'OK', {duration: 3000, panelClass: 'success-snackbar'});
  }
}
