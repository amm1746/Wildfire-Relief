import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Need } from '../models/need';

@Injectable({
  providedIn: 'root', 
})
export class NeedService {
  private apiUrl = 'http://localhost:8080/api/needs'; 

  constructor(private http: HttpClient) {}

  // Get all needs
  getAllNeeds(): Observable<Need[]> {
    return this.http.get<Need[]>(this.apiUrl);
  }

  // Add a new need
  addNeed(need: Need): Observable<Need> {
    return this.http.post<Need>(this.apiUrl, need);
  }

  // Update a need
  updateNeed(name: string, updatedNeed: Need): Observable<Need> {
    return this.http.put<Need>(`${this.apiUrl}/${name}`, updatedNeed);
  }

  // Delete a need
  deleteNeed(name: string): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${name}`);
  }

  // Check if a need exists
  needExists(name: string): Observable<boolean> {
    return this.http.get<boolean>(`${this.apiUrl}/exists/${name}`);
  }
}