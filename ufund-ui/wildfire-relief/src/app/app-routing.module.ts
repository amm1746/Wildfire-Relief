import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LoginComponent } from './components/login/login.component';
import { ManagerDashboardComponent } from './components/dashboard/manager-dashboard.component';
/**
 * Defines the routes for the application.
 */
const routes: Routes = [
  {path: '', component: LoginComponent},
  {path: 'dashboard', component: ManagerDashboardComponent}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
