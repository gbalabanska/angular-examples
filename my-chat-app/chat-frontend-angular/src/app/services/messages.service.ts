import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../environment/environment';
import { Message } from '../../models/entity/entities.model';

@Injectable({
  providedIn: 'root',
})
export class MessageService {
  private baseUrl = environment.apiUrl + '/api/messages'; // Use apiUrl from environment

  constructor(private http: HttpClient) {}

  // Get messages between current user and friend
  getMessages(friendId: number): Observable<Message[]> {
    return this.http.get<Message[]>(`${this.baseUrl}?friendId=${friendId}`, {
      withCredentials: true, // Include credentials in the request
    });
  }

  // Send a new message
  sendMessage(message: Message): Observable<any> {
    return this.http.post(this.baseUrl, message, {
      withCredentials: true, // Include credentials in the request
    });
  }
}
