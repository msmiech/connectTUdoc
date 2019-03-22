import {Component, OnInit} from '@angular/core';
import {AccessService} from '../services/access.service';
import {HttpClient, HttpParams} from '@angular/common/http';
import {Subject} from 'rxjs';
import {Appointment} from '../models/appointment';
import {CalendarEvent, CalendarEventAction, CalendarEventTimesChangedEvent, CalendarView} from 'angular-calendar';
import {endOfDay, endOfMonth, endOfWeek, startOfDay, startOfMonth, startOfWeek, subMonths, addMonths, format} from 'date-fns';
import {MatDialog, MatDialogConfig} from '@angular/material';
import {AppointmentComponent} from './appointment.component';
import {Office} from '../models/office';
import {AppointmentService} from '../services/appointment.service';
import {NotificationService} from '../services/notification.service';
import {OfficeService} from '../services/office.service';


const colors: any = {
  red: {
    primary: '#ad2121',
    secondary: '#FAE3E3'
  },
  blue: {
    primary: '#673ab7',
    secondary: '#D1E8FF'
  },
  yellow: {
    primary: '#e3bc08',
    secondary: '#FDF1BA'
  }
};

@Component({
  selector: 'app-appointment-overview',
  templateUrl: '../views/appointment-overview.component.html',
  styleUrls: ['../styles/appointment.compontent.css']
})
export class AppointmentOverviewComponent implements OnInit {

  constructor(accessService: AccessService, private _appointmentService: AppointmentService, private _officeService: OfficeService,
              private _notifyService: NotificationService, private http: HttpClient, public dialog: MatDialog) {
    this.accessService = accessService;
  }

  // Template accessible fields
  accessService: AccessService;
  CalendarView = CalendarView; // CalenderView type reference so you can use it in template

  viewDate: Date = new Date(); // now
  view: CalendarView = CalendarView.Day;
  currentMonth = -1;
  currentOffice: Office;

  // ----- based on example from https://mattlewis92.github.io/angular-calendar/#/kitchen-sink ----
  refresh: Subject<any> = new Subject();


  actions: CalendarEventAction[] = [
    {
      label: '<i class="fa fa-fw fa-times"></i>',
      onClick: ({event}: { event: CalendarEvent }): void => {
        this._appointmentService.deleteAppointment(event.id)
          .subscribe(data => this.events = this.events.filter(iEvent => iEvent !== event),
            error => this._notifyService.getErrorMessage(error));
      }
    },
    {
      label: '<i class="fa fa-fw fa-pencil"></i>',
      onClick: ({event}: { event: CalendarEvent }): void => {
        const appointment = new Appointment();
        appointment.id = event.id;
        appointment.patientName = event.title;
        appointment.appointmentBegin = format(event.start, 'YYYY-MM-DD HH:mm');
        appointment.appointmentEnd = format(event.end, 'YYYY-MM-DD HH:mm');
        this.editAppointment(appointment);
      }
    }
  ];

  events: CalendarEvent[] = [];

  ngOnInit(): void {
    console.log('search for office');
    this._officeService.getOfficeOfCurrentLoggedInUser().subscribe(
      data => this.onSuccess(data) , error => this._notifyService.getErrorMessage(error));
    console.log('fetch data');
  }

  onSuccess(data: Office) {
    this.currentOffice = data;
    this.fetchEvents(false);
  }

  fetchEvents(ignoreCurrentMonth: boolean): void {
    const getStart: any = {
      month: startOfMonth,
      week: startOfWeek,
      day: startOfDay
    }[this.view];

    const getEnd: any = {
      month: endOfMonth,
      week: endOfWeek,
      day: endOfDay
    }[this.view];

    if (!ignoreCurrentMonth && this.currentMonth === this.viewDate.getMonth()) {
      return;
    }
    console.log('after comparison');
    this.currentMonth = this.viewDate.getMonth();
    const params = new HttpParams()
      .set(
        'start',
        format(subMonths(startOfMonth(this.viewDate), 1), 'DD-MM-YYYY')
      )
      .set(
        'end',
        format(endOfMonth(addMonths(this.viewDate, 1)), 'DD-MM-YYYY')
      );
    this.accessService.getRequestWithParams('/appointment/office/' + this.currentOffice.id, params).subscribe((appointments: Appointment[]) => {
      this.events = [];
      appointments.forEach(appointment => {
        if(appointment.patient){
          this.events.push({
            id: appointment.id,
            start: new Date(appointment.appointmentBegin),
            end: new Date(appointment.appointmentEnd),
            title: appointment.patient.firstName + ' ' +appointment.patient.lastName,
            color: colors.blue,
            actions: this.actions,
          });
        } else {
          this.events.push({
            id: appointment.id,
            start: new Date(appointment.appointmentBegin),
            end: new Date(appointment.appointmentEnd),
            title: appointment.patientName,
            color: colors.blue,
            actions: this.actions,
          });
        }
      });
    }
  );
  }
  eventTimesChanged({
                      event,
                      newStart,
                      newEnd
                    }: CalendarEventTimesChangedEvent): void {
    event.start = newStart;
    event.end = newEnd;
    this.handleEvent('Dropped or resized', event);
    this.refresh.next();
  }


  handleEvent(action: string, event: CalendarEvent): void {
    console.log(action + ' ' + event);
  }

  registerAppointment(): void {

    const dialogRef = this.dialog.open(AppointmentComponent, {
      data: { name: '', edit: false, title: 'Termin anlegen' }, disableClose: true, autoFocus: true
    });

    dialogRef.afterClosed().subscribe(result => {
      // do all the stuff after register operation has done
      console.log('The dialog was closed');
      this.fetchEvents(true);
    });
  }

  editAppointment(appointment: Appointment): void {
    console.log('edit appointment');
    const dialogRef = this.dialog.open(AppointmentComponent, {
      data: { appointmentObject: appointment, name: appointment.patientName, edit: true, title: 'Termin bearbeiten' }, disableClose: true, autoFocus: true
    });

    dialogRef.afterClosed().subscribe(result => {
      // do all the stuff after register operation has done
      console.log('The dialog was closed');
      this.fetchEvents(true);
    });
  }
}
