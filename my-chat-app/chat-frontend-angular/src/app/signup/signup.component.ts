import { Component } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { ReactiveFormsModule, FormsModule } from '@angular/forms';

@Component({
  selector: 'app-signup',
  standalone: true,
  imports: [ReactiveFormsModule, FormsModule],
  templateUrl: './signup.component.html',
  styleUrls: ['./signup.component.css'],
})
export class SignupComponent {
  user = {
    username: '',
    password: '',
    email: '',
    roles: 'ROLE_USER', // Fixed role
  };

  private apiUrl = 'https://localhost:8443/auth/addNewUser'; // Your backend endpoint

  constructor(private http: HttpClient) {
    console.log('created');
  }

  onSubmit() {
    // Send POST request to the backend
    this.http.post(this.apiUrl, this.user).subscribe({
      next: (response) => {
        console.log('User created successfully!');
        alert('Sign up successful!');
      },
      error: (error) => {
        console.error('There was an error!', error);
        alert('Sign up failed!');
      },
      complete: () => {
        console.log('Request completed');
      },
    });
  }
}
