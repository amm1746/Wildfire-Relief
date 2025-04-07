import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { BasketService } from '../../services/basket.service';
import { Need } from '../../models/need';
import { Reward } from '../../models/reward';

@Component({
  selector: 'app-checkout',
  templateUrl: './checkout.component.html',
  styleUrls: ['./checkout.component.css']
})
export class CheckoutComponent implements OnInit {
  basket: Need[] = [];
  message: string | null = null;
  showPopup: boolean = false;  // Controls popup visibility
  rewardMessage: string = '';  // Message to show in the popup
  rewards: Reward[] = [];  // Array to hold all rewards
  showViewRewardsButton: boolean = false;  // Flag to control visibility of "View Rewards" button
  hasMadeFirstDonation: boolean = false;  // Flag to track if the first donation has been made

  constructor(private router: Router, private basketService: BasketService) {}

  ngOnInit(): void {
    this.loadBasket();
  }

  goBack(): void {
    this.router.navigate(['/helper-dashboard']);
  }

  loadBasket(): void {
    this.basketService.getBasket().subscribe(data => {
      this.basket = data;
    });
  }

  checkout(): void {
    this.basketService.checkout().subscribe({
      next: (res) => {
        console.log('Checkout response:', res);
  
        this.message = res.message;
        this.basket = [];
        this.basketService.emitCupboardUpdate();
  
        // Check if rewards is a string (in case the backend sends a message)
        if (typeof res.rewards === 'string') {
          this.rewardMessage = res.message; // Special Donation Reward message
          this.rewards = [];  // No rewards array if it's a string
          this.showViewRewardsButton = false;  // Hide "View Rewards" button if no array
  
          // Show the popup only if this is the first donation
          if (!this.hasMadeFirstDonation) {
            this.showPopup = true;  // Show the popup for the first donation
            this.hasMadeFirstDonation = true;  // Mark that the first donation has been made
          }
        }
        // Check if rewards is an array (should be an array of Rewards)
        else if (Array.isArray(res.rewards)) {
          this.rewards = res.rewards;  // Array of rewards
          this.rewardMessage = 'You earned the following rewards:';
          this.showViewRewardsButton = false;  // Show "View Rewards" button if rewards are available
  
          // Show the popup only if this is the first donation
          if (!this.hasMadeFirstDonation) {
            this.showPopup = true;  // Show the popup for the first donation
            this.hasMadeFirstDonation = true;  // Mark that the first donation has been made
          }
        }
  
        // Additional reward logic based on donation count or other conditions
        // Track the number of donations (assuming you have this count stored in localStorage)
        const donationCount = parseInt(localStorage.getItem('donationCount') || '0', 10);
  
        // Most Donations Reward
        if (donationCount >= 10) {
          this.rewardMessage = 'Congratulations! You earned a special reward for making the most donations!';
          this.rewards.push({ 
            name: 'Most Donations Reward', 
            description: 'Thank you for being such a generous donor!' 
          });
          this.showViewRewardsButton = false;  // Show "View Rewards" button
        }
  
        // Frequent Donor Reward (e.g., for donations every month)
        if (donationCount >= 5) {
          this.rewards.push({
            name: 'Frequent Donor Reward',
            description: 'You are a frequent donor! Thank you for your continued generosity.'
          });
          this.showViewRewardsButton = false;
        }
  
        // Generosity Award (e.g., large donation amount)
        if (res.donationAmount && res.donationAmount >= 100) {
          this.rewards.push({
            name: 'Generosity Award',
            description: 'Thank you for your large donation! Your generosity is truly appreciated.'
          });
          this.showViewRewardsButton = false;
        }
  
        // Encourage more donations with a reward for 3 donations
        if (donationCount >= 3 && donationCount < 5) {
          this.rewards.push({
            name: 'Thank You Reward',
            description: 'Thank you for your repeated donations! Your support is invaluable.'
          });
          this.showViewRewardsButton = false;
        }
  
        // Save updated donation count to localStorage
        localStorage.setItem('donationCount', (donationCount + 1).toString());
  
      },
      error: () => {
        this.message = 'Checkout failed. Please try again.';
      }
    });
  }
  

  closePopup(): void {
    this.showPopup = false;  // Close the popup by setting showPopup to false
  }

  viewRewards(): void {
    if (this.rewards.length > 0) {
      this.rewardMessage = 'You have earned the following rewards:';
    } else {
      this.rewardMessage = 'No rewards to display.';
    }
  }

  getRewardClass(reward: Reward): string {
    switch (reward.name) {
      case 'Most Donations Reward':
        return 'most-donations';
      case 'Frequent Donor Reward':
        return 'frequent-donor';
      case 'Generosity Award':
        return 'generosity-award';
      case 'Thank You Reward':
        return 'thank-you';
      default:
        return ''; // Default class if no match
    }
  }
  
  getRewardEmoji(reward: Reward): string {
    switch (reward.name) {
      case 'Most Donations Reward':
        return '🏅'; // Medal emoji
      case 'Frequent Donor Reward':
        return '🔄'; // Repeat emoji
      case 'Generosity Award':
        return '💖'; // Heart emoji
      case 'Thank You Reward':
        return '🙏'; // Thank you emoji
      default:
        return '🎉'; // Default emoji (celebration)
    }
  }
  
}
