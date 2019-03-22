import { Component, OnInit } from '@angular/core';
import {ChatService} from '../services/chat.service';
import {ActivatedRoute} from '@angular/router';

@Component({
  selector: 'app-chat-input',
  templateUrl: '../views/chat-input.component.html',
  styleUrls: ['../styles/chat-input.component.css']
})
export class ChatInputComponent implements OnInit {
  private _message: string;
  private _chatThreadID: number;


  constructor(private route: ActivatedRoute, private messagingService: ChatService) {
    this.route.params.subscribe((params) => {
        this._chatThreadID = params['id'];
    });
  }

  ngOnInit() {
  }

  send() {
    console.log('sent to ' + this._chatThreadID + ': ' + this.message);
    this.messagingService.getThread(this._chatThreadID)
      .subscribe((chatThread) =>{
        this.messagingService.sendMessage(this._chatThreadID, this.message, chatThread.patient.uid);
        this.message = '';
    });

  }

  handleSubmit(event) {
    if (event.which === 13) {
      this.send();
    }
  }

  get message(): string {
    return this._message;
  }
  set message(message: string) {
    this._message = message;
  }

}
