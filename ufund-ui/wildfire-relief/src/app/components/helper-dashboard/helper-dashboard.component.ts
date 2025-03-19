import { Component, OnInit } from '@angular/core';
import { BasketService } from '../../services/basket.service';
import { Need } from '../../models/need';
import { Router } from '@angular/router';

@Component({
  selector: 'app-helper-dashboard',
  templateUrl: './helper-dashboard.component.html',
  styleUrls: ['./helper-dashboard.component.css']
})
export class HelperDashboardComponent implements OnInit {

  needs: Need[] = [];
  basket: Need[] = [];
  message: string | null = null;
  searchText: string = '';

  constructor(private basketService: BasketService, private router: Router) {}

  ngOnInit(): void {
    this.loadNeeds();
    this.loadBasket();
  }

  loadNeeds(): void {
    this.basketService.getAllNeeds().subscribe(
      data => this.needs = data,
      error => this.message = 'Error loading needs'
    );
  }

  addToBasket(need: Need): void {
    this.basketService.addToBasket(need).subscribe({
      next: (response) => {
        this.message = 'Need added to basket!';
        this.loadBasket(); 
      },
      error: (error) => {
        console.error('Error adding to basket:', error);
        this.message = error.error.message || 'Failed to add need.';
      }
    });
  }

  loadBasket(): void {
    this.basketService.getBasket().subscribe(data => this.basket = data);
  }

  filteredNeeds(): Need[] {
    return this.needs.filter(n => n.name.toLowerCase().includes(this.searchText.toLowerCase()));
  }

  removeFromBasket(need: Need): void {
    this.basketService.removeFromBasket(need).subscribe({
      next: () => {
        this.message = 'Need removed from basket!';
        this.loadBasket(); 
      },
      error: error => {
        console.error('Error removing from basket:', error);
        this.message = error.error.message || 'Failed to remove need.';
      }
    });
  }

  logout(): void {
    this.basketService.logout().subscribe({
      next: () => {
        this.router.navigate(['/']); 
      },
      error: error => {
        console.error('Error logging out:', error);
      }
    });
  }

  
  searchNeeds(): void {
    if (this.searchText.trim() === '') {
      this.loadNeeds();
      return;
    }

    this.basketService.searchNeeds(this.searchText).subscribe({
      next: data => this.needs = data,
      error: error => {
        console.error('Error searching needs:', error);
      }
    });
  }
}
