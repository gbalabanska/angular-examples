import { Component } from '@angular/core';
import {
  FormBuilder,
  FormGroup,
  Validators,
  ReactiveFormsModule,
} from '@angular/forms';
import { HttpClient } from '@angular/common/http'; // Import HttpClient for HTTP requests
import { FormsModule } from '@angular/forms'; // Import FormsModule

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [ReactiveFormsModule, FormsModule], // Import both FormsModule and ReactiveFormsModule
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css'],
})
export class LoginComponent {
  loginForm: FormGroup;

  private apiUrl = 'https://localhost:8443'; // Replace with your backend URL

  constructor(private fb: FormBuilder, private http: HttpClient) {
    this.loginForm = this.fb.group({
      username: ['', [Validators.required]],
      password: ['', [Validators.required]],
    });
  }

  // Method to submit the form and send the login request
  onSubmit() {
    if (this.loginForm.valid) {
      const username = this.loginForm.value.username;
      const password = this.loginForm.value.password;

      // Send POST request to the backend to generate the token and set the cookie
      this.http
        .post(
          `${this.apiUrl}/auth/generateToken`,
          { username, password },
          {
            withCredentials: true, // Allow cookies (tokens) to be sent/received
          }
        )
        .subscribe({
          next: (response: any) => {
            // Log the success message
            console.log(response);

            // If the backend returns a success message, inform the user that they are logged in
            alert(
              'Login successful! Token has been stored in a secure cookie.'
            );
          },
          error: (error) => {
            // Handle login error (invalid credentials or other issues)
            alert('Login failed. Please check your credentials.');
          },
        });
    }
  }
}
