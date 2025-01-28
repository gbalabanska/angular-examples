import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FriendService } from '../../services/friend.service';
import { Friend } from '../../../models/dto/friend.model';
import { Router } from '@angular/router';

@Component({
  selector: 'app-available-chats',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './available-chats.component.html',
  styleUrls: ['./available-chats.component.css'],
})
export class AvailableChatsComponent implements OnInit {
  friendList: Friend[] = [];

  constructor(private friendService: FriendService, private router: Router) {}

  ngOnInit(): void {
    this.getFriendList();
  }

  // Fetch the friend list using FriendService
  getFriendList(): void {
    this.friendService.getFriendList().subscribe({
      next: (response) => {
        this.friendList = response.data || [];
      },
      error: (error) => {
        console.error('Error fetching friend list:', error);
      },
    });
  }

  goToChat(friendId: number, friendUsername: string): void {
    this.router.navigate(['/chat', friendId], {
      queryParams: { friendUsername },
    });
  }
}
