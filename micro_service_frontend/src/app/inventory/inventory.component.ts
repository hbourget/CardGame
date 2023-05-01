import { Component } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { catchError } from 'rxjs/operators';
import { throwError } from 'rxjs';
import { AuthService } from '../auth.service';
import jwt_decode from 'jwt-decode';

interface DecodedToken {
  sub: string;
  [key: string]: any;
}

@Component({
  selector: 'app-inventory',
  templateUrl: './inventory.component.html',
  styleUrls: ['./inventory.component.css'],
})
export class InventoryComponent {
  inv: any;
  username: any;

  constructor(private http: HttpClient, private authService: AuthService) {}

  ngOnInit() {
    const token = this.authService.getAccessToken();
    if (token) {
      const decodedToken = jwt_decode(token) as DecodedToken;
      this.username = decodedToken.sub;

      const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);
      this.http
        .get(`http://proxyauth:8080/inventories/users/${this.username}`, { headers })
        .subscribe((data) => (this.inv = data));
    } else {
      alert('Vous êtes déconnecté')
    }
  }

  sellCard(id: any) {
    const token = this.authService.getAccessToken();
    if (token) {
      const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);
      this.http
        .post(`http://proxyauth:8080/inventories/sell/users/${this.username}/cards/${id}`, null, { headers })
        .pipe(
          catchError((error) => {
            if (error.status === 409) {
              alert('Vous ne possédez pas la carte');
            }
            return throwError(error);
          })
        )
        .subscribe((data) => {
          this.authService.getUserData();
          this.http
            .get(`http://proxyauth:8080/inventories/users/${this.username}`, { headers })
            .subscribe((updatedData) => (this.inv = updatedData));
        });
    } else {
      alert('Vous êtes déconnecté')
    }
  }
}
