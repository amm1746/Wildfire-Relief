import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';

@Injectable({
  providedIn: 'root'
})

export class AuthService {
  private url = 'http://localhost:8080/api'
  private currentUsername : string | null = null;

  constructor(private http: HttpClient) { }

  login(username: string, password: string): Observable<any>{
    this.currentUsername = username;
    return this.http.post(`${this.url}/login`, { username, password}, {withCredentials: true});
  }

  logout(): Observable<any>{
    this.currentUsername = null;
    return this.http.post(`${this.url}/logout`, {}, {withCredentials: true});
  }

  getCurrentUser(): string | null {
    return this.currentUsername;
  }

  getRole(): Observable<any>{
    return this.http.get(`${this.url}/role`, {withCredentials: true});
  }
  getDonationCount() {
    return this.http.get<{ donationCount: number }>('/api/donationCount', { withCredentials: true });
  }
  
}
