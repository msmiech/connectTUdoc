import {Person} from './person';
import {Speciality} from './speciality';
import {ChatThread} from './chat-thread';

export class MedicalWorker implements Person {

  eMail: string;
  firstName: string;
  id: number;
  preTitle: string;
  lastName: string;
  posTitle: string;
  privateKey: string;
  publicKey: string;
  specialities: Array<Speciality>;
  chatThreads: Array<ChatThread>;
  type: string;
  uid: string;

  constructor() {
    this.specialities = new Array<Speciality>();
    this.chatThreads = new Array<ChatThread>();
  }
}
