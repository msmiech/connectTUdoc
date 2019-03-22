import {Component, Input, OnInit} from '@angular/core';
import {AccessService} from '../services/access.service';
import {MatSidenav} from "@angular/material";
import {SidebarComponent} from "./sidebar.component";

@Component({
  selector: 'navbar',
  templateUrl: '../views/navbar.component.html'
})
export class NavbarComponent implements OnInit {

  @Input()
  menuBtnVisible: boolean = true;

  @Input()
  sidebar: SidebarComponent;

  accessService: AccessService;


  constructor(accessService: AccessService) {
    this.accessService = accessService;
  }

  ngOnInit() {

  }

}
