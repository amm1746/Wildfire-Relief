import { Component } from '@angular/core';
import { AuthService } from '../../services/auth.service';
import { Router } from '@angular/router'; 
/**
 * LoginComponent
 * 
 * Handles user authentication, redirecting to dashboard upon sucessful login.
 * 
 * @author Alexandra Mantagas
 */

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {
  username: string = '';
  password: string = '';
  role: string | null = null;
  error: string | null = null; 

  /**
   * Initializes authentication service and router for naviagtion.
   * 
   * @param authService 
   * @param router 
   */
  constructor(private authService: AuthService, private router: Router) {}

  /**
   * Logs in the user with provided credentials. Goes to dashboard if successful.
   */
  login(): void{
    this.authService.login(this.username, this.password).subscribe(
      response => {
        this.role = response.role;
        this.error = null;

        if(this.role){
          this.router.navigate(['/dashboard']); 
        }
      },
      error => {
        this.error = 'Incorrect username or password.';
        this.role = null;
      }
    );
  }

  /**
   * Logs user out, resetting the session data. 
   */
  logout(): void{
    this.authService.logout().subscribe(() => {
      this.role = null;
      this.username = '';
      this.password = '';
    });
  }

  /**
   * Gets the user's role.
   */
  getRole(): void{
    this.authService.getRole().subscribe(response => {
      this.role = response.role;
    });
  }
}
