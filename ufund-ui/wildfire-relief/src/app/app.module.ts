import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { FormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { LoginComponent } from './components/login/login.component';
import { RouterModule } from '@angular/router';
import { ManagerDashboardComponent } from './components/dashboard/manager-dashboard.component';

@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    ManagerDashboardComponent,
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
