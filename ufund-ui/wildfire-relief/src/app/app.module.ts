import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { FormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { LoginComponent } from './components/login/login.component';
import { RouterModule } from '@angular/router';
import { ManagerDashboardComponent } from './components/dashboard/manager-dashboard.component';
import { HelperDashboardComponent } from './components/helper-dashboard/helper-dashboard.component';
import { AccessDeniedComponent } from './access-denied/access-denied.component';
import { RegisterComponent } from './register/register.component';
import { CheckoutComponent } from './components/checkout/checkout.component';
import { PopupComponent } from './components/popup/popup.component';  // Import the PopupComponent

@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    ManagerDashboardComponent,
    HelperDashboardComponent,
    AccessDeniedComponent,
    RegisterComponent,
    CheckoutComponent,
    PopupComponent,  // Declare PopupComponent here
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    FormsModule,
    HttpClientModule,
    RouterModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
