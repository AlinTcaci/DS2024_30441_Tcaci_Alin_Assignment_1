import { Injectable } from '@angular/core';
import { Observable, Subject } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class WebSocketService {
  private socket: WebSocket | null = null;
  private messageSubject: Subject<any> = new Subject<any>();

  connect(url: string): void {
    if (this.socket) {
      console.warn('WebSocket is already connected');
      return;
    }

    this.socket = new WebSocket(url);

    this.socket.onopen = () => {
      console.log('WebSocket connection established');
    };

    this.socket.onmessage = (event: MessageEvent) => {
      console.log('Message received from server:', event.data);
      this.messageSubject.next(event.data); // Notify all subscribers
    };

    this.socket.onclose = () => {
      console.log('WebSocket connection closed');
      this.socket = null; // Clean up
    };

    this.socket.onerror = (error) => {
      console.error('WebSocket error occurred:', error);
    };
  }

  getMessages(): Observable<any> {
    return this.messageSubject.asObservable();
  }

  disconnect(): void {
    if (this.socket) {
      this.socket.close();
      this.socket = null;
    }
  }
}
