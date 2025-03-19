import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LoginComponent } from './components/login/login.component';
import { ManagerDashboardComponent } from './components/dashboard/manager-dashboard.component';
import { HelperDashboardComponent } from './components/helper-dashboard/helper-dashboard.component';
/**
 * Defines the routes for the application.
 */
const routes: Routes = [
  {path: '', component: LoginComponent},
  {path: 'manager-dashboard', component: ManagerDashboardComponent},
  {path: 'helper-dashboard', component: HelperDashboardComponent}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
