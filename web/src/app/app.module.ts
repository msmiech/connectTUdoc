import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';

import {AppRoutingModule} from './app-routing.module';
import {AppComponent} from './components/app.component';
import {MainComponent} from './components/main.component';
import {LoginComponent} from './components/login.component';
import {SidebarComponent} from './components/sidebar.component';
import {NavbarComponent} from './components/navbar.component';
import {HttpClientModule} from '@angular/common/http';
import {AccessService} from './services/access.service';
import {ChatService} from './services/chat.service';
import {AuthGuardService} from './services/auth-guard.service';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {CommonModule} from '@angular/common';
import {
  MAT_SNACK_BAR_DEFAULT_OPTIONS,
  MatAutocompleteModule,
  MatBadgeModule,
  MatBottomSheetModule,
  MatButtonModule,
  MatButtonToggleModule,
  MatCardModule,
  MatCheckboxModule,
  MatChipsModule,
  MatDatepickerModule,
  MatDialogModule,
  MatDividerModule,
  MatExpansionModule,
  MatGridListModule,
  MatIconModule,
  MatInputModule,
  MatListModule,
  MatMenuModule,
  MatNativeDateModule,
  MatPaginatorModule,
  MatProgressBarModule,
  MatProgressSpinnerModule,
  MatRadioModule,
  MatRippleModule,
  MatSelectModule,
  MatSidenavModule,
  MatSliderModule,
  MatSlideToggleModule,
  MatSnackBarModule,
  MatSortModule,
  MatStepperModule,
  MatTableModule,
  MatTabsModule,
  MatToolbarModule,
  MatTooltipModule,
  MatTreeModule
} from '@angular/material';
import {CalendarModule, DateAdapter} from 'angular-calendar';
import {adapterFactory} from 'angular-calendar/date-adapters/date-fns';
import {OfficeService} from './services/office.service';
import {OfficeHourComponent} from './components/office-hour.component';
import {MedicalworkerOverviewComponent} from './components/medicalworker-overview.component';
import {AppointmentOverviewComponent} from './components/appointment-overview.component';
import {AngularFireModule} from '@angular/fire';
import {AngularFireDatabaseModule} from '@angular/fire/database';
import {AngularFireAuthModule} from '@angular/fire/auth';
import {environment} from '../environments/environment';
import {OfficeManagementComponent} from './components/office-management.component';
import {OfficeHour} from './models/office-hour';
import {MedicalworkerService} from './services/medicalworker.service';
import { MedicalworkerComponent } from './components/medicalworker.component';
import { SpecialityComponent } from './components/speciality.component';
import { ChatComponent } from './components/chat.component';
import { ChatThreadsComponent } from './components/chat-threads.component';
import { ChatInputComponent } from './components/chat-input.component';
import { ChatDetailComponent } from './components/chat-detail.component';
import { ChatMessageComponent } from './components/chat-message.component';
import { AppointmentComponent } from './components/appointment.component';
import {AppointmentService} from './services/appointment.service';
import {ChatHeaderComponent} from './components/chat-header.component';
import {VideoCallComponent} from './components/video-call.component';
import {ScreenshotDialogComponent} from './components/screenshot-dialog.component';
import { AsyncPipe } from '../../node_modules/@angular/common';
import { AngularFireMessagingModule} from '@angular/fire/messaging';
import {PatientConfirmComponent} from './components/patient-confirm.component';

@NgModule({
  declarations: [
    AppComponent,
    MainComponent,
    LoginComponent,
    SidebarComponent,
    NavbarComponent,
    OfficeHourComponent,
    MedicalworkerOverviewComponent,
    AppointmentOverviewComponent,
    OfficeManagementComponent,
    MedicalworkerComponent,
    SpecialityComponent,
    ChatComponent,
    ChatThreadsComponent,
    ChatInputComponent,
    ChatDetailComponent,
    ChatHeaderComponent,
    ChatMessageComponent,
    AppointmentComponent,
    VideoCallComponent,
    ScreenshotDialogComponent,
    PatientConfirmComponent
  ],
  imports: [
    BrowserModule,
    CommonModule,
    AppRoutingModule,
    HttpClientModule,
    BrowserAnimationsModule,
    MatToolbarModule, // Material Design Modules
    MatAutocompleteModule,
    MatBadgeModule,
    MatBottomSheetModule,
    MatButtonModule,
    MatButtonToggleModule,
    MatCardModule,
    MatCheckboxModule,
    MatChipsModule,
    MatStepperModule,
    MatDatepickerModule,
    MatDialogModule,
    MatDividerModule,
    MatExpansionModule,
    MatGridListModule,
    MatIconModule,
    MatInputModule,
    MatListModule,
    MatMenuModule,
    MatNativeDateModule,
    MatPaginatorModule,
    MatProgressBarModule,
    MatProgressSpinnerModule,
    MatRadioModule,
    MatRippleModule,
    MatSelectModule,
    MatSidenavModule,
    MatButtonToggleModule,
    MatListModule,
    MatInputModule,
    FormsModule,
    MatSliderModule,
    MatSlideToggleModule,
    MatSnackBarModule,
    MatSortModule,
    MatTableModule,
    MatTabsModule,
    MatToolbarModule,
    MatTooltipModule,
    MatTreeModule,
    CalendarModule.forRoot({ // Calendar Modules
      provide: DateAdapter,
      useFactory: adapterFactory
    }),
    AngularFireDatabaseModule,
    AngularFireMessagingModule,
    AngularFireAuthModule,
    AngularFireModule.initializeApp(environment.firebase),
    ReactiveFormsModule
  ],
  providers: [
    AccessService,
    OfficeService,
    OfficeHour,
    MedicalworkerService,
    ChatService,
    AuthGuardService,
    FormsModule,
    AccessService,
    OfficeService,
    OfficeHour,
    MedicalworkerService,
    AppointmentService,
    AsyncPipe,
    // global setting for the duration of any snackbar, can be overwritten at selection
    {provide: MAT_SNACK_BAR_DEFAULT_OPTIONS, useValue: {duration: 2500}}
  ],
  bootstrap: [AppComponent],
  entryComponents: [AppointmentComponent, ScreenshotDialogComponent, PatientConfirmComponent]
})
export class AppModule {
}
