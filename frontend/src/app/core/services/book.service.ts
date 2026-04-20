import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';

export interface Book {
  id: number;
  isbn: string;
  title: string;
  author: { id: number; name: string };
  category: { id: number; name: string };
  quantity: number;
  availableQty: number;
  publishedYear: number;
  imageUrl: string;
  description: string;
}

export interface PaginatedResponse<T> {
  data: {
    content: T[];
    totalElements: number;
    totalPages: number;
    size: number;
    number: number;
  }
}

@Injectable({ providedIn: 'root' })
export class BookService {
  private http = inject(HttpClient);

  searchBooks(q: string = '', categoryId?: number, authorId?: number, page: number = 0, size: number = 10, sortBy: string = 'createdAt', sortDir: string = 'desc'): Observable<any> {
    let params = new HttpParams()
      .set('q', q)
      .set('page', page.toString())
      .set('size', size.toString())
      .set('sortBy', sortBy)
      .set('sortDir', sortDir);
      
    if (categoryId) params = params.set('categoryId', categoryId.toString());
    if (authorId) params = params.set('authorId', authorId.toString());

    return this.http.get<PaginatedResponse<Book>>(`${environment.apiUrl}/books/search`, { params });
  }

  getBook(id: number): Observable<any> {
    return this.http.get<any>(`${environment.apiUrl}/books/${id}`);
  }
  
  createBook(book: any): Observable<any> {
    return this.http.post<any>(`${environment.apiUrl}/books`, book);
  }

  updateBook(id: number, book: any): Observable<any> {
    return this.http.put<any>(`${environment.apiUrl}/books/${id}`, book);
  }

  deleteBook(id: number): Observable<any> {
    return this.http.delete<any>(`${environment.apiUrl}/books/${id}`);
  }
}
