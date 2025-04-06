import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { BasketService } from '../../services/basket.service';
import { Need } from '../../models/need';

@Component({
  selector: 'app-checkout',
  templateUrl: './checkout.component.html',
  styleUrls: ['./checkout.component.css']
})
export class CheckoutComponent implements OnInit {
  basket: Need[] = [];
  message: string | null = null;

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
      },
      error: () => {
        this.message = 'Checkout failed. Please try again.';
      }
    });
  }
  
}
