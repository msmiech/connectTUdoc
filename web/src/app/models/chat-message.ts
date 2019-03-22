import {ChatThread} from './chat-thread';
import {ChatAttachment} from './chat-attachment';


export class ChatMessage {
  id: number;
  chatThread: ChatThread;
  createDateTime: string;
  message: string;
  patientMessage: boolean;
  chatAttachment: ChatAttachment;
  senderUid: string;
  chatAttachmentPresent: boolean;
}
