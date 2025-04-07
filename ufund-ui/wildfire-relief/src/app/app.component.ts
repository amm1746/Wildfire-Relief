import { Component } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})


export class AppComponent {
  title = 'wildfire-relief';

  constructor(private router: Router) {}

  isLoggedIn(): boolean {
    return !!localStorage.getItem('username');
  }

  shouldShowNotifications(): boolean {
    const hiddenRoutes = ['/', '/login'];
    return this.isLoggedIn() && !hiddenRoutes.includes(this.router.url);
  }
  
}

