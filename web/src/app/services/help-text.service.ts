import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class HelpTextService {

  public infoInputRequired = 'Eingabe notwendig!';
  public infoDateFormat = 'Das Datumsformat ist  yyyy-MM-dd HH:mm';
  public infoEmail = 'Bitte ein gültiges Email format wählen z.b. max.muster@muster.en';
  public infoTelFax = 'Falsches Format! z.B. 02644 9685 oder 02644/9685';

  constructor() { }
}
