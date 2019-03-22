import {Address} from './address';
import {Appointment} from './appointment';
import {OfficeHour} from './office-hour';
import {MedicalWorker} from './medicalworker';

export class Office {
  id: number;
  name: string;
  phone: string;
  fax: string;
  address: Address;
  email: string;
  officehours: Array<OfficeHour>;
  appointments: Array<Appointment>;
  officeWorkers: Array<MedicalWorker>;
}
