import {ChatThread} from './chatthread';


export class ChatMessage {
  id: number;
  chatThread: ChatThread;
  createDateTime: string;
  message: string;
  patientMessage: boolean;
  attachments: Array<string>;
}
