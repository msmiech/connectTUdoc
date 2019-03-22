
import { Injectable } from '@angular/core';
import {AccessService} from './access.service';
import {HttpClient} from '@angular/common/http';
import {Appointment} from '../models/appointment';

@Injectable({
  providedIn: 'root'
})
export class AppointmentService {

  readonly APPOINTMENT_PATH: string = '/appointment';
  readonly PATIENT_PATH: string = '/patient';

  constructor(private accessService: AccessService, private http: HttpClient) {
  }


  getAllAppointmentsOfPatient() {
    console.log(this.APPOINTMENT_PATH);

    const patientUrl = this.accessService.buildUrl(this.PATIENT_PATH);
    console.log(patientUrl);
    return this.http.get<any>(patientUrl);
  }

  createAppointment(appointment: Appointment) {
    console.log(this.APPOINTMENT_PATH);
    console.log('Patient name: ' + appointment.patientName);
    console.log(JSON.stringify(appointment));

    return this.accessService.postRequest(this.APPOINTMENT_PATH, appointment);
  }

  updateAppointment(appointment: Appointment) {
    return this.accessService.putRequest(this.APPOINTMENT_PATH, appointment);
  }

  deleteAppointment(id: string | number) {
    return this.accessService.deleteRequest(this.APPOINTMENT_PATH + '/' + id);
  }
}
