import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { AvailableChannelsComponent } from '../available-channels/available-channels.component';
import { AvailableChatsComponent } from '../available-chats/available-chats.component';
import { environment } from '../environment/environment';
import { AuthService } from '../services/auth.service';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-welcome',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    FormsModule,
    RouterLink,
    AvailableChannelsComponent,
    AvailableChatsComponent,
  ],
  templateUrl: './welcome.component.html',
  styleUrls: ['./welcome.component.css'],
})
export class WelcomeComponent implements OnInit {
  welcomeMessage: string = '';
  userId: number | null = null;
  username: string | null = null;
  apiUrl: string = 'https://localhost:8443/auth/welcome'; // Backend endpoint
  logoutUrl: string = environment.apiUrl + '/auth/logout';

  constructor(
    private http: HttpClient,
    private router: Router,
    private authService: AuthService
  ) {}

  ngOnInit(): void {
    // Fetch current user details when the component is initialized
    this.authService.getCurrentUser().subscribe({
      next: (response) => {
        this.userId = response.userId;
        this.username = response.username;
        // Store user info in sessionStorage
        if (this.userId && this.username) {
          this.authService.saveUserInfo(this.userId, this.username);
        }
      },
      error: (error) => {
        console.error('Error fetching user data:', error);
      },
    });
  }

  // Method to get the welcome message from the backend
  getWelcomeMessage() {
    // Send GET request to the /auth/welcome endpoint with credentials
    this.http.get(this.apiUrl, { withCredentials: true }).subscribe({
      next: (response: any) => {
        // Log the response to the console
        console.log('Backend response:', response);
        alert(response.message);
        // Assuming the response contains a "message" field
        this.welcomeMessage = response.message || 'Welcome!';
      },
      error: (error) => {
        // Handle error (if the token is invalid or not provided)
        console.error('Error:', error);
        this.welcomeMessage =
          'Failed to load welcome message. Please check your session.';
      },
    });
  }

  // Method to log out the user
  logout() {
    this.http.post(this.logoutUrl, {}, { withCredentials: true }).subscribe({
      next: (response: any) => {
        console.log(response.message);
        alert(response.message);

        // Clear user data from sessionStorage
        this.authService.clearUserInfo();

        // Optionally clear other session data
        sessionStorage.clear(); // This removes all session data

        // Redirect to the login page
        this.router.navigate(['/login']);
      },
      error: (error) => {
        console.error('Logout failed:', error);
        alert('Failed to log out. Please try again.');
      },
    });
  }
}
