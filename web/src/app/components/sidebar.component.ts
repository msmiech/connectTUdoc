import {AfterViewChecked, Component, OnInit} from '@angular/core';
import {AccessService} from '../services/access.service';
import {ChatService} from '../services/chat.service';
import {ChatThread} from '../models/chat-thread';
import {getMinimumEventHeightInMinutes} from 'angular-calendar/modules/common/util';

@Component({
  selector: 'sidebar',
  templateUrl: '../views/sidebar.component.html',
  styleUrls: ['../styles/height.component.css']
})
export class SidebarComponent implements OnInit {

  sidebarOpened = true;
  private _threads: Array<ChatThread>;
  private _unreadMessages: number;

  constructor(private accessService: AccessService, private chatService: ChatService) {
    this._unreadMessages = 0;
  }

  ngOnInit() {
    this.reloadUnreadMessages();
    this.chatService.callBackUnreadMessage(() => {
      this.reloadUnreadMessages();
    });
  }

  toggleSidebar() {
    this.sidebarOpened = !this.sidebarOpened;
  }

  getUnreadMessages() {
    if (this._unreadMessages != null) {
      if (this._unreadMessages != 0) {
        return this._unreadMessages;
      }
    }
  }

  reloadUnreadMessages() {
    this.chatService.getThreads().subscribe((threads) => {
      this._threads = threads;
      this._unreadMessages = 0;
      for (let i = 0; i < this._threads.length; i++) {
        this._unreadMessages += this._threads[i].unreadMessages;
      }
    });
  }
}
