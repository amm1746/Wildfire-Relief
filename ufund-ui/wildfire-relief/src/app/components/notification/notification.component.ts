import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import {NotificationService, Notification } from '../../services/notification.service';

@Component({
  selector: 'app-notification',
  templateUrl: './notification.component.html',
  styleUrls: ['./notification.component.css']
})
export class NotificationComponent implements OnInit {
  notifications: Notification[] = [];
  username: string = '';

  constructor(
    private notificationService: NotificationService,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void{

    this.username = localStorage.getItem('username') || '';
    console.log('Current username:', this.username);

    this.notificationService.getNotifications().subscribe(data => {
      console.log('Fetched notifications:', data);
      this.notifications = data.filter(note =>
        note.recipients.includes(this.username) &&
        note.sender !== this.username
      );

      console.log('Filtered notifications:', this.notifications);
      this.cdr.detectChanges();
    });

    setInterval(() => {
      this.notificationService.getNotifications().subscribe(data => {
        console.log('Fetched (interval):', data);
        const filtered = data.filter(note =>
          note.recipients.includes(this.username)
        );
        console.log('Filtered (interval):', filtered);
        this.notifications = [...filtered];
        this.cdr.detectChanges();
      });
    }, 2000);
  }
}
