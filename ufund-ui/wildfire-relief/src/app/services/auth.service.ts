import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';

@Injectable({
  providedIn: 'root'
})

export class AuthService {
  private url = 'http://localhost:8080/api'

  constructor(private http: HttpClient) { }

  login(username: string, password: string): Observable<any>{
    return this.http.post(`${this.url}/login`, null, {
      params: {username, password},
      withCredentials: true
    });
  }

  logout(): Observable<any>{
    return this.http.post(`${this.url}/logout`, {}, {withCredentials: true});
  }

  getRole(): Observable<any>{
    return this.http.get(`${this.url}/role`, {withCredentials: true});
  }
}
