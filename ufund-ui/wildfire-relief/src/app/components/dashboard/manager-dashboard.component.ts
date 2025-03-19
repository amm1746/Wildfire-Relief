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

  addNeed(): void 
  {
    if (!this.newNeed.name || this.newNeed.cost == null || this.newNeed.quantity == null || !this.newNeed.type) {
      this.message = 'All Fields are Required!';
      return;
    }

    this.needService.addNeed(this.newNeed).subscribe(
      (response) => {
        this.message = 'Need Added Successfully!'
        this.needs.push(response);
        this.newNeed = new Need('', 0, 0, '')
      },
      (error) => {
        console.error('Error Adding Need:', error);
        if (error.error && error.error.message) {
          this.message = error.error.message;
        } else if (error.status === 409) {
          this.message = 'A need with this name already exists.';
        } else {
          this.message = 'Failed to create need. ' + error;
        }
      }
    );
  }

  editNeed(need: Need): void {
    this.selectedNeed = { ...need }; 
  }

  updateNeed(): void {
    if (this.selectedNeed) { // Check if selectedNeed is not null
      this.needService.updateNeed(this.selectedNeed.name, this.selectedNeed).subscribe({
        next: (response) => {
          console.log('Update Successful:', response);
  
          // Manually update the needs list
          const index = this.needs.findIndex((n) => n.name === this.selectedNeed!.name); // Use non-null assertion here
          if (index !== -1) {
            this.needs[index] = this.selectedNeed!; // Use non-null assertion here
          }
  
          this.selectedNeed = null; // Reset the selected need
          this.message = 'Need Updated Successfully!';
        },
        error: (error) => {
          console.error('Error Updating Need:', error);
          this.message = 'Failed to Update Need.';
        }
      });
    } else {
      console.error('Selected Need is Null.');
      this.message = 'No Need Selected for Update.';
    }
  }

  deleteNeed(needName: string): void {
    this.needService.deleteNeed(needName).subscribe({
      next: (response) => {
        console.log('Delete Successful:', response);
        this.message = 'Need Deleted Successfully!';
        this.needs = this.needs.filter((n) => n.name !== needName); // Remove the need from the list
      },
      error: (error) => {
        console.error('Error Deleting Need:', error);
        this.message = 'Failed to Delete Deed.';
      }
    });
  }

  logout(): void {
    this.router.navigate(['/']);
  }
}
