// edit-channel.component.ts
import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Observable } from 'rxjs';
import { ChannelService } from '../services/channel.service';

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
  users$: Observable<any> | undefined; // Observable to hold the users' data

  constructor(
    private route: ActivatedRoute,
    private channelService: ChannelService,
    private router: Router // Inject the Router service
  ) {}

  ngOnInit(): void {
    // Retrieve both 'id' and 'name' from query parameters
    this.route.queryParamMap.subscribe((queryParams) => {
      this.channelId = +queryParams.get('id')!; // Convert to number
      this.channelName = queryParams.get('name')!; // Get the name (if exists)

      console.log('Editing channel with ID:', this.channelId);
      console.log('Channel Name:', this.channelName);

      // Fetch the channel users data from the API
      this.users$ = this.channelService.getChannelUsers(this.channelId);
    });
  }

  changeChannelName(): void {
    const newName = prompt('Enter the new channel name:', this.channelName); // Prompt for new name
    if (newName && newName !== this.channelName) {
      this.channelService.updateChannelName(this.channelId, newName).subscribe({
        next: (response) => {
          alert(response.message); // Show success message
          this.channelName = newName; // Update local channelName
        },
        error: (error) => {
          alert('Error updating channel name: ' + error.error.message); // Show error message
        },
      });
    }
  }

  // Method to delete the channel
  deleteChannel(): void {
    if (confirm('Are you sure you want to delete this channel?')) {
      this.channelService.deleteChannel(this.channelId).subscribe({
        next: (response) => {
          alert(response.message); // Display the response message
          this.router.navigate(['/welcome']);
        },
        error: (error) => {
          alert('Error deleting channel: ' + error.error.message); // Display error message
        },
      });
    }
  }
}
