import {AfterViewChecked, Component, ElementRef, OnInit, ViewChild} from '@angular/core';

@Component({
  selector: 'app-chat-detail',
  templateUrl: '../views/chat-detail.component.html',
  styleUrls: ['../styles/chat-detail.component.css']
})
export class ChatDetailComponent implements OnInit, AfterViewChecked  {
  @ViewChild('scroller') private feedContainer: ElementRef;
  private scroll = false;

  constructor() {  }

  ngOnInit() {
  }

  scrollToBottom($event: any): void {
    this.scroll = true;
  }


  ngAfterViewChecked() {
     if (this.scroll) {
       this.feedContainer.nativeElement.scrollTop
         = this.feedContainer.nativeElement.scrollHeight;
       this.scroll = false;
     }
  }

}
