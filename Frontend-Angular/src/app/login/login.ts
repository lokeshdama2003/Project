import { Component, inject } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService, UserRole } from '../services/auth';

@Component({
  selector: 'app-login',
  standalone: true,
  templateUrl: './login.html'
})
export class LoginComponent {
  private authService = inject(AuthService);
  private router = inject(Router);

  chooseRole(role: UserRole): void {
    this.authService.login(role);

    let dashboardPath = '/attendee';

    if (role === 'Admin') {
      dashboardPath = '/admin';
    } else if (role === 'Organizer') {
      dashboardPath = '/organizer';
    }

    void this.router.navigate([dashboardPath]);
  }
}
