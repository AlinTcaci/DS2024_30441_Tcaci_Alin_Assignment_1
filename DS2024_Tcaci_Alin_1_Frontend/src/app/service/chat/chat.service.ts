import { Injectable } from '@angular/core';

import {map, Observable, Subject} from 'rxjs';
import {ChatMessage} from '../../model/chatMessage/chat-message.model';

import SockJS from 'sockjs-client';
import {Stomp} from "@stomp/stompjs";
import {HttpClient} from '@angular/common/http';



@Injectable({
  providedIn: 'root',
})
export class ChatService {
  // private baseUrl = 'http://localhost:8083/api';
  // private wsUrl = 'http://localhost:8083/ws';
  private baseUrl = 'http://chat.localhost/api'; // Updated to match Traefik route
  private wsUrl = 'http://chat.localhost/ws'; // Updated to match Traefik route

  private stompClient: any;
  private messageSubject = new Subject<ChatMessage>();
  private typingSubject = new Subject<string>(); // Subject for typing notifications
  private messages: ChatMessage[] = []; // Define messages array


  constructor(private http: HttpClient) {
    this.connectToWebSocket();
  }

  connectToWebSocket(): void {
    const socket = new SockJS(this.wsUrl);
    this.stompClient = Stomp.over(socket);

    this.stompClient.connect(
      {},
      (frame: any) => {
        console.log('Connected to WebSocket:', frame);

        // Subscribe to the typing topic
        this.stompClient.subscribe('/topic/typing', (message: any) => {
          const notification: string = message.body;
          this.typingSubject.next(notification);
        });

        // Subscribe to the chat topic
        this.stompClient.subscribe('/topic/messages', (message: any) => {
          const chatMessage: ChatMessage = JSON.parse(message.body);
          this.messageSubject.next(chatMessage); // Push new messages to the Subject
        });

        this.stompClient.subscribe('/topic/readStatus', (message: any) => {
          const messageId: string = message.body;
          const msgToUpdate = this.messages.find(m => m.id === messageId);
          if (msgToUpdate) {
            msgToUpdate.readStatus = true;
          }
        });

        // Handle reconnecting
        this.stompClient.onStompError = (frame: any) => {
          console.error('STOMP connection error:', frame);
          this.connectToWebSocket(); // Attempt to reconnect
        };
      },
      (error: any) => {
        console.error('WebSocket connection error:', error);
        setTimeout(() => this.connectToWebSocket(), 5000); // Retry connection after 5 seconds
      }
    );
  }


  // Send a chat message over WebSocket (sender and receiver are dynamic)
  sendMessageOverWebSocket(chatMessage: ChatMessage): void {
    if (this.stompClient && this.stompClient.connected) {
      this.stompClient.send(
        '/app/sendMessage', // Destination in the backend
        {},
        JSON.stringify(chatMessage)
      );
    } else {
      console.error('WebSocket connection is not established.');
    }
  }

  // Expose WebSocket message stream as an Observable
  getMessagesFromWebSocket(): Observable<ChatMessage> {
    return this.messageSubject.asObservable();
  }

  // HTTP API methods
  getAllMessages(sender: string, receiver: string): Observable<ChatMessage[]> {
    return this.http.get<ChatMessage[]>(`${this.baseUrl}/messages?sender=${sender}&receiver=${receiver}`);
  }


  markMessageAsRead(messageId: string | null | undefined): void {
    if (!messageId) {
      console.warn('Invalid messageId for marking as read:', messageId);
      return; // Exit early if messageId is null or undefined
    }

    this.http.put(`${this.baseUrl}/messages/read/${messageId}`, {}).subscribe({
      next: () => console.log('Message marked as read:', messageId),
      error: (err) => console.error('Error marking message as read:', err),
    });
  }

  getTypingNotifications(): Observable<string> {
    return this.typingSubject.asObservable();
  }

  notifyTyping(sender: string, receiver: string): void {
    if (this.stompClient && this.stompClient.connected) {
      const notification = `${sender} is typing to ${receiver}`;
      this.stompClient.send('/app/typing', {}, notification);
    }
  }

}
