import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {OfficeHour} from "../models/office-hour";
import {Speciality} from "../models/speciality";

@Component({
  selector: 'app-speciality',
  templateUrl: '../views/speciality.component.html'
})
export class SpecialityComponent implements OnInit {

  @Input() speciality: Speciality;
  @Output() specialityChanged: EventEmitter<boolean> = new EventEmitter<boolean>();
  @Output() onSpecialityClickDelete: EventEmitter<Speciality> = new EventEmitter<Speciality>();

  constructor() { }

  ngOnInit() {
  }

  someChange(){
    this.specialityChanged.emit(true);
  }

  onSpecialityDelete() {
    console.log("delete speciality with id: " + this.speciality.id);
    this.onSpecialityClickDelete.emit(this.speciality);
    this.specialityChanged.emit(true);
  }

}
