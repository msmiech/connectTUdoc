import {Injectable} from '@angular/core';
import {CanActivate, Router} from '@angular/router';
import {AccessService} from './access.service';

@Injectable()
export class AuthGuardService implements CanActivate {

  constructor(private router: Router, private accessService: AccessService) {
  }

  canActivate() {
    if (this.accessService.isLoggedIn()) {
      return true;
    }
    this.router.navigate(['/']);
    return false;
  }
}
