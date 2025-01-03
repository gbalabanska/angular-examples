import { HttpClient } from '@angular/common/http';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';

@Component({
  selector: 'app-add-channel',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './add-channel.component.html',
  styleUrl: './add-channel.component.css',
})
export class AddChannelComponent {
  channelName: string = '';

  constructor(private http: HttpClient, private router: Router) {}

  createChannel() {
    this.http
      .post(
        `https://localhost:8443/api/channels/create/${this.channelName}`,
        {},
        {
          withCredentials: true,
        }
      )
      .subscribe({
        next: (response: any) => {
          console.log(`channel added: ` + response.message);
          this.router.navigate(['/welcome']);
        },
        error: (err) => {
          console.error('Error adding channel', err);
        },
      });
  }
}
