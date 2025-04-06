import { Component, Input, Output, EventEmitter } from '@angular/core';

@Component({
  selector: 'app-popup',
  templateUrl: './popup.component.html',
  styleUrls: ['./popup.component.css']
})
export class PopupComponent {
  @Input() rewardMessage: string = '';  // Accept the message passed from CheckoutComponent
  @Output() close: EventEmitter<void> = new EventEmitter();  // Emit close event when popup is closed
  
  closePopup(): void {
    this.close.emit();  // Emit event to close the popup
  }
}
