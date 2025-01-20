import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, tap } from 'rxjs';
import { ChannelUserEdit } from '../../models/dto/channel-user-edit.model';
import { ApiResponse } from '../../models/reponse/api-response.model';

@Injectable({
  providedIn: 'root',
})
export class ChannelService {
  private readonly baseUrl: string = 'https://localhost:8443/api/channels';

  constructor(private http: HttpClient) {}

  // Update channel name
  updateChannelName(
    channelId: number,
    newChannelName: string
  ): Observable<ApiResponse<void>> {
    const requestBody = { newChannelName };
    return this.http.post<ApiResponse<void>>(
      `${this.baseUrl}/update-name/${channelId}`,
      requestBody,
      {
        withCredentials: true, // Send credentials (cookies, auth headers)
      }
    );
  }

  // Call API to fetch users in the channel
  getChannelUsers(channelId: number): Observable<any> {
    return this.http
      .get<ApiResponse<ChannelUserEdit[]>>(
        `${this.baseUrl}/users/${channelId}`,
        {
          withCredentials: true, // Send credentials (cookies, auth headers)
        }
      )
      .pipe(
        // Intercept the response and log the message
        tap((response) => {
          console.log(response.message); // Log the 'message' from the response
        })
      );
  }

  // Add a method to add friends to a channel
  addFriendToChannel(
    channelId: number,
    friendId: number
  ): Observable<ApiResponse<null>> {
    const requestBody = { friendId }; // The request body containing friendId
    return this.http.post<ApiResponse<null>>(
      `${this.baseUrl}/addFriends/${channelId}`,
      requestBody,
      {
        withCredentials: true, // Ensures cookies/auth headers are sent with the request
      }
    );
  }

  // Call API to delete a channel
  deleteChannel(channelId: number): Observable<ApiResponse<null>> {
    return this.http.delete<ApiResponse<null>>(
      `${this.baseUrl}/delete/${channelId}`,
      {
        withCredentials: true,
      }
    );
  }
}
