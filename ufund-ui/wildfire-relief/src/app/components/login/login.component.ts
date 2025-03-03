import { Component } from '@angular/core';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent {
  username: string = '';
  password: string = '';
  role: string | null = null;
  error: string | null = null; 

  constructor(private authService: AuthService) {}

  login(): void{
    this.authService.login(this.username, this.password).subscribe(
      response => {
        this.role = response.role;
        this.error = null;
      },
      error => {
        this.error = 'Incorrect username or password.';
        this.role = null;
      }
    );
  }
}
