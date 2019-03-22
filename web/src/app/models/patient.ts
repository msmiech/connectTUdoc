import {Person} from './person';
import {ChatThread} from './chat-thread';

export class Patient implements Person {

  id: number;
  uid = '';
  lastName = '';
  posTitle = '';
  preTitle = '';
  eMail = '';
  firstName = '';
  privateKey = '';
  publicKey = '';
  svnr = '';
  chatThreads: Array<ChatThread>;

  constructor() {
    this.lastName = '';
    this.firstName = '';
    this.chatThreads = new Array<ChatThread>();
  }


}
