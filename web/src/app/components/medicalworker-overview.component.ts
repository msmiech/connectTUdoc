import {Component, OnInit} from '@angular/core';
import {AccessService} from '../services/access.service';
import {MedicalworkerService} from '../services/medicalworker.service';
import {MedicalWorker} from '../models/medicalworker';
import {Office} from '../models/office';
import {Address} from '../models/address';
import {OfficeHour} from '../models/office-hour';
import {Appointment} from '../models/appointment';
import {OfficeService} from '../services/office.service';

@Component({
  selector: 'app-medicalworker-overview',
  templateUrl: '../views/medicalworker-overview.component.html'
})
export class MedicalworkerOverviewComponent implements OnInit {

  officeModel: Office;
  accessService: AccessService;
  public errorMsg;
  public addInfoMedicalworker = 'Please fill out the existing free medical worker template.';
  public isAddErrorMedicalworker = false;
  public onMedChange = false;

  constructor(accessService: AccessService,
              private _medicalworkerService: MedicalworkerService,
              private _officeService: OfficeService) {
    this.accessService = accessService;
  }

  ngOnInit() {
    this.officeModel = new Office();
    this.officeModel.address = new Address();
    this.officeModel.officehours = new Array<OfficeHour>();
    this.officeModel.officeWorkers = new Array<MedicalWorker>();

    this._officeService.getOfficeOfCurrentLoggedInUser()
      .subscribe(data => this.officeModel = data,
        error => this.errorMsg = error);
  }

  onMedicalWorkerChange(changed: boolean) {
    this.onMedChange = changed;
  }

  onMedicalWorkerDelete(delMedicalWorker: MedicalWorker) {
    console.log('delete medical worker with id: ' + delMedicalWorker.id);
    for (let med of this.officeModel.officeWorkers) {
      console.log(med);
      this.officeModel.officeWorkers = this.officeModel.officeWorkers.filter(obj => obj !== med);
      break;
    }
    this._medicalworkerService.deleteMedicalWorker(delMedicalWorker)
      .subscribe(null, error1 => this.errorMsg = error1);
  }


  onSubmit(objMedicalWorker: MedicalWorker) {
    console.log('MedicalWorker: ' + JSON.stringify(objMedicalWorker));
    console.log('Office: ' + JSON.stringify(this.officeModel));
    if (objMedicalWorker.id == undefined) {
      this._officeService.updateOffice(this.officeModel)
        .subscribe(data => console.log('Success!', data),
          error => console.error('Error!', error));
    } else {
      this._medicalworkerService.updateMedicalWorker(objMedicalWorker)
        .subscribe(data => console.log('Success!', data),
          error => console.error('Error!', error));
    }
  }

  // only for adding to the view
  addMedicalWorker() {
    console.log('add new medical worker');
    for (let oh of this.officeModel.officeWorkers) {
      console.log(oh);
      if (!oh.hasOwnProperty('firstName')) {
        console.log('Nothing to do ...');
        this.isAddErrorMedicalworker = true;
        return;
      }
    }
    this.isAddErrorMedicalworker = false;

    this.officeModel.officeWorkers.push(new MedicalWorker());
  }


}
