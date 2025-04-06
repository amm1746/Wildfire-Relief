import { Component, Input, Output, EventEmitter } from '@angular/core';
import { Reward } from '../../models/reward';

@Component({
  selector: 'app-popup',
  templateUrl: './popup.component.html',
  styleUrls: ['./popup.component.css']
})
export class PopupComponent {
  @Input() showPopup: boolean = false;  // Accept the flag to show or hide popup
  @Input() rewardMessage: string = '';  // Accept the message passed from CheckoutComponent
  @Input() rewards: Reward[] = [];  // Accept the list of rewards
  @Input() showViewRewardsButton: boolean = false;  // Accept the flag for viewing rewards
  @Output() close: EventEmitter<void> = new EventEmitter();  // Emit close event when popup is closed
  
  // Close the popup
  closePopup(): void {
    this.close.emit();  // Emit event to close the popup
  }

  // View Rewards logic
  viewRewards(): void {
    console.log("Viewing rewards", this.rewards);
  }

  // Get Reward Emoji based on reward type
  getRewardEmoji(reward: Reward): string {
    switch (reward.name) {
      case 'Most Donations Reward':
        return '🏅';  // Medal emoji
      case 'Frequent Donor Reward':
        return '🔄';  // Repeat emoji
      case 'Generosity Award':
        return '💖';  // Heart emoji
      case 'Thank You Reward':
        return '🙏';  // Thank you emoji
      default:
        return '🎉';  // Default emoji (celebration)
    }
  }

  // Get styles dynamically based on the reward type
  getRewardStyles(reward: Reward) {
    // All rewards will now have a consistent green background
    return { backgroundColor: '#8BC34A', color: '#FFFFFF' };  // Green with white text
  }
}
