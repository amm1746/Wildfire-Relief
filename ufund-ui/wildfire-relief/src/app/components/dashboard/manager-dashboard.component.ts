import { Component, OnInit  } from '@angular/core';
import { NeedService } from '../../services/need.service';
import { Router } from '@angular/router';
import { Need } from '../../models/need';
/**
 * DashboardComponent
 * 
 * Displays the dashboard when logged in sucessfully.
 * 
 */
@Component({
  selector: 'app-manager-dashboard',
  templateUrl: './manager-dashboard.component.html',
  styleUrls: ['./manager-dashboard.component.css']
})

export class ManagerDashboardComponent implements OnInit
{
  /**
   * Initializes authentication service and router for naviagtion.
   * 
   * @param needService 
   * @param router 
   */

  constructor(private needService: NeedService, private router: Router) {}

  ngOnInit(): void {
    this.fetchNeeds();
  }

  fetchNeeds(): void {
    this.needService.getAllNeeds().subscribe(
      (data) => {
        this.needs = data;
      },
      (error) => {
        console.error('Error fetching needs:', error);
      }
    );
  }

  message: string = '';

  needs: Need[] = [];
  newNeed: Need = new Need('', 0, 0, ''); 
  selectedNeed: Need | null = null;

  addNeed(): void {
    if (!this.newNeed.name || this.newNeed.cost == null || this.newNeed.quantity == null || !this.newNeed.type) {
      this.message = 'All fields are Required!';
      return;
    }

    this.needService.addNeed(this.newNeed).subscribe(
      (response) => {
        this.message = 'Need Added Successfully!'
        this.needs.push(response);
        this.newNeed = new Need('', 0, 0, '')
      },
      (error) => 
        {
        this.message = ' Failed to Create Need.'
        console.error('Error adding need:', error);
      }
    );
  }

  editNeed(need: Need): void {
    this.selectedNeed = { ...need }; 
  }

  updateNeed(): void {
    if (this.selectedNeed) {
      this.needService.updateNeed(this.selectedNeed.name, this.selectedNeed).subscribe(
        (data) => {
          const index = this.needs.findIndex((n) => n.name === data.name);
          if (index !== -1) {
            this.needs[index] = data; 
          }
          this.selectedNeed = null; 
        },
        (error) => {
          console.error('Error updating need:', error);
        }
      );
    }
  }

  deleteNeed(needName: string): void {
    this.needService.deleteNeed(needName).subscribe(
      () => {
        this.message = 'Need deleted successfully!';
        this.needs = this.needs.filter((n) => n.name !== needName); // Remove the need from the list
      },
      (error) => {
        console.error('Error deleting need:', error);
        this.message = 'Failed to delete need. Please try again.';
      }
    );
  }

  logout(): void {
    this.router.navigate(['/']);
  }
}
