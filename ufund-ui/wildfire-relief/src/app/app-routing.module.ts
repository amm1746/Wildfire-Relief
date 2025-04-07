import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LoginComponent } from './components/login/login.component';
import { ManagerDashboardComponent } from './components/dashboard/manager-dashboard.component';
import { HelperDashboardComponent } from './components/helper-dashboard/helper-dashboard.component';
import { HelperAuthGuard } from './guards/helper-auth.guard';
import { AccessDeniedComponent } from './access-denied/access-denied.component';
import { RegisterComponent } from './register/register.component';
import { CheckoutComponent } from './components/checkout/checkout.component';
import { NotificationComponent } from './components/notification/notification.component';
/**
 * Defines the routes for the application.
 */
const routes: Routes = [
  {path: '', component: LoginComponent},
  {path: 'manager-dashboard', component: ManagerDashboardComponent},
  {path: 'helper-dashboard', component: HelperDashboardComponent, canActivate: [HelperAuthGuard]},
  { path: 'checkout', component: CheckoutComponent },
  { path: 'access-denied', component: AccessDeniedComponent }, 
  { path: 'register', component: RegisterComponent },
  { path: 'notifications', component: NotificationComponent },
  { path: '**', redirectTo: '/login' }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
