// edit-channel.component.ts
import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Observable } from 'rxjs';
import { Friend } from '../../../models/dto/friend.model';
import { ChannelService } from '../../services/channel.service';
import { FriendService } from '../../services/friend.service';

@Component({
  selector: 'app-edit-channel',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './edit-channel.component.html',
  styleUrls: ['./edit-channel.component.css'],
})
export class EditChannelComponent implements OnInit {
  channelId!: number;
  channelName!: string;
  users$: Observable<any> | undefined;
  friendList: Friend[] = [];

  constructor(
    private route: ActivatedRoute,
    private channelService: ChannelService,
    private friendService: FriendService,
    private router: Router
  ) {}

  ngOnInit(): void {
    // Retrieve channel information
    this.route.queryParamMap.subscribe((queryParams) => {
      this.channelId = +queryParams.get('id')!;
      this.channelName = queryParams.get('name')!;

      // Fetch the channel users data
      this.users$ = this.channelService.getChannelUsers(this.channelId);

      // Fetch the friend list using FriendService
      this.friendService.getFriendList().subscribe({
        next: (response) => {
          this.friendList = response.data || []; // Store friend list
        },
        error: (error) => {
          console.error('Error fetching friend list:', error);
        },
      });
    });
  }

  changeChannelName(): void {
    const newName = prompt('Enter the new channel name:', this.channelName);
    if (newName && newName !== this.channelName) {
      this.channelService.updateChannelName(this.channelId, newName).subscribe({
        next: (response) => {
          alert(response.message);
          this.channelName = newName;
        },
        error: (error) => {
          alert('Error updating channel name: ' + error.error.message);
        },
      });
    }
  }

  addFriendToChannel(friendId: number): void {
    this.channelService.addFriendToChannel(this.channelId, friendId).subscribe({
      next: (response) => {
        alert(response.message);
        this.users$ = this.channelService.getChannelUsers(this.channelId);
      },
      error: (error) => {
        alert('Error adding friend to channel: ' + error.error.message); // Display error message
      },
    });
  }

  deleteChannel(): void {
    if (confirm('Are you sure you want to delete this channel?')) {
      this.channelService.deleteChannel(this.channelId).subscribe({
        next: (response) => {
          alert(response.message);
          this.router.navigate(['/welcome']);
        },
        error: (error) => {
          alert('Error deleting channel: ' + error.error.message);
        },
      });
    }
  }

  promoteToAdmin(userIdToPromote: number): void {
    this.channelService
      .promoteUserToAdmin(this.channelId, userIdToPromote)
      .subscribe({
        next: (response) => {
          alert(response.message);
          this.users$ = this.channelService.getChannelUsers(this.channelId); // Refresh the users list
        },
        error: (error) => {
          alert('Error promoting user to admin: ' + error.error.message);
        },
      });
  }

  removeUser(userIdToRemove: number): void {
    if (
      confirm('Are you sure you want to remove this user from the channel?')
    ) {
      this.channelService
        .removeUserFromChannel(this.channelId, userIdToRemove)
        .subscribe({
          next: (response) => {
            alert(response.message);
            this.users$ = this.channelService.getChannelUsers(this.channelId); // Refresh the user list
          },
          error: (error) => {
            alert('Error removing user from channel: ' + error.error.message);
          },
        });
    }
  }
}
