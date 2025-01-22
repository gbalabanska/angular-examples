import { Component } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { ReactiveFormsModule, FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../service/auth.service';

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
  constructor(private authService: AuthService, private router: Router) {}

  onSubmit() {
    // Call the signup service to send the POST request
    this.authService.signup(this.user).subscribe({
      next: (response) => {
        console.log('User created successfully!');
        alert('Sign up successful!');
        this.router.navigate(['/login']);
      },
      error: (error: HttpErrorResponse) => {
        console.error('There was an error!', error);

        // Check if the error is a 400 Bad Request and handle it
        if (error.status === 400) {
          alert('User with this username already exists.');
        } else {
          alert('Sign up failed!');
        }
      },
      complete: () => {
        console.log('Request completed');
      },
    });
  }
}
