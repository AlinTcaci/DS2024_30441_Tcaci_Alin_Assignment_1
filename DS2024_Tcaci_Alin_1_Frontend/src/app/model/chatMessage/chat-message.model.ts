export class ChatMessage {
  id!: string;
  sender!: string;
  receiver!: string;
  content!: string;
  timestamp!: string;
  readStatus!: boolean;
  readTimestamp?: string;
}
