import { Component, OnInit } from '@angular/core';
import {ChatMessage} from '../model/chatMessage/chat-message.model';
import {ChatService} from '../service/chat/chat.service';
import {DatePipe} from '@angular/common';
import {EncryptionService} from '../service/encryption/encryption.service';
import {User} from '../model/user/user.model';
import {UserService} from '../service/user/user.service';


@Component({
  selector: 'app-chat',
  templateUrl: './chat.component.html',
  styleUrls: ['./chat.component.scss'],
  providers: [DatePipe]
})
export class ChatComponent implements OnInit {
  messages: ChatMessage[] = [];
  messageContent = '';
  sender = ''; // Dynamic sender input
  receiver = ''; // Dynamic receiver input
  users: User[] = [];
  isAdmin = false;
  typingNotification: string = ''; // Add this line for typing notifications


  constructor(
    private chatService: ChatService,
    private datePipe: DatePipe,
    private encryptionService: EncryptionService,
    private userService: UserService) {}

  ngOnInit(): void {
    const encryptedSender = sessionStorage.getItem('username');
    const encryptedRole = sessionStorage.getItem('userRole'); // Assuming role is stored in sessionStorage

    if (encryptedSender) {
      this.sender = this.encryptionService.decrypt(encryptedSender); // Decrypt the sender's username
    }

    if (encryptedRole) {
      const role = this.encryptionService.decrypt(encryptedRole);
      this.isAdmin = role === 'ADMIN'; // Check if the role is Admin
    }

    this.loadUsers();
    this.listenToWebSocketMessages();
    this.listenToTypingNotifications();
  }

  // Fetch all users and subscribe to the Observable
  loadUsers(): void {
    this.userService.getAllUsers().subscribe({
      next: (data: User[]) => {
        this.users = data; // Populate the users list
      },
      error: (err) => {
        console.error('Error fetching users:', err);
        alert('Failed to load users');
      },
    });
  }

  loadMessages(): void {
    console.log('Loading messages for:', this.sender);
    if (this.sender) {
      this.chatService.getAllMessages(this.sender, this.receiver).subscribe({
        next: (data) => {
          this.messages = data;

          // Mark all messages as read when loaded
          this.messages.forEach((message) => {
            if (!message.readStatus && message.receiver === this.sender) {
              this.markMessageAsRead(message.id);
            }
          });
        },
        error: (err) => console.error('Failed to load messages:', err),
      });
    }
  }

  sendMessage(): void {
    if (!this.messageContent.trim() || !this.sender) return;

    console.log('Sending message:', this.messageContent, 'by', this.sender, 'to', this.receiver);

    // Check if the receiver is "All" (admin-only feature)
    if (this.isAdmin && this.receiver === 'All') {
      // Send the message to all users
      this.users.forEach((user) => {
        if (user.username !== this.sender) { // Exclude the admin themselves
          const newMessage = new ChatMessage();
          newMessage.sender = this.sender;
          newMessage.receiver = user.username;
          newMessage.content = this.messageContent;
          newMessage.timestamp = new Date().toISOString();
          newMessage.readStatus = false;

          // Send the message via WebSocket
          this.chatService.sendMessageOverWebSocket(newMessage);
        }
      });
    } else {
      // Regular message to a single receiver
      const newMessage = new ChatMessage();
      newMessage.sender = this.sender;
      newMessage.receiver = this.receiver;
      newMessage.content = this.messageContent;
      newMessage.timestamp = new Date().toISOString();
      newMessage.readStatus = false;

      // Send the message via WebSocket
      this.chatService.sendMessageOverWebSocket(newMessage);
    }

    // Clear the input field
    this.messageContent = '';
  }


  // Listen for incoming WebSocket messages
  listenToWebSocketMessages(): void {
    this.chatService.getMessagesFromWebSocket().subscribe({
      next: (message) => {
        // Only push the message to the chat if it's for the current conversation
        if (
          (message.sender === this.sender && message.receiver === this.receiver) ||
          (message.sender === this.receiver && message.receiver === this.sender)
        ) {
          this.messages.push(message); // Add the new message
          console.log('New message received:', message);

          // Mark message as read if it is for the current user
          if (!message.readStatus && message.receiver === this.sender) {
            this.markMessageAsRead(message.id);
          }
        }
      },
      error: (err) => console.error('WebSocket error:', err),
    });
  }



  formatDate(timestamp: string): string {
    return this.datePipe.transform(timestamp, 'short') || '';  // Return the formatted date
  }

  onReceiverChange(): void {
    this.loadMessages(); // Reload the messages when the receiver changes
  }

  // New method to mark a message as read
  markMessageAsRead(messageId: string): void {
    this.chatService.markMessageAsRead(messageId);
  }

  listenToTypingNotifications(): void {
    this.chatService.getTypingNotifications().subscribe({
      next: (notification: string) => {
        console.log('Typing notification received:', notification);
        this.displayTypingNotification(notification);
      },
      error: (err) => console.error('Error receiving typing notifications:', err),
    });
  }

  notifyTyping(): void {
    if (this.receiver) {
      this.chatService.notifyTyping(this.sender, this.receiver);
    }
  }

  private displayTypingNotification(notification: string): void {
    // Display typing notification logic
    const parts = notification.split(' is typing to ');
    if (parts[1] === this.sender) {
      this.typingNotification = `${parts[0]} is typing...`;
      setTimeout(() => (this.typingNotification = ''), 3000); // Clear notification after 3 seconds
    }
  }

}
