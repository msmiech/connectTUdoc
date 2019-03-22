import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {OfficeHour} from '../models/office-hour';
import {HelpTextService} from '../services/help-text.service';

@Component({
  selector: 'app-office-hour',
  templateUrl: '../views/office-hour.component.html'
})
export class OfficeHourComponent implements OnInit {

  @Input() officeHour: OfficeHour;
  @Output() officeHourChanged: EventEmitter<boolean> = new EventEmitter<boolean>();
  @Output() onOfficeHourClickDelete: EventEmitter<OfficeHour> = new EventEmitter<OfficeHour>();
  @Output() sortArray: EventEmitter<boolean> = new EventEmitter<boolean>();


  public _dayType = ['MONTAG', 'DIENSTAG', 'MITTWOCH', 'DONNERSTAG', 'FREITAG'];

  constructor(public _helpTexts: HelpTextService
              ) {
  }

  ngOnInit() {}

  someChange(valid: boolean, touched: boolean) {
    console.log('Valid: ' + valid + ' | touched: ' + touched);
    if (valid || (valid && touched)) {
      this.officeHourChanged.emit(true);
    }
  }

  onDeleteOfficeHour() {
    console.log('Delete pressed with id:' + this.officeHour.id);
    this.onOfficeHourClickDelete.emit(this.officeHour);
    this.officeHourChanged.emit(true);
  }

  onSortArray() {
    this.sortArray.emit(true);
  }

}
