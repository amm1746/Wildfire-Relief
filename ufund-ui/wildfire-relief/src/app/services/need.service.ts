import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Need } from '../models/need';

@Injectable({
  providedIn: 'root',
})
export class NeedService {
  private apiUrl = 'http://localhost:8080/cupboard'; // Base URL for the API

  constructor(private http: HttpClient) {}

  // Get all needs
  getAllNeeds(): Observable<Need[]> {
    return this.http.get<Need[]>(`${this.apiUrl}/needs`);
  }

  // Add a new need
  addNeed(need: Need): Observable<Need> {
    return this.http.post<Need>(`${this.apiUrl}/need`, need);
  }

  // Update a need
  updateNeed(name: string, updatedNeed: Need): Observable<Need> {
    return this.http.put<Need>(`${this.apiUrl}/need/${name}`, updatedNeed);
  }

  // Delete a need
  deleteNeed(name: string): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/need/${name}`);
  }

  // Search for needs by partial name
  searchNeedsByName(partialName: string): Observable<Need[]> {
    return this.http.get<Need[]>(`${this.apiUrl}/needs/search?name=${partialName}`);
  }

  // Get a need by name
  getNeedByName(name: string): Observable<Need> {
    return this.http.get<Need>(`${this.apiUrl}/need/${name}`);
  }
}