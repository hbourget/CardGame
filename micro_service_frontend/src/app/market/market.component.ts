import { Component, OnDestroy, OnInit } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { catchError } from 'rxjs/operators';
import { throwError } from 'rxjs';
import { AuthService } from '../auth.service';
import { User } from '../auth.service';

@Component({
  selector: 'app-market',
  templateUrl: './market.component.html',
  styleUrls: ['./market.component.css']
})
export class MarketComponent implements OnInit, OnDestroy {
  cards: any;
  user: User | null = null;
  userSubscription: any;
  serverIp = 'http://192.168.1.17:8080';

  constructor(private http: HttpClient, private authService: AuthService) {}

  ngOnInit() {
    this.userSubscription = this.authService.user$.subscribe((user) => {
      this.user = user;
      if (user) {
        const token = this.authService.getAccessToken();
        const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);
        let response = this.http.get(this.serverIp + '/inventories/availablecards', { headers });
        response.subscribe((data) => (this.cards = data));
      }
    });
  }

  ngOnDestroy() {
    this.userSubscription.unsubscribe();
  }

  buyCard(id: any) {
    if (this.user) {
      const token = this.authService.getAccessToken();
      const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);
      let response = this.http
        .post(this.serverIp + '/inventories/buy/users/' + this.user.username + '/cards/' + id, null, { headers })
        .pipe(
          catchError((error) => {
            if (error.status === 409) {
              alert("Vous n'avez pas assez d'argent!");
            }
            return throwError(error);
          })
        );
      response.subscribe((data) => {
        this.authService.getUserData();

        let updatedResponse = this.http.get(this.serverIp + '/inventories/availablecards', { headers });
        updatedResponse.subscribe((updatedData) => (this.cards = updatedData));
      });
    }
  }

}
