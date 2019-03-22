import {Component, OnInit} from '@angular/core';
import {Router} from '@angular/router';
import {HttpClient, HttpErrorResponse} from '@angular/common/http';
import {AccessService} from '../services/access.service';
import {OfficeService} from '../services/office.service';
import {Office} from '../models/office';
import {Address} from '../models/address';
import {OfficeHour} from '../models/office-hour';
import {Appointment} from '../models/appointment';
import {MedicalWorker} from '../models/medicalworker';
import {MatSnackBar} from '@angular/material';
import {NotificationService} from '../services/notification.service';
import {HelpTextService} from '../services/help-text.service';


@Component({
  selector: 'app-office-management',
  templateUrl: '../views/office-management.component.html',
  styles: [`
    .example-container {
      display: flex;
      flex-direction: column;
    }
  `]



})
export class OfficeManagementComponent implements OnInit {

  public officeModel: Office;
  public errorMsg;
  public officeHourValidChange = false;
  public addInfo = 'Please fill out the existing free office hour template.';
  public isAddError = false;
  private submitMessage = 'Ihre daten wurden erfolgreich übertragen.';

  constructor(private router: Router,
              private http: HttpClient,
              private accessService: AccessService,
              private _officeService: OfficeService,
              private _notifyService: NotificationService,
              public _helpTexts: HelpTextService) {
  }

  ngOnInit() {
    this.officeModel = new Office();
    this.officeModel.address = new Address();
    this.officeModel.officehours = new Array<OfficeHour>();
    this.officeModel.appointments = new Array<Appointment>();
    this.officeModel.officeWorkers = new Array<MedicalWorker>();

    this.getOfficeData();

    console.dir('ngOnInit Office: ' + JSON.stringify(this.officeModel));
  }

  onSubmit() {
    this._officeService.updateOffice(this.officeModel)
      .subscribe(data => console.log('Success!', data, this.notifySuccess()),
        error =>  this.getErrorMessage(error));
  }

  notifySuccess() {
    this._notifyService.notifySuccess(this.submitMessage);
    this.officeHourValidChange = false;
  }

  getErrorMessage(error: string) {
    this.errorMsg = error;
    this._notifyService.getErrorMessage(error);
  }

  getOfficeData() {
    this._officeService.getOfficeOfCurrentLoggedInUser()
      .subscribe(data => {
        this.officeModel = data;
      }, error => {
        this.getErrorMessage(error);
      });
  }

  onOfficeHourChange(changed: boolean) {
    this.officeHourValidChange = changed;
    this.sortOfficeHours();
  }

  onOfficeHourDelete(delOfficeHour: OfficeHour) {
    console.log('delete office hour with id: ' + delOfficeHour.id);
    for (const oh of this.officeModel.officehours) {
      console.log(oh);
      if (oh === delOfficeHour) {
        console.log(oh);
        this.officeModel.officehours = this.officeModel.officehours.filter(obj => obj !== oh);
        break;
      }
    }
  }

  onSortArray() {
    this.sortOfficeHours();
  }

  addOfficeHour() {
    console.log('add new office Hour');
    for (const oh of this.officeModel.officehours) {
      console.log(oh);
      if (!oh.hasOwnProperty('beginTime')) {
        console.log('Nothing to do ...');
        this.isAddError = true;
        return;
      }
    }
    this.isAddError = false;

    this.officeModel.officehours.push(new OfficeHour());

    this.sortOfficeHours();
  }

  private sortOfficeHours(): void {
    this.officeModel.officehours.sort((a: OfficeHour, b: OfficeHour) => {
      return this.dayTypeComparison(a.daytype, b.daytype);
    });
  }

  private dayTypeComparison(dayTypeA: string, dayTypeB: string): number {
    const a = this.convertDayTypeToNumber(dayTypeA);
    const b = this.convertDayTypeToNumber(dayTypeB);
    if (a < b) {
      return -1;
    }
    if (a > b) {
      return 1;
    }
    return 0;
  }

  private convertDayTypeToNumber(dayType: string): number {
    switch (dayType.toLowerCase()) {
      case 'monday':
        return 0;
      case 'tuesday':
        return 1;
      case 'wednesday':
        return 2;
      case 'thursday':
        return 3;
      case 'friday':
        return 4;
      case 'saturday':
        return 5;
      case 'sunday':
        return 6;
    }
  }
}
