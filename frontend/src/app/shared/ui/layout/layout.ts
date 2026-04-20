import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { MatSidenavModule } from '@angular/material/sidenav';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatListModule } from '@angular/material/list';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { AuthService } from '../../../core/services/auth.service';

@Component({
  selector: 'app-layout',
  standalone: true,
  imports: [
    CommonModule, 
    RouterModule, 
    MatSidenavModule, 
    MatToolbarModule, 
    MatListModule, 
    MatIconModule, 
    MatButtonModule
  ],
  templateUrl: './layout.html',
  styleUrls: ['./layout.css']
})
export class Layout {
  public authService = inject(AuthService);
  public userSignal = this.authService.currentUser;

  logout() {
    this.authService.logout();
  }
}
