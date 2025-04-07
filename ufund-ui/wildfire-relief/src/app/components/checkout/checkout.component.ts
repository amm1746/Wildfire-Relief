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
  showPopup: boolean = false; 
  rewardMessage: string = '';  
  rewards: Reward[] = [];  
  hasMadeFirstDonation: boolean = false;  
  firstDonationRewardShown: boolean = false; 

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
  
        // Retrieve current donation count from localStorage
        let donationCount = parseInt(localStorage.getItem('donationCount') || '0', 10);
        console.log('Current donation count:', donationCount); // Debugging output
  
        // Handle rewards and messages
        if (typeof res.rewards === 'string') {
          // First donation scenario
          this.rewardMessage = res.message;
          this.rewards = [];
  
          // Show the first donation reward popup only when donationCount is 1
          if (donationCount === 0 && !this.firstDonationRewardShown) {
            this.showPopup = true;
            this.firstDonationRewardShown = true;
            localStorage.setItem('firstDonationRewardShown', 'true');  // Save state to prevent showing again
          }
        } else if (Array.isArray(res.rewards)) {
          this.rewards = res.rewards.filter((reward: { name: string; }) => reward.name !== 'Thanks for funding your first need!' || this.firstDonationRewardShown);
          this.rewardMessage = 'You earned the following rewards:';
  
          if (donationCount === 0 && !this.firstDonationRewardShown) {
            this.showPopup = true;
            this.firstDonationRewardShown = true;
            localStorage.setItem('firstDonationRewardShown', 'true');  // Save state to prevent showing again
          }
        }
  
        // Reward logic based on donation count
        if (donationCount > 0) {
          // Add rewards based on the number of donations
          if (donationCount >= 100) {
            this.rewards.push({
              name: 'Most Donations Reward',
              description: 'Thank you for being such a generous donor! You have the most donations!' 
            });
          }
          if (donationCount >= 100) {
            this.rewards.push({
              name: 'Frequent Donor Reward',
              description: 'You are a frequent donor! Thank you for your continued generosity.'
            });
          }
          if (res.donationAmount && res.donationAmount >= 100) {
            this.rewards.push({
              name: 'Generosity Award',
              description: 'Thank you for your large donation! Your generosity is truly appreciated.'
            });
          }
          if (donationCount >= 3 && donationCount < 5) {
            this.rewards.push({
              name: 'Thank You Reward',
              description: 'Thank you for your repeated donations! Your support is invaluable.'
            });
          }
        }
  
        console.log('Rewards after donation count check:', this.rewards);  // Debugging output
  
        donationCount += 1;  // Increment the donation count
        localStorage.setItem('donationCount', donationCount.toString());  // Save the updated count
        console.log('Updated donation count saved:', donationCount);  // Debugging output
  
        // Clear the basket and trigger an update to reflect the changes
        this.basket = [];
        this.basketService.emitCupboardUpdate();
      },
      error: () => {
        this.message = 'Checkout failed. Please try again.';
      }
    });
  }
  

  closePopup(): void {
    this.showPopup = false; 
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
