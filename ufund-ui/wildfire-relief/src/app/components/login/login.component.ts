import { Component } from '@angular/core';
import { AuthService } from '../../services/auth.service';
import { Router } from '@angular/router'; 

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

  constructor(private authService: AuthService, private router: Router) {}

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

  logout(): void{
    this.authService.logout().subscribe(() => {
      this.role = null;
      this.username = '';
      this.password = '';
    });
  }

  getRole(): void{
    this.authService.getRole().subscribe(response => {
      this.role = response.role;
    });
  }
}
