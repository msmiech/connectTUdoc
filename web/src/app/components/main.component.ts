import {Component} from '@angular/core';
import {AccessService} from '../services/access.service';
import {Router} from '@angular/router';
import {HttpClient} from '@angular/common/http';
import {ChatService} from "../services/chat.service";
import {MatSnackBar} from "@angular/material";

@Component({
  selector: 'app-main-page',
  templateUrl: '../views/main.component.html'
})
export class MainComponent {

  // Template accessible fields
  accessService: AccessService;
  // firebase messaging

  public testRestResult;

  constructor(private router: Router, accessService: AccessService,
              private http: HttpClient,
              private chatService: ChatService) {
    this.accessService = accessService;
  }

  ngOnInit(){
    const userId = 'user001';
    this.chatService.requestPermission(userId);
    this.chatService.receiveMessage();
  }

  testRest() {
    const options = {}; // headers: this.getTokenHeader()};
    const url = this.accessService.buildUrl('/dummy');
    console.log('GET ' + url);
    const self = this;
    this.http.get(url, options).toPromise().then(function (res) {
      console.log(res);
      self.testRestResult = res;
    });
  }

  routeToOfficeManagement() {
    this.router.navigate(['/office']);
  }
}
