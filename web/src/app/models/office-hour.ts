import {Office} from './office';

export class OfficeHour {
  id: number;
  office: Office;
  beginTime: string;
  endTime: string;
  daytype: string;


  constructor() {
    this.id = 0;
    this.daytype = 'MONDAY';
    this.beginTime = '00:00';
    this.endTime = '00:00';
  }
}
