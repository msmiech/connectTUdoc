import {Office} from './office';
import {Patient} from './patient';

export class Appointment {
  id: string | number;
  office: Office;
  patient: Patient;
  patientName = '';
  appointmentBegin = '';
  appointmentEnd = '';

  constructor() {
    this.patient = new Patient();
  }

  set _appointmentBegin(appointmentBegin: string) {
    this.appointmentBegin = appointmentBegin.replace('T', ' ' );
  }

  get _appointmentBegin() {
    return this.appointmentBegin.replace(' ', 'T');
  }

  set _appointmentEnd(appointmentEnd: string) {
    this.appointmentEnd = appointmentEnd.replace('T', ' ' );
  }

  get _appointmentEnd() {
    return this.appointmentEnd.replace(' ', 'T');
  }
}
