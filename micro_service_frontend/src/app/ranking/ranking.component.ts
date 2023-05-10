import {Component, OnInit} from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";
import { AuthService } from '../auth.service';

@Component({
  selector: 'app-ranking',
  templateUrl: './ranking.component.html',
  styleUrls: ['./ranking.component.css']
})
export class RankingComponent implements OnInit{
  users:any
  serverIp = 'http://192.168.1.17:8080';
  constructor(private http: HttpClient, private authService: AuthService) {}

  ngOnInit() {
    const token = this.authService.getAccessToken();
    if (token) {
      const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);
      this.http
        .get(this.serverIp + '/users', { headers })
        .subscribe((data) => (this.users = data));
    } else {
      alert('Vous êtes déconnecté')
    }
  }
}
