import { Component } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {catchError} from "rxjs/operators";
import {throwError} from "rxjs";

@Component({
  selector: 'app-inventory',
  templateUrl: './inventory.component.html',
  styleUrls: ['./inventory.component.css']
})
export class InventoryComponent {

  inv:any
  constructor(private http:HttpClient) {
  }

  ngOnInit() {
    let response = this.http.get('http://localhost:8080/inventories/users/1');
    response.subscribe((data)=>this.inv=data);
  }

  sellCard(id: any) {
    let response = this.http.post('http://localhost:8080/inventories/sell/users/1/cards/'+id, null).pipe(
      catchError((error) => {
        if (error.status === 409) {
          alert("Vous ne possédez pas la carte");
        }
        return throwError(error);
      })
    );
    response.subscribe((data)=>{
      let updatedResponse = this.http.get('http://localhost:8080/inventories/users/1');
      updatedResponse.subscribe((updatedData)=>this.inv=updatedData);
    });
  }
}
