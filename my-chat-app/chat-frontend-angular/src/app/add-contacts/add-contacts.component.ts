import { Component } from '@angular/core';
import { AddChannelComponent } from '../channels/add-channel/add-channel.component';
import { AddFriendComponent } from '../friends/add-friend/add-friend.component';

@Component({
  selector: 'app-add-contacts',
  standalone: true,
  imports: [AddChannelComponent, AddFriendComponent],
  templateUrl: './add-contacts.component.html',
  styleUrl: './add-contacts.component.css',
})
export class AddContactsComponent {}
