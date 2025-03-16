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
    throw new Error('Method not implemented.');
  }


  needs: Need[] = [];
  newNeed: Need = new Need('', 0, 0, ''); 
  selectedNeed: Need | null = null;
  addNeed(): void {
    this.needService.addNeed(this.newNeed).subscribe(
      (data) => {
        this.needs.push(data); // Add the new need to the list
        this.newNeed = new Need('', 0, 0, ''); // Reset the form
      },
      (error) => {
        console.error('Error adding need:', error);
      }
    );
  }

  editNeed(need: Need): void {
    this.selectedNeed = { ...need }; // Create a copy of the need for editing
  }

  updateNeed(): void {
    if (this.selectedNeed) {
      this.needService.updateNeed(this.selectedNeed.name, this.selectedNeed).subscribe(
        (data) => {
          const index = this.needs.findIndex((n) => n.name === data.name);
          if (index !== -1) {
            this.needs[index] = data; // Update the need in the list
          }
          this.selectedNeed = null; // Clear the selected need
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
        this.needs = this.needs.filter((n) => n.name !== needName); // Remove the need from the list
      },
      (error) => {
        console.error('Error deleting need:', error);
      }
    );
  }

  logout(): void {
    this.router.navigate(['/']);
  }
}
