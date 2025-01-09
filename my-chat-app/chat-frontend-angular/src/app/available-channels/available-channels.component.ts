import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { Channel } from '../../models/entity/channel.model';

@Component({
  selector: 'app-available-channels',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './available-channels.component.html',
  styleUrls: ['./available-channels.component.css'],
})
export class AvailableChannelsComponent implements OnInit {
  channels: Channel[] = [];

  constructor(private http: HttpClient) {}

  ngOnInit(): void {
    this.fetchChannels();
  }

  fetchChannels(): void {
    this.http
      .get<any>('https://localhost:8443/api/channels/user/channels', {
        withCredentials: true,
      })
      .subscribe({
        next: (response) => {
          this.channels = response.data; // Use 'data' from the API response
        },
        error: (error) => {
          console.error('Error fetching channels:', error);
          this.channels = []; // Set channels to empty array in case of error
        },
      });
  }

  // deleteChannel(channelId: number): void {
  //   this.http
  //     .delete(`https://localhost:8443/api/channels/delete/${channelId}`, {
  //       withCredentials: true,
  //     })
  //     .subscribe({
  //       next: () => {
  //         this.channels = this.channels.filter(
  //           (channel) => channel.channelId !== channelId
  //         );
  //       },
  //       error: (error) => {
  //         console.error('Error deleting channel:', error);
  //       },
  //     });
  // }

  // changeChannelName(channelId: number): void {
  //   const newName = prompt('Enter new channel name:');
  //   if (newName) {
  //     this.http
  //       .post(
  //         `https://localhost:8443/api/channels/update-name/${channelId}`,
  //         { newChannelName: newName },
  //         { withCredentials: true }
  //       )
  //       .subscribe({
  //         next: (response: any) => {
  //           const updatedChannel = this.channels.find(
  //             (channel) => channel.channelId === channelId
  //           );
  //           if (updatedChannel) {
  //             updatedChannel.channelName = newName;
  //           }
  //         },
  //         error: (error) => {
  //           console.error('Error changing channel name:', error);
  //         },
  //       });
  //   }
  // }
}
