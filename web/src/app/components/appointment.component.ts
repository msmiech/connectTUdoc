import {Appointment} from '../models/appointment';
import {AppointmentService} from '../services/appointment.service';
import {Router} from '@angular/router';
import {HttpClient} from '@angular/common/http';
import {AccessService} from '../services/access.service';
import {Component, EventEmitter, Inject, Input, OnInit, Output} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from '@angular/material';
import {OfficeService} from '../services/office.service';
import {Patient} from '../models/patient';
import {NotificationService} from '../services/notification.service';
import {FormControl} from '@angular/forms';
import {Observable} from 'rxjs';
import {map, startWith} from 'rxjs/operators';
import {HelpTextService} from '../services/help-text.service';
import {Office} from '../models/office';

@Component({
  selector: 'app-appointment',
  templateUrl: '../views/appointment.component.html',
  styles: [`
    .mat-input-wrapper_name{
        width:300px !important;
    }
    .mat-input-wrapper_temrin{
      width:200px !important;
    }
    .mat-input-wrapper_patienName{
      width:300px !important;
    }
  `]
})
export class AppointmentComponent implements OnInit {

  public appointment: Appointment;
  @Output() onAppointmentSaved:  EventEmitter<boolean> = new EventEmitter<boolean>();
  private patientList: Patient[];
  public errorMsg: string;
  private submitMessage = 'Ihre daten wurden erfolgreich übertragen';
  // public patientNameList: string[]; // = ['Hugo', 'Meier', 'Schwarz', 'Blau'];
  myControl = new FormControl();
  filteredOptions: Observable<Patient[]>;
  // public patientName: string;
  private emptyPatient: Patient;
  public patientDisplayName: string;
  private currentOffice: Office;
  public editView = true;
  public terminTitel: string = 'Termin anlegen'

  constructor(private router: Router,
              private http: HttpClient,
              private accessService: AccessService,
              private _appointmentService: AppointmentService,
              public dialogRef: MatDialogRef<AppointmentComponent>,
              private _officeService: OfficeService,
              private _notifyService: NotificationService,
              public _helpTexts: HelpTextService,
              @Inject(MAT_DIALOG_DATA) public data: any) { }

  ngOnInit() {
    this.appointment = new Appointment();
    this.patientList = new Array();
    this.emptyPatient = new Patient();
    this.currentOffice = new Office();
    this._officeService.getOfficePatients().subscribe(data => this.preparePatientNames(data), error => this.getErrorMessage(error));
    this._officeService.getOfficeOfCurrentLoggedInUser().subscribe(data => this.currentOffice = data, error => this.getErrorMessage(error));
    if (this.data.edit) {
      this.appointment = this.data.appointmentObject;
    } else {
      this._officeService.getOfficePatients().subscribe(data => this.preparePatientNames(data), error => this.getErrorMessage(error));
    }
    this.terminTitel = this.data.title;
  }

  getDisplayOption() {
    // config display settings
    this.editView = this.data.edit;

    return this.editView;
  }

  preparePatientNames(list: Patient[]) {
    console.log('Patients loaded ...');
    // console.log(JSON.stringify(list));

    this.patientList = list;

    this.filteredOptions = this.myControl.valueChanges
      .pipe(
        startWith(''),
        map(value => this._filter(value))
      );
  }

  private _filter(value: string): Patient[] {
    const filterValue = value.toLowerCase();

    return this.patientList.filter(
      option => option.lastName.toLowerCase().includes(filterValue)
      || option.firstName.toLowerCase().includes(filterValue));
  }

  getErrorMessage(error: string) {
    this.errorMsg = error;
    this._notifyService.getErrorMessage(error);
  }

  onSubmit() {
    console.log('OnSubmit before: ' + JSON.stringify(this.appointment));
    console.log('Patient ID: ' + this.appointment.patient.id);
    console.log('PatientName: ' + this.patientDisplayName);
    // no patient was chosen
    if (this.appointment.patient.id === undefined) {
      this.setEmptyPatient();
      console.log('OnSubmit after: ' + JSON.stringify(this.appointment));
    }

    this.appointment.office = this.currentOffice;
    // console.log('Inkl Office.: ' + JSON.stringify(this.appointment));
    if(this.data.edit) {
      this._appointmentService.updateAppointment(this.appointment)
        .subscribe(data => this.notifySuccess(),
          error => this.getErrorMessage(error));
    } else {

      this._appointmentService.createAppointment(this.appointment)
        .subscribe(data => this.notifySuccess(),
          error => this.getErrorMessage(error));
    }
  }

  private setEmptyPatient() {
    this.appointment.patientName = this.patientDisplayName;
    this.appointment.patient = null;
  }

  onClose() {
    this.dialogRef.close();
  }

  private notifySuccess() {
    this._notifyService.notifySuccess(this.submitMessage);
    this.onClose();
  }

  setSelection(patient: Patient) {
    // console.log('selection changed: ' + JSON.stringify(patient));
    this.appointment.patient = patient;
  }
}
