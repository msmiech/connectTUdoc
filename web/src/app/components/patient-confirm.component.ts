import {Component, EventEmitter, Inject, OnInit, Output} from '@angular/core';
import {Appointment} from '../models/appointment';
import {Patient} from '../models/patient';
import {FormControl} from '@angular/forms';
import {Observable} from 'rxjs';
import {Office} from '../models/office';
import {Router} from '@angular/router';
import {HttpClient} from '@angular/common/http';
import {AccessService} from '../services/access.service';
import {AppointmentService} from '../services/appointment.service';
import {MAT_DIALOG_DATA, MatDialogRef} from '@angular/material';
import {OfficeService} from '../services/office.service';
import {NotificationService} from '../services/notification.service';
import {HelpTextService} from '../services/help-text.service';
import {map, startWith} from 'rxjs/operators';
import {Registrationcode} from '../models/registrationcode';

@Component({
  selector: 'app-appointment',
  templateUrl: '../views/patient-confirm.component.html',
  styles: [`
    .mat-input-wrapper_name {
      width: 300px !important;
    }

    .mat-input-wrapper_temrin {
      width: 200px !important;
    }

    .mat-input-wrapper_patienName {
      width: 300px !important;
    }
  `]
})

export class PatientConfirmComponent implements OnInit {

  private office: Office;
  private patient: Patient;
  private submitMessage = 'Ihre daten wurden erfolgreich übertragen';

  // Template accessible fields
  @Output() onAppointmentSaved: EventEmitter<boolean> = new EventEmitter<boolean>();
  public errorMsg: string;
  // public patientNameList: string[]; // = ['Hugo', 'Meier', 'Schwarz', 'Blau'];
  myControl = new FormControl();

  registrationcode: Registrationcode;

  constructor(private router: Router,
              private http: HttpClient,
              private accessService: AccessService,
              private _appointmentService: AppointmentService,
              public dialogRef: MatDialogRef<PatientConfirmComponent>,
              private _officeService: OfficeService,
              private _notifyService: NotificationService,
              public _helpTexts: HelpTextService,
              @Inject(MAT_DIALOG_DATA) public data: any) {
  }

  ngOnInit() {
    this.office = this.data.office;
    this.patient = this.data.patient;
    this.registrationcode = new Registrationcode();
  }

  getErrorMessage(error: string) {
    this.errorMsg = error;
    this._notifyService.getErrorMessage(error);
  }

  onSubmit() {
    // no patient was chosen
    this._officeService.acceptPatient(this.office, this.patient, this.registrationcode)
      .subscribe(data => {
        this.dialogRef.close();
      }, error => {
        this.getErrorMessage(error);
      });
  }

  onClose() {
    this.dialogRef.close();
  }

  private notifySuccess() {
    this._notifyService.notifySuccess(this.submitMessage);
    this.onClose();
  }
}
