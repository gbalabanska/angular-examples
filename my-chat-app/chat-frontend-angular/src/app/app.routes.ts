import { Routes } from '@angular/router';
import { LoginComponent } from './authentication/login/login.component';
import { WelcomeComponent } from './welcome/welcome.component';
import { SignupComponent } from './authentication/signup/signup.component';
import { AddChannelComponent } from './channels/add-channel/add-channel.component';
import { ChatChannelComponent } from './channels/chat-channel/chat-channel.component';
import { AddFriendComponent } from './friends/add-friend/add-friend.component';
import { EditChannelComponent } from './channels/edit-channel/edit-channel.component';
import { MessagePageComponent } from './friends/message-page/message-page.component';
import { AddContactsComponent } from './add-contacts/add-contacts.component';

export const routes: Routes = [
  { path: '', redirectTo: 'login', pathMatch: 'full' },
  { path: 'login', component: LoginComponent },
  { path: 'signup', component: SignupComponent },
  { path: 'welcome', component: WelcomeComponent },
  { path: 'addFriend', component: AddFriendComponent },
  { path: 'addChannel', component: AddChannelComponent },
  { path: 'editChannel', component: EditChannelComponent },
  { path: 'chat/:friendId', component: MessagePageComponent },
  { path: 'chatChannel', component: ChatChannelComponent },
  { path: 'logout', component: LoginComponent },
  { path: 'addContacts', component: AddContactsComponent },
];
