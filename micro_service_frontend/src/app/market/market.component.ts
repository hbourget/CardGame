import { Component } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {catchError} from "rxjs/operators";
import {throwError} from "rxjs";

@Component({
  selector: 'app-market',
  templateUrl: './market.component.html',
  styleUrls: ['./market.component.css']
})
export class MarketComponent {
  cards:any
  constructor(private http:HttpClient) {
  }

  ngOnInit() {
    let response = this.http.get('http://localhost:8080/inventories/availablecards');
    response.subscribe((data)=>this.cards=data);
  }

  buyCard(id: any) {
    let response = this.http.post('http://localhost:8080/inventories/buy/users/1/cards/'+id, null).pipe(
      catchError((error) => {
        if (error.status === 409) {
          alert("Vous n'avez pas assez d'argent!");
        }
        return throwError(error);
      })
    );
    response.subscribe((data)=>{
      let updatedResponse = this.http.get('http://localhost:8080/inventories/availablecards');
      updatedResponse.subscribe((updatedData)=>this.cards=updatedData);
    });
  }
}
