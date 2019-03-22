import { Component, OnInit } from '@angular/core';
import {ChatService} from '../services/chat.service';
import {ActivatedRoute} from '@angular/router';
import {ChatThread} from '../models/chat-thread';

@Component({
  selector: 'app-chat-header',
  templateUrl: '../views/chat-header.component.html',
  styleUrls: ['../styles/chat-header.component.css']
})
export class ChatHeaderComponent implements OnInit {
  get chatThread(): ChatThread {
    return this._chatThread;
  }

  set chatThread(value: ChatThread) {
    this._chatThread = value;
  }
  private _chatThreadID: number;
  private _chatThread: ChatThread;


  constructor(private route: ActivatedRoute, private messagingService: ChatService) {
    this.route.params.subscribe((params) => {
        this._chatThreadID = params['id'];
        this.messagingService.getThread(this._chatThreadID).subscribe((chatThread) => {
          this._chatThread = chatThread;
        });
    });
  }

  ngOnInit() {
  }


}
