import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {MedicalWorker} from "../models/medicalworker";
import {Speciality} from "../models/speciality";

@Component({
  selector: 'app-medicalworker',
  templateUrl: '../views/medicalworker.component.html',
  styles: [`
    .example-container {
      display: flex;
      flex-direction: column;
    }

    .topBottom {
      margin-bottom: 1em;
      margin-top: 1em;
    }

    .button {
      padding: 1em;
    }

    div a.active {
      color: darkgoldenrod;
    }
  `]
})
export class MedicalworkerComponent implements OnInit {

  @Input() medicalWorker: MedicalWorker;
  @Output() onMedicalWorkerChanged: EventEmitter<boolean> = new EventEmitter<boolean>();
  @Output() onMedicalWorkerClickDelete: EventEmitter<MedicalWorker> = new EventEmitter<MedicalWorker>();
  @Output() onMedicalWorkerUpdate: EventEmitter<MedicalWorker> = new EventEmitter<MedicalWorker>();
  public addInfoSpeciality = "Please fill out the existing free speciality template.";
  public isAddErrorSpeciality = false;
  public onMedChange = false;
  public types = ['DOCTOR', 'ASSISTANT'];

  constructor() {
  }

  ngOnInit() {
  }

  onSubmit() {
    console.log("MedComp: Create/Update med");
    this.onMedicalWorkerUpdate.emit(this.medicalWorker);
    this.onMedChange = false;
  }

  someChange() {
    this.onMedicalWorkerChanged.emit(true);
  }

  onDeleteMedicalworker() {
    console.log("MedComp: Delete pressed with id:" + this.medicalWorker.id);
    this.onMedicalWorkerClickDelete.emit(this.medicalWorker);
  }

  onSpecialityChange(){
    this.onMedChange = true;
  }

  onSpecialityDelete(delSpeciality: Speciality){
    for(let sp of this.medicalWorker.specialities){
      if(sp == delSpeciality){
        console.log(sp)
        this.medicalWorker.specialities = this.medicalWorker.specialities.filter(obj => obj !== sp);
        break;
      }
    }
  }

  addSpeciality() {
    console.log("add new medical worker")
    for (let sp of this.medicalWorker.specialities) {
      if (!sp.hasOwnProperty('specialityName')) {
        console.log("Nothing to do ...");
        this.isAddErrorSpeciality = true;
        return;
      }
    }
    // i'm sure that now there is no existing new template
    this.isAddErrorSpeciality = false;
    this.medicalWorker.specialities.push(new Speciality());

    return;

  }
}


