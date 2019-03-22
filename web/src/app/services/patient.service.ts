import { Injectable } from '@angular/core';
import {AccessService} from './access.service';
import {Patient} from '../models/patient';
import {Observable} from 'rxjs';

@Injectable()
export class PatientService {

  readonly PATIENT_PATH: string = '/patient';

  constructor(private accessService: AccessService) { }

  // getPatientBulk(patientIDs: number[]): Array<Observable<Patient>> {
  //   // TODO getRequest via accessService with body
  //   let arr = new Array<Observable<Patient>>();
  //   for (let i = 0; i < patientIDs.length; i++) {
  //     arr.push(this.getPatient(patientIDs[i]));
  //   }
  //
  //   //return this.accessService.getRequest<Array<Patient>>(this.PATIENT_PATH, patientIDs);
  // }

  // getPatient(patientID: number): Observable<Patient> {
  //   return this.accessService.getRequest<Patient>(this.PATIENT_PATH + '/' + patientID);
  // }
}
