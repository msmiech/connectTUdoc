import { Injectable } from '@angular/core';
import {MatSnackBar} from '@angular/material';

@Injectable({
  providedIn: 'root'
})
export class NotificationService {

  constructor(public snackBar: MatSnackBar) { }

  public notifySuccess(message: string, callback?: () => any) {
    this.snackBar.open(message, 'Erfolgreich', {
    });
  }

  public getErrorMessage(error: string) {
    this.snackBar.open(error, 'Fehler', {
      duration: 5000
    });
  }
}
