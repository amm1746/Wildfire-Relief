import { Component } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
})
export class RegisterComponent {
  username = '';
  password = '';

  constructor(private http: HttpClient, private router: Router) {}

  register(){
    this.http.post<any>('http://localhost:8080/api/register', {
        username: this.username,
        password: this.password
    }).subscribe({
      next: response => {
        alert('Account created successfully!');
        this.router.navigate(['/']);
      },
      error: err => {
        alert(err.error.message || 'Registration failed.');
      }
    });
  }
}
