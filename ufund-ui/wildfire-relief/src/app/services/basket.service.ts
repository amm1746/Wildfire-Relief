import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Need } from '../models/need';

@Injectable({
  providedIn: 'root'
})
export class BasketService {

  private url = 'http://localhost:8080';

  constructor(private http: HttpClient) { }

  getAllNeeds(): Observable<Need[]> {
    return this.http.get<Need[]>(`${this.url}/cupboard/needs`, { withCredentials: true });
  }

  addToBasket(need: Need): Observable<any> {
    return this.http.post(`${this.url}/api/add-to-basket`, need, { withCredentials: true });
  }

  getBasket(): Observable<Need[]> {
    return this.http.get<Need[]>(`${this.url}/api/basket`, { withCredentials: true });
  }

  removeFromBasket(need: Need): Observable<any> {
    return this.http.post(`${this.url}/api/remove-from-basket`, need, { withCredentials: true });
  }

  checkout(): Observable<any> {
    return this.http.post(`${this.url}/api/checkout`, {}, { withCredentials: true });
  }

  logout(): Observable<any> {
    return this.http.post(`${this.url}/api/logout`, {}, { withCredentials: true });
  }

  searchNeeds(name: string): Observable<Need[]> {
    return this.http.get<Need[]>(`${this.url}/cupboard/needs/search?name=${name}`, { withCredentials: true });
  }
}
