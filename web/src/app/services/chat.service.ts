import {Injectable} from '@angular/core';
import {AccessService} from './access.service';
import {ChatMessage} from '../models/chat-message';
import {ChatThread} from '../models/chat-thread';
import {Observable} from 'rxjs';
import { AngularFireDatabase } from '@angular/fire/database';
import { AngularFireMessaging } from '@angular/fire/messaging';
import {MatSnackBar} from '@angular/material';
import {Router} from '@angular/router';


@Injectable()
export class ChatService {

  readonly CHAT_PATH: string = '/chat';
  readonly CHAT_THREAD_PATH: string = this.CHAT_PATH + '/thread';
  readonly CHAT_MESSAGE_PATH: string = this.CHAT_PATH + '/message';
  readonly CHAT_ATTACHMENT_PATH: string = this.CHAT_PATH + '/attachment';
  readonly OFFICE_SUBSCRIBE_PATH: string = '/office/subscribe';

  private _callBackNewMessage: any;
  private _callBackUnreadMessages: any;
  private _patientName: string;

  //currentMessage = new BehaviorSubject(null);

  constructor(private accessService: AccessService,
              private angularFireDB: AngularFireDatabase,
              private angularFireMessaging: AngularFireMessaging,
              public snackBar: MatSnackBar,
              private router: Router) {
    this.angularFireMessaging.messaging.subscribe(
      (_messaging) => {
        _messaging.onMessage = _messaging.onMessage.bind(_messaging);
        _messaging.onTokenRefresh = _messaging.onTokenRefresh.bind(_messaging);
      }
    );
  }

  /**
   * request permission for notification from firebase cloud messaging
   *
   * @param userId userId
   */
  requestPermission(userId) {
    this.angularFireMessaging.requestToken.subscribe(
      (token) => {
        this.accessService.postRequestWithTextBody(this.OFFICE_SUBSCRIBE_PATH, token);
      },
      (err) => {
        console.error('Unable to get permission to notify.', err);
      }
    );
  }

  /**
   * hook method when new notification received in foreground
   */
  receiveMessage() {
    this.angularFireMessaging.messages.subscribe(
      (payload) => {
        console.log('new message received. ' + JSON.stringify(payload));
        if (this._callBackUnreadMessages != null) {
          this._callBackUnreadMessages();
        }
        // this.currentMessage.next(payload);
        let myObj = JSON.parse(JSON.stringify(payload));
        if (!myObj.data.notificationBody) {
          // refresh chat!
          console.log('Message without body!');
          if (this._callBackNewMessage != null) {
            this._callBackNewMessage();
          }
        } else {
          if (this._patientName !== myObj.data.notificationTitle) {
            this.snackBar.open(myObj.data.notificationBody, myObj.data.notificationTitle, { duration: 10000 })
              .onAction().subscribe(() => {
                console.log('action clicked!');
                this.getThreads().subscribe((threads) => {
                  console.log('loaded threads!');
                  let thread = threads.find(value1 => value1.patient.firstName + ' ' + value1.patient.lastName == myObj.data.notificationTitle);
                  console.log(JSON.stringify(thread));
                  if (thread != null) {
                    console.log('thread not null! ' + thread.id);
                    this.router.navigate(['/main/chat/' + thread.id]);
                  }
                });
            });
          }
          if (this._callBackNewMessage != null) {
            this._callBackNewMessage();
          }
        }
      });
  }

  getThreads(): Observable<Array<ChatThread>> {
    return this.accessService.getRequest<Array<ChatThread>>(this.CHAT_THREAD_PATH);
  }

  getThread(threadID: number): Observable<ChatThread> {
    return this.accessService.getRequest<ChatThread>(this.CHAT_THREAD_PATH + '/' + threadID);
  }

  getAllMessages(threadID: number): Observable<Array<ChatMessage>> {
    if (this._callBackUnreadMessages != null) {
      this._callBackUnreadMessages();
    }
    return this.accessService.getRequest<Array<ChatMessage>>(this.CHAT_MESSAGE_PATH + '/' + threadID);
  }

  sendMessage(threadID: number, message: string, patientUID: string) {
    console.log('threadID: ' + threadID + ', message: ' + message + ', patientUID: ' + patientUID);
    this.accessService.encrypt(message, [patientUID])
      .then((encryptedMessage) => {
        console.log('encryptedMessage: ' + encryptedMessage);
        this.accessService.postRequest<ChatMessage>(this.CHAT_MESSAGE_PATH + '/' + threadID, encryptedMessage)
          .subscribe((chatMessage) => console.log(JSON.stringify(chatMessage)));
    }).catch((e) => {
      console.error('error at encryption: ' + e);
    });
  }

  callBackNewMessage(callBackNewMessages: () => any) {
    this._callBackNewMessage = callBackNewMessages;
  }

  callBackUnreadMessage(callBackUnreadMessages: () => any) {
    this._callBackUnreadMessages = callBackUnreadMessages;
  }

  dontShowSnackbarFor(patientName: string) {
    this._patientName = patientName;
  }

  downloadAttachment(messageId: number): Observable<Blob> {
    return this.accessService.getRequestWithResponseType(this.CHAT_ATTACHMENT_PATH + '/' + messageId, 'blob');
  }
}
