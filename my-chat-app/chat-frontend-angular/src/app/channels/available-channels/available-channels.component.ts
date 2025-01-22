import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { Channel } from '../../../models/entity/channel.model';
import { ChannelService } from '../../services/channel.service';

@Component({
  selector: 'app-available-channels',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './available-channels.component.html',
  styleUrls: ['./available-channels.component.css'],
})
export class AvailableChannelsComponent implements OnInit {
  channels: Channel[] = [];
  constructor(private channelService: ChannelService) {}

  ngOnInit(): void {
    this.fetchChannels();
  }

  // Fetch channels using ChannelService
  fetchChannels(): void {
    this.channelService.getCurrentUserChannels().subscribe({
      next: (response) => {
        this.channels = response.data || []; // Ensure it's always an array, even if response.data is null
      },
      error: (error) => {
        console.error('Error fetching channels:', error);
        this.channels = []; // Set channels to empty array in case of error
      },
    });
  }
}
