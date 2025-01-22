import { Component } from '@angular/core';
import { AuthService } from '../service/auth.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-logout',
  standalone: true,
  templateUrl: './logout.component.html',
  styleUrls: ['./logout.component.css'],
})
export class LogOutComponent {
  constructor(private authService: AuthService, private router: Router) {}

  logOut(): void {
    this.authService.logout().subscribe({
      next: (response: any) => {
        console.log(response.message);
        alert('You have successfully logged out.');
        sessionStorage.clear(); // Clear session data
        this.router.navigate(['/login']); // Redirect to login
      },
      error: (error) => {
        console.error('Logout failed:', error);
        alert('Failed to log out. Please try again.');
      },
    });
  }
}
