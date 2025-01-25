import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { AvailableChatsComponent } from '../friends/available-chats/available-chats.component';
import { CommonModule } from '@angular/common';
import { AuthService } from '../authentication/service/auth.service';
import { AvailableChannelsComponent } from '../channels/available-channels/available-channels.component';
import { LogOutComponent } from '../authentication/logout/logout.component';

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
    LogOutComponent,
  ],
  templateUrl: './welcome.component.html',
  styleUrls: ['./welcome.component.css'],
})
export class WelcomeComponent implements OnInit {
  welcomeMessage: string = '';
  userId: number | null = null;
  username: string | null = null;
  apiUrl: string = 'https://localhost:8443/auth/welcome';
  selectedTab: string = 'channels'; // default to Channels tab

  // Function to change the selected tab
  selectTab(tab: string) {
    this.selectedTab = tab;
  }

  constructor(private http: HttpClient, private authService: AuthService) {}

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

  // Just for testing purposes
  getWelcomeMessage() {
    this.http.get(this.apiUrl, { withCredentials: true }).subscribe({
      next: (response: any) => {
        console.log('Backend response:', response);
        this.welcomeMessage = response.message || 'Welcome!';
      },
      error: (error) => {
        console.error('Error:', error);
        this.welcomeMessage =
          'Failed to load welcome message. Please check your session.';
      },
    });
  }
}
