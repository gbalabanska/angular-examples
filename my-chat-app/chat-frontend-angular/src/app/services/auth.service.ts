import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../environment/environment';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  private baseUrl = environment.apiUrl + '/auth'; // Use apiUrl from environment

  constructor(private http: HttpClient) {}

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

  // Clear user info from sessionStorage
  clearUserInfo(): void {
    sessionStorage.removeItem('userId');
    sessionStorage.removeItem('username');
  }
}
