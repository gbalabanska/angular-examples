import { Routes } from '@angular/router';
import { LoginComponent } from './login/login.component';
import { WelcomeComponent } from './welcome/welcome.component';
import { SignupComponent } from './signup/signup.component';
import { AddFriendComponent } from './add-friend/add-friend.component';
import { AddChannelComponent } from './add-channel/add-channel.component';
import { EditChannelComponent } from './edit-channel/edit-channel.component';
import { MessagePageComponent } from './message-page/message-page.component';
import { ChatChannelComponent } from './chat-channel/chat-channel.component';

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
];
