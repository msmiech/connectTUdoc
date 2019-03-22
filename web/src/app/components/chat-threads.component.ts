import { Component, OnInit } from '@angular/core';
import {ChatService} from '../services/chat.service';
import {ChatThread} from '../models/chat-thread';
import {Patient} from '../models/patient';
import {Router} from '@angular/router';
import {Office} from '../models/office';
import {OfficeService} from '../services/office.service';
import {NotificationService} from '../services/notification.service';
import {MatDialog} from '@angular/material';
import {PatientConfirmComponent} from './patient-confirm.component';

@Component({
  selector: 'app-chat-threads',
  templateUrl: '../views/chat-threads.component.html',
  styleUrls: ['../styles/chat-thread.component.css']
})
export class ChatThreadsComponent implements OnInit {

  private _chatThreads: ChatThread[];
  private officeModel: Office;
  private _patients: Patient[];

  constructor(private messagingService: ChatService,
              private _officeService: OfficeService,
              private _notifyService: NotificationService,
              public dialog: MatDialog,
              private router: Router) {
  }

  ngOnInit() {
     console.log('got here, ngOnInit chat.threads');
    this.getThreadsAndPatients();
    this._patients = [];
    this.getOfficeData();
  }

  private getThreadsAndPatients() {

    this.messagingService.getThreads().subscribe((chatThreads => {
      this._chatThreads = chatThreads;
      console.log('got _chatThreads, _chatThreads: ' + this._chatThreads.length);
    }));
  }

  startChat(patient: Patient) {
    console.log('patientID: ', patient.id);
    let chatThread = this._chatThreads.filter((value => {
      return value.patient.id === patient.id;
    }));
    console.log('chatThread: ', chatThread);
    if (chatThread.length >= 1) {
      this.router.navigate(['/main/chat', chatThread[0].id]);
    }
  }

  routeToVideoCall(patient: Patient){
    this.router.navigate(['/main/video-call', patient.id, patient.firstName + " " + patient.lastName]);
  }


  get chatThreads(): ChatThread[] {
    return this._chatThreads;
  }

  get unconfirmedPatients(): Patient[] {
    return this._patients;
  }
  accept(patient: Patient) {
    console.log('accept patient: ', patient);
    const dialogRef = this.dialog.open(PatientConfirmComponent, {
      data: { office: this.officeModel, patient: patient }, disableClose: true, autoFocus: true
    });

    dialogRef.afterClosed().subscribe(result => {
      console.log('The dialog was closed');
      this._patients = [];
      this.retrieveUnconfirmedPatients();
      this.getThreadsAndPatients();
    });
  }
  decline(patient: Patient) {
    console.log('decline patient: ', patient);
    this._officeService.declinePatient(this.officeModel, patient)
      .subscribe( data => {
        this._patients = [];
        this.retrieveUnconfirmedPatients();
      }, error => {
        this.getErrorMessage(error);
      });
  }
  getErrorMessage(error: string) {
    this._notifyService.getErrorMessage(error);
  }

  getOfficeData() {
    this._officeService.getOfficeOfCurrentLoggedInUser()
      .subscribe(data => {
        this.officeModel = data;
        this.retrieveUnconfirmedPatients();
      }, error => {
        this.getErrorMessage(error);
      });
  }
  retrieveUnconfirmedPatients() {
    this._officeService.getUnconfirmedPatients(this.officeModel)
      .subscribe( patientsData => {
        this._patients = patientsData;
      }, errorData => {
        this.getErrorMessage(errorData);
      });
  }
}
