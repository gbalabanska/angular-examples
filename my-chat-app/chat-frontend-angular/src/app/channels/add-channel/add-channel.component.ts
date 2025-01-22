import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { ChannelService } from '../../services/channel.service';

@Component({
  selector: 'app-add-channel',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './add-channel.component.html',
  styleUrl: './add-channel.component.css',
})
export class AddChannelComponent {
  channelName: string = '';

  constructor(private channelService: ChannelService, private router: Router) {}

  createChannel() {
    this.channelService.createChannel(this.channelName).subscribe({
      next: (response) => {
        console.log(`Channel added: ` + response.message);
        this.router.navigate(['/welcome']);
      },
      error: (err) => {
        console.error('Error adding channel', err);
      },
    });
  }
}
