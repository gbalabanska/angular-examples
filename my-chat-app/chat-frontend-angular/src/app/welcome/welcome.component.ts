import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-welcome',
  standalone: true,
  imports: [ReactiveFormsModule, FormsModule, RouterLink],
  templateUrl: './welcome.component.html',
  styleUrls: ['./welcome.component.css'],
})
export class WelcomeComponent implements OnInit {
  welcomeMessage: string = '';
  apiUrl: string = 'https://localhost:8443/auth/welcome'; // Backend endpoint

  constructor(private http: HttpClient) {}

  ngOnInit(): void {
    // Optionally, you can call getWelcomeMessage on initialization or leave it to the button click
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
}
