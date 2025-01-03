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

  // Method to load all users from the API
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

  // Method to search users based on the search query
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
          error: (err) => console.error('Error searching users', err),
        });
    }
  }

  // Method to track by user id
  trackByUserId(index: number, user: User): number {
    return user.id;
  }

  // Method to add a user to the friend list
  addToFriendList(userId: number) {
    this.http
      .post(
        `https://localhost:8443/api/users/addFriend/${userId}`,
        {}, // Empty body, as you're not sending any data here
        {
          withCredentials: true, // Correctly place withCredentials here in the options
        }
      )
      .subscribe({
        next: (response) => {
          console.log(`User with ID ${userId} added to friend list`);
        },
        error: (err) => {
          console.error('Error adding friend', err);
        },
      });
  }

  // Method to show the "Add to Friendlist" option when user hovers
  showAddFriendOption(user: User) {
    this.isHoveredUserId = user.id;
  }

  // Method to hide the "Add to Friendlist" option when user stops hovering
  hideAddFriendOption() {
    this.isHoveredUserId = null;
  }
}
