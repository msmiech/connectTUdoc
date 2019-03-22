import { Injectable } from '@angular/core';
import {AccessService} from './access.service';
import {ChatMessage} from '../models/chatmessage';
import {ChatThread} from '../models/chatthread';
import {Patient} from '../models/patient';
import {Observable} from 'rxjs';

@Injectable()
export class MessagingService {

  readonly CHAT_PATH: string = '/chat';
  readonly CHAT_THREAD_PATH: string = this.CHAT_PATH + '/thread';
  readonly CHAT_MESSAGE_PATH: string = this.CHAT_PATH + '/message';

  private _callBackNewMessage: any;

  constructor(private accessService: AccessService) { }

  getThreads(): Observable<Array<ChatThread>> {
    return this.accessService.getRequest<Array<ChatThread>>(this.CHAT_THREAD_PATH);
  }

  getThread(threadID: number): Observable<ChatThread> {
    return this.accessService.getRequest<ChatThread>(this.CHAT_THREAD_PATH + '/' + threadID);
  }

  getAllMessages(threadID: number): Observable<Array<ChatMessage>> {
    return this.accessService.getRequest<Array<ChatMessage>>(this.CHAT_MESSAGE_PATH + '/' + threadID);
  }

  sendMessage(threadID: number, message: string): Observable<ChatMessage> {
    return this.accessService.postRequest<ChatMessage>(this.CHAT_MESSAGE_PATH + '/' + threadID, message);
  }

  callBackNewMessage(reloadMessages: () => any) {
    this._callBackNewMessage = reloadMessages;
  }
}
