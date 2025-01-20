import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Friend } from '../../models/dto/friend.model'; // Path to your Friend model
import { ApiResponse } from '../../models/reponse/api-response.model'; // Path to your ApiResponse model

@Injectable({
  providedIn: 'root',
})
export class FriendService {
  private readonly friendsUrl: string = 'https://localhost:8443/api/friends'; // Adjust to your API base URL
  private readonly addFriendToChannelUrl: string =
    'https://localhost:8443/api/channels/addFriends'; // Adjust to your API base URL

  constructor(private http: HttpClient) {}

  // Get friend list for a specific user
  getFriendList(): Observable<ApiResponse<Friend[]>> {
    return this.http.get<ApiResponse<Friend[]>>(this.friendsUrl, {
      withCredentials: true, // Send credentials (cookies, auth headers)
    });
  }

  addFriendToChannel(channelId: number, friendId: number): Observable<any> {
    const url = `${this.addFriendToChannelUrl}/${channelId}`; // Use the channelId in the URL
    const body = { friendId: friendId }; // Request body
    return this.http.post<any>(url, body, {
      withCredentials: true, // Send credentials (cookies, auth headers)
    }); // POST request to add friend
  }
}
