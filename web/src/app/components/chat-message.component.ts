import {Component, Input, OnInit} from '@angular/core';
import {ChatMessage} from '../models/chat-message';
import {saveAs as importedSaveAs} from "file-saver";
import {ChatService} from "../services/chat.service";

@Component({
  selector: 'app-chat-message',
  templateUrl: '../views/chat-message.component.html',
  styleUrls: ['../styles/chat-message.component.css']
})
export class ChatMessageComponent implements OnInit {

  @Input() chatMessage: ChatMessage;

  constructor(private messagingService: ChatService) {
  }

  ngOnInit(chatMessage = this.chatMessage) {
  }

  downloadAttachment() {
    this.messagingService.downloadAttachment(this.chatMessage.id).subscribe(blob => {
      importedSaveAs(blob, this.chatMessage.chatAttachment.fileName);
    });
  }
}


