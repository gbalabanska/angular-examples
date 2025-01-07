import { Component } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { FormsModule } from '@angular/forms';
import { User } from '../../models/user.model';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-add-friend',
  standalone: true,
  imports: [FormsModule, CommonModule],
  templateUrl: './add-friend.component.html',
  styleUrls: ['./add-friend.component.css'],
})
export class AddFriendComponent {
  searchQuery: string = '';
  foundUser: User | null = null;
  users: User[] = [];
  isHoveredUserId: number | null = null;
  private apiUrl = 'https://localhost:8443';

  constructor(private http: HttpClient) {
    this.loadAllUsers();
  }

  loadAllUsers() {
    this.http
      .get<User[]>('https://localhost:8443/api/users', {
        withCredentials: true,
      })
      .subscribe({
        next: (data) => {
          this.users = data;
        },
        error: (err) => {
          console.error('Error loading users', err);
        },
      });
  }

  searchUsers() {
    if (this.searchQuery.trim()) {
      this.http
        .get<User>(`https://localhost:8443/api/users/${this.searchQuery}`, {
          withCredentials: true,
        })
        .subscribe({
          next: (data) => {
            console.log('found user with username: ' + data.username);
            this.foundUser = data;
          },
          error: (err) => {
            if (err.status === 404) {
              console.log('User not found');
              this.foundUser = null; // Hide found user if 404
            } else {
              console.error('Error searching users', err);
            }
          },
        });
    }
  }

  trackByUserId(index: number, user: User): number {
    return user.id;
  }

  addToFriendList(userId: number) {
    this.http
      .post(
        `https://localhost:8443/api/users/addFriend/${userId}`,
        {},
        {
          withCredentials: true,
        }
      )
      .subscribe({
        next: (response: any) => {
          alert(response.message);
          console.log(`User with ID ${userId} added to friend list`);
        },
        error: (err: any) => {
          if (err.status === 400) {
            const errorMessage = err.error.message || 'An error occurred';
            alert(errorMessage); // Alert the user with the message from the server
          } else {
            console.error('Error adding friend', err.message);
          }
        },
      });
  }
}
