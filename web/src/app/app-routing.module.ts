import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {MainComponent} from './components/main.component';
import {LoginComponent} from './components/login.component';
import {OfficeManagementComponent} from './components/office-management.component';
import {AppointmentOverviewComponent} from './components/appointment-overview.component';
import {AuthGuardService} from './services/auth-guard.service';
import {MedicalworkerOverviewComponent} from './components/medicalworker-overview.component';
import {ChatThreadsComponent} from './components/chat-threads.component';
import {ChatDetailComponent} from './components/chat-detail.component';
import {AppointmentComponent} from './components/appointment.component';
import {VideoCallComponent} from './components/video-call.component';


const routes: Routes = [
  {path: '', redirectTo: '/login', pathMatch: 'full'},
  {
    path: 'login', component: LoginComponent,
    children: [
      {path: '', component: LoginComponent}
    ]
  },
  {
    path: 'main', component: MainComponent, canActivate: [AuthGuardService],
    children: [
      {path: '', component: AppointmentOverviewComponent},
      {path: 'office', component: OfficeManagementComponent, canActivate: [AuthGuardService]},
      {path: 'appointment-overview', component: AppointmentOverviewComponent, canActivate: [AuthGuardService]},
      {path: 'chats', component: ChatThreadsComponent},
      {path: 'chat/:id', component: ChatDetailComponent},
      {path: 'appointment-overview', component: AppointmentOverviewComponent, canActivate: [AuthGuardService]},
      // {path: 'messages-overview', component: MessagesOverviewComponent, canActivate: [AuthGuardService]},
      // test purposes cause others not work ...
      {path: 'medicalworker-overview', component: MedicalworkerOverviewComponent},
      {path: 'appointment', component: AppointmentComponent,  canActivate: [AuthGuardService]},
      {path: 'video-call/:patientId/:patientName', component: VideoCallComponent,  canActivate: [AuthGuardService]}
    ]
  },
];


@NgModule({
  imports: [RouterModule.forRoot(routes
    // , { enableTracing: true } // <-- debugging purposes only
    )],
  exports: [RouterModule]
})
export class AppRoutingModule {
}
