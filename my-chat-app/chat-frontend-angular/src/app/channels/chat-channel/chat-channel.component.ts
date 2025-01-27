import {
  Component,
  OnInit,
  OnDestroy,
  ViewChild,
  ElementRef,
  AfterViewChecked,
} from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Message } from '../../../models/entity/entities.model';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { MessageService } from '../../services/messages.service';

@Component({
  selector: 'app-chat-channel',
  imports: [CommonModule, FormsModule],
  templateUrl: './chat-channel.component.html',
  styleUrls: ['./chat-channel.component.css'],
  standalone: true,
})
export class ChatChannelComponent
  implements OnInit, OnDestroy, AfterViewChecked
{
  @ViewChild('messageContainer') messageContainer!: ElementRef; // Add ViewChild to access the message container
  messages: Message[] = [];
  channelId: number = 0;
  currentUserId: number = parseInt(sessionStorage.getItem('userId') || '0', 10);
  newMessage: string = ''; // Variable to hold the new message text
  private pollingInterval: any = null; // Reference for the polling interval

  private shouldScrollToBottom = false; // Flag to determine if we need to scroll

  constructor(
    private route: ActivatedRoute,
    private messageService: MessageService
  ) {}

  ngOnInit(): void {
    this.route.queryParams.subscribe((params) => {
      this.channelId = params['id'];
      this.loadMessages();
      this.startPolling(); // Start polling when the component initializes
    });
  }

  ngAfterViewChecked(): void {
    // Scroll to bottom if flagged
    if (this.shouldScrollToBottom) {
      this.scrollToBottom();
      this.shouldScrollToBottom = false; // Reset the flag
    }
  }

  loadMessages(): void {
    this.messageService.getMessagesForChannel(this.channelId).subscribe({
      next: (response: any) => {
        console.log('Received messages:', response);
        // Extract the 'data' array from the response object
        this.messages = response.data || [];
        this.shouldScrollToBottom = true; // Flag to scroll to bottom after loading
      },
      error: (error) => {
        console.error('Error loading messages:', error);
        this.messages = []; // Reset to an empty array on error
      },
    });
  }

  // Send a new message to the current channel
  sendMessage(): void {
    if (this.newMessage.trim()) {
      const newMessage: Message = {
        senderId: this.currentUserId, // Use the logged-in user ID
        receiverId: null, // Channels don't use receiverId
        channelId: this.channelId, // Use the current channel ID
        messageText: this.newMessage,
        createdAt: new Date().toISOString(), // Current timestamp in ISO format
      };

      this.messageService
        .sendMessageToChannel(this.channelId, newMessage)
        .subscribe({
          next: () => {
            this.loadMessages(); // Reload messages after sending
            this.newMessage = ''; // Clear the input field
          },
          error: (error) => {
            console.error('Error sending message:', error);
          },
        });
    }
    this.shouldScrollToBottom = true; // Set the flag to scroll after sending the message
  }

  // Start polling for new messages every 5 seconds
  startPolling(): void {
    this.pollingInterval = setInterval(() => {
      this.loadMessages(); // Reload messages to check for new ones
    }, 50000);
  }

  // Stop polling when the component is destroyed
  ngOnDestroy(): void {
    if (this.pollingInterval) {
      clearInterval(this.pollingInterval); // Clear the interval to stop polling
    }
  }

  // Scroll to the bottom of the message container
  scrollToBottom(): void {
    if (this.messageContainer) {
      const container = this.messageContainer.nativeElement;
      container.scrollTop = container.scrollHeight;
    }
  }
}
