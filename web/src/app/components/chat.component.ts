import {Component, EventEmitter, OnDestroy, OnInit, Output} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {ChatService} from '../services/chat.service';
import {ChatMessage} from '../models/chat-message';
import {Patient} from '../models/patient';
import {ChatThread} from '../models/chat-thread';
import {Office} from '../models/office';
import {AngularFireDatabase} from '@angular/fire/database';
import {AccessService} from '../services/access.service';

@Component({
  selector: 'app-chat',
  templateUrl: '../views/chat.component.html',
  styleUrls: ['../styles/chat.component.css']
})
export class ChatComponent implements OnInit, OnDestroy {
  @Output() updateValues = new EventEmitter();


  private _thread: ChatThread;
  private _patient: Patient;
  private _office: Office;
  private _messages: ChatMessage[];

  private _doLoop: boolean;

  constructor(private route: ActivatedRoute, private messagingService: ChatService,
              private database: AngularFireDatabase, private accessService: AccessService) {
    this.route.params.subscribe((params) => {
      console.log('params[\'id\']' + params['id']);
      this.messagingService.getThread(params['id']).subscribe(chatThread => {

        this._thread = chatThread;
        this._patient = chatThread.patient;
        this._office = chatThread.office;

        this.messagingService.dontShowSnackbarFor(this._patient.firstName + ' ' + this._patient.lastName);
        this.messagingService.callBackNewMessage(() => {
          this.loadMessages();
        });

        this.loadMessages();
      });
    });
  }

  ngOnInit() {
    this.loadMessages();
  }

  loadMessages() {
    if (this._thread != null) {
      this.messagingService.getAllMessages(this._thread.id).subscribe(chatMessages => {
        if ((this._messages == null) || (chatMessages.length !== this._messages.length)) {
          this._messages = chatMessages;
          for (let i = 0; i < this._messages.length; i++) {
            if (this._messages[i].patientMessage) {
              this.accessService.decrypt(this._messages[i].message, this._messages[i].senderUid)
                .then((decryptedMessage) => {
                  console.log('encrypted message from patient');
                  this._messages[i].message = decryptedMessage;
                })
                .catch((error) => {
                  console.log('not encrypted message from patient');
                  this._messages[i].message += '\nMessage is not encrypted!';
                });
            }
            else {
              this.accessService.decrypt(this._messages[i].message, this._messages[i].senderUid)
                .then((decryptedMessage) => {
                  console.log('encrypted message from doctor');
                  this._messages[i].message = decryptedMessage;
                })
                .catch((error) => {
                  console.log('not encrypted message from doctor');
                  this._messages[i].message += '\nMessage is not encrypted!';
                });
            }
          }
          this.updateValues.emit();
        }

      });
    }
  }


  get patient(): Patient {
    return this._patient;
  }

  get thread(): ChatThread {
    return this._thread;
  }

  get messages(): ChatMessage[] {
    return this._messages;
  }

  ngOnDestroy(): void {
    this.messagingService.dontShowSnackbarFor('');
    this.messagingService.callBackNewMessage(() => {});
  }
}
