import { Injectable } from '@angular/core';
import jwt_decode from 'jwt-decode';
import {BehaviorSubject, Observable} from "rxjs";
import {HttpHeaders, HttpClient} from "@angular/common/http";

interface DecodedToken {
  exp?: number;
  [key: string]: any;
}

export interface User {
  id: number;
  username: string;
  balance: string;
  idInventory: number
}

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  constructor(private http: HttpClient) {}

  userSubject: BehaviorSubject<User | null> = new BehaviorSubject<User | null>(null);
  public user$: Observable<User | null> = this.userSubject.asObservable();


  public getUserData(): void {
    const token = this.getAccessToken();
    if (token && !this.isTokenExpired(token)) {
      const decodedToken = jwt_decode(token) as DecodedToken;
      const username = decodedToken['sub'];
      const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);
      this.http.get<User>(`http://proxyauth:8080/users/${username}`, { headers }).subscribe((user) => {
        this.userSubject.next(user);
      });
    }
    else {
      this.userSubject.next(null);
    }
  }

  public setAccessToken(token: string): void {
    localStorage.setItem('access_token', token);
  }

  public getAccessToken(): string | null {
    return localStorage.getItem('access_token');
  }

  public setRefreshToken(token: string): void {
    localStorage.setItem('refresh_token', token);
  }

  public getRefreshToken(): string | null {
    return localStorage.getItem('refresh_token');
  }

  public removeTokens(): void {
    localStorage.removeItem('access_token');
    localStorage.removeItem('refresh_token');
  }

  public isTokenExpired(token: string): boolean {
    try {
      const decoded = jwt_decode(token) as DecodedToken;
      if (decoded.exp === undefined) {
        return false;
      }
      const currentTime = Math.floor(Date.now() / 1000);
      return decoded.exp < currentTime;
    } catch (err) {
      return false;
    }
  }
}
