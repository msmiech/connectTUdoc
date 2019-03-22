import {Component, OnInit} from '@angular/core';
import {Router} from '@angular/router';
import {MedicalworkerService} from '../services/medicalworker.service';
import {AccessService} from '../services/access.service';

@Component({
  selector: 'app-login',
  templateUrl: '../views/login.component.html',
  styleUrls: ['../styles/login.component.css']
})
export class LoginComponent implements OnInit {

  showSpinner: boolean;
  showError: boolean;
  textError: string;
  email: string;
  password: string;

  constructor(private router: Router, private accessService: AccessService, private medicalworkerService: MedicalworkerService) {
    this.showSpinner = false;
  }

  ngOnInit() {
    // this.accessService.user = null; // i.e. logout
    this.accessService.logout();
  }

  login(email: string, password: string) {
    this.showSpinner = true;
    this.showError = false;
    this.textError = '';
    this.accessService.login(email, password).then(value => {
      this.showSpinner = false;
      this.showError = false;
      this.router.navigate(['/main']);
    })
      .catch(err => {
        this.showSpinner = false;
        this.showError = true;
        if (err == null) err = 'Error at connection to server!';
        this.textError = err;
        console.log('Error caught at login!', err);
        alert('Login error: ' + err);
      });
  }

}

