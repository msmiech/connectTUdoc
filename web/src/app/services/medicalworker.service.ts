import {Injectable} from '@angular/core';
import {HttpClient, HttpErrorResponse} from '@angular/common/http';
import {Observable, of, throwError} from 'rxjs';
import {Office} from '../models/office';
import {catchError} from 'rxjs/operators';
import {AccessService} from './access.service';
import {MedicalWorker} from '../models/medicalworker';

@Injectable()
export class MedicalworkerService {

  readonly MEDICALWORKER_PATH: string = '/medicalworker';
  readonly MEDICALWORKER_FIRE_PATH: string = this.MEDICALWORKER_PATH + '/fire';

  constructor(private accessService: AccessService, private http: HttpClient) {
  }

  getMedicalWorker() {
    return this.accessService.getRequest<MedicalWorker>(this.MEDICALWORKER_FIRE_PATH);
  }

  updateMedicalWorker(medicalWorker: MedicalWorker) {
    console.log(JSON.stringify(medicalWorker));
    console.log(this.MEDICALWORKER_PATH);

    const medURL = this.accessService.buildUrl(this.MEDICALWORKER_PATH);
    console.log(medURL);
    return this.http.put(medURL, medicalWorker);
  }

  createMedicalWorker(office: Office) {
    console.log(this.MEDICALWORKER_PATH);

    const medURL = this.accessService.buildUrl(this.MEDICALWORKER_PATH);
    console.log(medURL);
    return this.http.put(medURL, office);
  }

  deleteMedicalWorker(medicalWorker: MedicalWorker) {
    console.log('OrigPath: ' + this.MEDICALWORKER_PATH);

    const medicalWorkerURL = this.accessService.buildUrl(this.MEDICALWORKER_PATH + '/' + medicalWorker.id);
    console.log('deleteURL: ' + medicalWorkerURL);
    return this.http.delete(medicalWorkerURL);
  }

  // From https://angular.io/guide/http
  private handleError(error: HttpErrorResponse) {
    if (error.error instanceof ErrorEvent) {
      // A client-side or network error occurred. Handle it accordingly.
      console.error('An error occurred:', error.error.message);
    } else {
      // The backend returned an unsuccessful response code.
      // The response body may contain clues as to what went wrong,
      console.error(
        `Backend returned code ${error.status}, ` +
        `body was: ${error.error}`);
    }
    // return an observable with a user-facing error message
    return throwError(
      'Something bad happened; please try again later.');
  }
}
