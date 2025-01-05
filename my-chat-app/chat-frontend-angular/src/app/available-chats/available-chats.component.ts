import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';

@Component({
  selector: 'app-available-chats',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './available-chats.component.html',
  styleUrl: './available-chats.component.css',
})
export class AvailableChatsComponent {}
