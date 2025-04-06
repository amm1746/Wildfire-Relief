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
        this.message = res.message;
        this.basket = [];
        this.basketService.emitCupboardUpdate();
  
        console.log('Response:', res);
        console.log('Rewards:', res.rewards);
  
        // Check if rewards is an array and display each reward description
        if (Array.isArray(res.rewards)) {
          this.showPopup = true;
          // Join the descriptions of the rewards to form a detailed message
          this.rewardMessage = 'You earned: ' + res.rewards.map((reward: Reward) => reward.description).join(', ');
        } else if (typeof res.rewards === 'string') {
          this.showPopup = true;
          this.rewardMessage = res.rewards;  // Handle string-based reward messages
        }
      },
      error: () => {
        this.message = 'Checkout failed. Please try again.';
      }
    });
  }
  
  closePopup(): void {
    this.showPopup = false;  // Close the popup when this method is called
  }
}
