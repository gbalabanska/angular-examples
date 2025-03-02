import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../environment/environment';
import { Message } from '../../models/entity/entities.model';

@Injectable({
  providedIn: 'root',
})
export class MessageService {
  private baseUrl = environment.apiUrl + '/api/messages';
  private baseUrlChatChannel = environment.apiUrl + '/api/chat-channel';

  constructor(private http: HttpClient) {}

  // Get messages between current user and friend
  getMessages(friendId: number): Observable<Message[]> {
    return this.http.get<Message[]>(`${this.baseUrl}?friendId=${friendId}`, {
      withCredentials: true,
    });
  }

  // Send a new message to friend
  sendMessage(message: Message): Observable<any> {
    return this.http.post(this.baseUrl, message, {
      withCredentials: true,
    });
  }

  // Get all messages for a specific channel
  getMessagesForChannel(channelId: number): Observable<Message[]> {
    return this.http.get<Message[]>(
      `${this.baseUrlChatChannel}/${channelId}/messages`,
      {
        withCredentials: true,
      }
    );
  }

  // Send a new message to a specific channel
  sendMessageToChannel(channelId: number, message: Message): Observable<any> {
    return this.http.post(
      `${this.baseUrlChatChannel}/${channelId}/messages`,
      message,
      {
        withCredentials: true,
      }
    );
  }
}
