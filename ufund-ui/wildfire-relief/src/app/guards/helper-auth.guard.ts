import { Injectable } from '@angular/core';
import { CanActivate, Router } from '@angular/router';
import { AuthService } from '../services/auth.service';

@Injectable({
  providedIn: 'root'
})
export class HelperAuthGuard implements CanActivate {

  constructor(private authService: AuthService, private router: Router) {}

  canActivate(): boolean {
    const role = sessionStorage.getItem('role'); 

    if (role === 'Helper') {
      return true; 
    } else {
      this.router.navigate(['/access-denied']); 
      return false;
    }
  }
}
