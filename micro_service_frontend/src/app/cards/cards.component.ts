import {Component, OnInit} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import { catchError } from 'rxjs/operators';
import { throwError } from 'rxjs';

@Component({
  selector: 'app-cards',
  templateUrl: './cards.component.html',
  styleUrls: ['./cards.component.css']
})
export class CardsComponent implements OnInit {

  cards:any
  constructor(private http:HttpClient) {
  }

  ngOnInit() {
    let response = this.http.get('http://localhost:8080/cards');
    response.subscribe((data)=>this.cards=data);
    }
}
