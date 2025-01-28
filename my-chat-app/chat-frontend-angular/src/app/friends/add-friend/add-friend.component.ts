import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { User } from '../../../models/entity/user.model';
import { FriendService } from '../../services/friend.service';

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

  constructor(private friendService: FriendService) {
    this.loadAllUsers();
  }

  // Load all users
  loadAllUsers(): void {
    this.friendService.loadAllUsers().subscribe({
      next: (data) => {
        this.users = data;
      },
      error: (err) => {
        console.error('Error loading users', err);
      },
    });
  }

  // Search users
  searchUsers(): void {
    if (this.searchQuery.trim()) {
      this.friendService.searchUsers(this.searchQuery).subscribe({
        next: (data) => {
          console.log('Found user with username:', data.username);
          this.foundUser = data;
        },
        error: (err) => {
          if (err.status === 404) {
            console.log('User not found');
            this.foundUser = null;
          } else {
            console.error('Error searching users', err);
          }
        },
      });
    }
  }

  // Add user to friend list
  addToFriendList(userId: number): void {
    this.friendService.addToFriendList(userId).subscribe({
      next: (response) => {
        alert(response.message);
        console.log(`User with ID ${userId} added to friend list`);
      },
      error: (err) => {
        if (err.status === 400) {
          const errorMessage = err.error.message || 'An error occurred';
          alert(errorMessage);
        } else {
          console.error('Error adding friend', err.message);
        }
      },
    });
  }

  // Track by User ID for *ngFor
  trackByUserId(index: number, user: User): number {
    return user.id;
  }
}
