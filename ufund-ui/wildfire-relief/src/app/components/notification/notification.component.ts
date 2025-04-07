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

    this.notificationService.getNotifications().subscribe(data => {
      this.notifications = data.filter(note =>
        note.recipients.includes(this.username)
      );
      this.cdr.detectChanges();
    });

    setInterval(() => {
      this.notificationService.getNotifications().subscribe(data => {
        const filtered = data.filter(note =>
          note.recipients.includes(this.username)
        );
        this.notifications = filtered;
        this.cdr.detectChanges();
      });
    }, 2000);
  }
}
