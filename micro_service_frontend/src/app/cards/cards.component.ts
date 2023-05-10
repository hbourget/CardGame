import { Component, OnInit } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { AuthService } from '../auth.service';
import {Subject} from "rxjs";

@Component({
  selector: 'app-cards',
  templateUrl: './cards.component.html',
  styleUrls: ['./cards.component.css']
})
export class CardsComponent implements OnInit {
  cards: any;
  dtOptions: DataTables.Settings = {};
  dtTrigger: Subject<any> = new Subject<any>();
  serverIp = 'http://localhost:8080';

  constructor(private http: HttpClient, private authService: AuthService) {}

  ngOnInit() {
    const token = this.authService.getAccessToken();
    if (token) {
      const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);
      this.http
        .get(this.serverIp + '/cards', { headers })
        .subscribe((data) => {
          this.cards = data;
          // @ts-ignore
          this.dtTrigger.next();
        });
    } else {
      alert('Vous êtes déconnecté')
    }

    this.dtOptions = {
      pagingType: 'full_numbers',
      pageLength: 25,
      language: {
        url: '//cdn.datatables.net/plug-ins/1.10.25/i18n/French.json'
      }
    };
  }

}
