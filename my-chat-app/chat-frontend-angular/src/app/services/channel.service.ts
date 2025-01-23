import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, tap } from 'rxjs';
import { ChannelUserEdit } from '../../models/dto/channel-user-edit.model';
import { ApiResponse } from '../../models/reponse/api-response.model';
import { environment } from '../environment/environment';
import { Channel } from '../../models/entity/channel.model';

@Injectable({
  providedIn: 'root',
})
export class ChannelService {
  private readonly channelBaseUrl = environment.apiUrl + '/api/channels';

  constructor(private http: HttpClient) {}

  createChannel(channelName: string): Observable<any> {
    return this.http.post(
      `${this.channelBaseUrl}/create/${channelName}`,
      {},
      { withCredentials: true }
    );
  }

  // Fetch available channels for the user
  getCurrentUserChannels(): Observable<ApiResponse<Channel[]>> {
    return this.http.get<ApiResponse<Channel[]>>(
      `${this.channelBaseUrl}/user/channels`,
      {
        withCredentials: true,
      }
    );
  }

  // Update channel name
  updateChannelName(
    channelId: number,
    newChannelName: string
  ): Observable<ApiResponse<void>> {
    return this.http.post<ApiResponse<void>>(
      `${this.channelBaseUrl}/update-name/${channelId}/${newChannelName}`,
      {}, // No body
      {
        withCredentials: true,
      }
    );
  }

  // Call API to fetch member users in the channel
  getChannelUsers(channelId: number): Observable<any> {
    return this.http
      .get<ApiResponse<ChannelUserEdit[]>>(
        `${this.channelBaseUrl}/users/${channelId}`,
        {
          withCredentials: true,
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
      `${this.channelBaseUrl}/addFriends/${channelId}`,
      requestBody,
      {
        withCredentials: true,
      }
    );
  }

  deleteChannel(channelId: number): Observable<ApiResponse<null>> {
    return this.http.delete<ApiResponse<null>>(
      `${this.channelBaseUrl}/delete/${channelId}`,
      {
        withCredentials: true,
      }
    );
  }

  promoteUserToAdmin(
    channelId: number,
    userIdToPromote: number
  ): Observable<ApiResponse<void>> {
    return this.http.post<ApiResponse<void>>(
      `${this.channelBaseUrl}/promote?channelId=${channelId}&userIdToPromote=${userIdToPromote}`,
      {},
      { withCredentials: true }
    );
  }

  // channel.service.ts
  removeUserFromChannel(
    channelId: number,
    userIdToRemove: number
  ): Observable<ApiResponse<void>> {
    return this.http.delete<ApiResponse<void>>(
      `${this.channelBaseUrl}/remove-user/${channelId}/${userIdToRemove}`,
      { withCredentials: true }
    );
  }
}
