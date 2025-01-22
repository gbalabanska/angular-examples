import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Friend } from '../../models/dto/friend.model'; // Path to your Friend model
import { ApiResponse } from '../../models/reponse/api-response.model'; // Path to your ApiResponse model
import { environment } from '../environment/environment';
import { User } from '../../models/entity/user.model';

@Injectable({
  providedIn: 'root',
})
export class FriendService {
  private readonly friendsUrl = environment.apiUrl + '/api/friends';
  private readonly addFriendToChannelUrl =
    environment.apiUrl + '/api/channels/addFriends';

  constructor(private http: HttpClient) {}

  //----------------------------ADD FRIEND TO CHANNEL
  // Get friend list for a specific user
  getFriendList(): Observable<ApiResponse<Friend[]>> {
    return this.http.get<ApiResponse<Friend[]>>(this.friendsUrl, {
      withCredentials: true,
    });
  }

  addFriendToChannel(channelId: number, friendId: number): Observable<any> {
    const url = `${this.addFriendToChannelUrl}/${channelId}`; // Use the channelId in the URL
    const body = { friendId: friendId }; // Request body
    return this.http.post<any>(url, body, {
      withCredentials: true,
    });
  }

  //----------------------------ADD FRIEND TO FRIENDLIST
  private readonly apiUrl = environment.apiUrl;

  // Load all users
  loadAllUsers(): Observable<User[]> {
    return this.http.get<User[]>(`${this.apiUrl}/api/users`, {
      withCredentials: true,
    });
  }

  // Search users by query
  searchUsers(username: string): Observable<User> {
    return this.http.get<User>(`${this.apiUrl}/api/users/${username}`, {
      withCredentials: true,
    });
  }

  // Add user to friend list
  addToFriendList(userId: number): Observable<any> {
    return this.http.post<any>(
      `${this.apiUrl}/api/users/addFriend/${userId}`,
      {},
      {
        withCredentials: true,
      }
    );
  }
}
