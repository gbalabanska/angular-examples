import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  private apiUrl: string = 'https://localhost:8443/auth/generateToken';

  constructor(private http: HttpClient) {}

  // Method to authenticate the user and store the token in the HttpOnly cookie
  login(username: string, password: string): Observable<any> {
    const loginData = { username, password };
    return this.http.post<any>(this.apiUrl, loginData, {
      withCredentials: true, // This ensures that cookies are included in the request
    });
  }

  // Method to check if the user is authenticated (you can check the cookie here)
  isAuthenticated(): boolean {
    // Check for token or validate user authentication in some way
    // Since we store the token in an HttpOnly cookie, you can rely on the backend for validation
    return !!document.cookie; // You might have a specific check here, based on your application's logic
  }
}
