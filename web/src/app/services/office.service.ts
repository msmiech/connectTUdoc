import {Injectable} from '@angular/core';
import {HttpClient, HttpErrorResponse} from '@angular/common/http';
import {Office} from '../models/office';
import {Observable, throwError} from 'rxjs';
import {AccessService} from './access.service';
import {Appointment} from '../models/appointment';
import {Patient} from '../models/patient';
import {Registrationcode} from '../models/registrationcode';

@Injectable()
export class OfficeService {

  readonly OFFICE_PATH: string = '/office';
  readonly LOGGED_USER_OFFICE: string = '/searchOfficeByLoggedInMedicalUser';
  readonly GET_PATIENTS: string = '/officePatients';
  readonly GET_UNCONFIRMED_PATIENTS: string = '/patients/unconfirmed';

  constructor(private accessService: AccessService, private http: HttpClient) {
  }


  getOfficeOfCurrentLoggedInUser(): Observable<Office> {
    console.log('UID: ' + this.accessService.UID);
    return this.accessService.getRequest(this.OFFICE_PATH + this.LOGGED_USER_OFFICE);
  }

  updateOffice(office: Office) {
    office.appointments = new Array<Appointment>();
    return this.accessService.putRequest(this.OFFICE_PATH, office);
  }

  createOffice(office: Office) {
    console.log(this.OFFICE_PATH);

    const officeUrl = this.accessService.buildUrl(this.OFFICE_PATH);
    console.log(officeUrl);
    // return this.http.post<any>(officeUrl, office);
    return this.accessService.postRequest(this.OFFICE_PATH, office);
  }

  getOfficePatients(): Observable<Patient[]> {
    console.log('call get patients ...');
    return this.accessService.getRequest(this.OFFICE_PATH + this.GET_PATIENTS);
  }
  getUnconfirmedPatients(office: Office): Observable<Patient[]> {
    console.log('get unconfirmed patients');
    return this.accessService.getRequest(this.OFFICE_PATH + '/' + office.id + this.GET_UNCONFIRMED_PATIENTS);
  }
  declinePatient(office: Office, patient: Patient) {
    console.log('call decline patients');
    return this.accessService.deleteRequest(this.OFFICE_PATH + '/' + office.id + '/patient/' + patient.id);
  }
  acceptPatient(office: Office, patient: Patient, registrationcode: Registrationcode) {
    console.log('call accept patients');
    return this.accessService.putRequest(this.OFFICE_PATH + '/' + office.id + '/patient/' + patient.id, registrationcode);
  }
}
