import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../environment/environment';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  private baseUrl = environment.apiUrl + '/auth';

  constructor(private http: HttpClient) {}

  // Register new user
  signup(user: {
    username: string;
    password: string;
    email: string;
    roles: string;
  }): Observable<any> {
    return this.http.post(`${this.baseUrl}/addNewUser`, user, {
      withCredentials: true,
    });
  }

  // Method to send the login request
  login(username: string, password: string): Observable<any> {
    return this.http.post(
      `${this.baseUrl}/generateToken`,
      { username, password },
      { withCredentials: true }
    );
  }

  logout(): Observable<any> {
    return this.http.post(
      `${this.baseUrl}/logout`,
      {},
      { withCredentials: true }
    );
  }

  // Get the current user's id and username
  getCurrentUser(): Observable<any> {
    return this.http.get<any>(`${this.baseUrl}/currentUser`, {
      withCredentials: true,
    });
  }

  // Save user info in sessionStorage
  saveUserInfo(userId: number, username: string): void {
    sessionStorage.setItem('userId', userId.toString());
    sessionStorage.setItem('username', username);
  }
}
