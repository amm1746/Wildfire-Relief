import { Component } from '@angular/core';
import { AuthService } from '../../services/auth.service';
import { Router } from '@angular/router';
/**
 * DashboardComponent
 * 
 * Displays the dashboard when logged in sucessfully.
 * 
 */
@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent {
  role: string | null = null;

  /**
   * Initializes authentication service and router for naviagtion.
   * @param authService 
   * @param router 
   */
  constructor(private authService: AuthService, private router: Router) {
    this.authService.getRole().subscribe(response => {
      this.role = response.role;
      if (!this.role) {
        this.router.navigate(['/']); 
      }
    });
  }

  /**
   * Logs the user out and redirects to login page. 
   */
  logout(): void {
    this.authService.logout().subscribe(() => {
      this.router.navigate(['/']);
    });
  }
}
