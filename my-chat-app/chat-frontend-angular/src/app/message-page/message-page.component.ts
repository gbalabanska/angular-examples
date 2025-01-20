import { CommonModule } from '@angular/common';
import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Message } from '../../models/entity/entities.model';
import { MessageService } from '../services/messages.service';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-message-page',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './message-page.component.html',
  styleUrls: ['./message-page.component.css'],
})
export class MessagePageComponent implements OnInit, OnDestroy {
  messages: Message[] = [];
  newMessage: string = '';
  friendId!: number;
  currentUserId: number | null = null; // Store the current user ID
  private pollingInterval: any = null; // Store the interval reference for polling

  constructor(
    private messageService: MessageService,
    private route: ActivatedRoute
  ) {}

  ngOnInit(): void {
    const friendId = this.route.snapshot.paramMap.get('friendId');
    if (friendId) {
      this.friendId = +friendId; // Retrieve the friendId from the route parameters
      this.loadMessages(this.friendId); // Load messages between the current user and the friend
    }

    // Retrieve the current user ID from sessionStorage
    this.currentUserId = sessionStorage.getItem('userId')
      ? +sessionStorage.getItem('userId')!
      : null;

    // Start polling for new messages every 5 seconds
    this.startPolling();
  }

  // Load messages from the backend
  loadMessages(friendId: number): void {
    this.messageService.getMessages(friendId).subscribe((messages) => {
      this.messages = messages;
    });
  }

  // Send a new message to the friend
  sendMessage(): void {
    if (this.friendId && this.newMessage.trim()) {
      const message: Message = {
        id: 0, // Assuming the backend auto-generates the ID
        senderId: this.currentUserId!, // Use current user ID
        receiverId: this.friendId,
        channelId: null, // Assuming it's a direct message
        messageText: this.newMessage,
        createdAt: new Date().toISOString(), // Use current timestamp in ISO format
      };

      this.messageService.sendMessage(message).subscribe(() => {
        this.loadMessages(this.friendId); // Reload messages after sending
        this.newMessage = ''; // Clear the message input
      });
    }
  }

  // Start polling for new messages every 5 seconds
  startPolling(): void {
    this.pollingInterval = setInterval(() => {
      this.loadMessages(this.friendId); // Reload messages to check for new ones
    }, 5000); // Poll every 5 seconds
  }

  // Stop polling when the component is destroyed
  ngOnDestroy(): void {
    if (this.pollingInterval) {
      clearInterval(this.pollingInterval); // Clear the interval to stop polling
    }
  }
}
