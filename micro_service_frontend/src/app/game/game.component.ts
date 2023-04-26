import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { switchMap } from 'rxjs/operators';

@Component({
  selector: 'app-game',
  templateUrl: './game.component.html',
  styleUrls: ['./game.component.css']
})
export class GameComponent implements OnInit {
  roomId: any;
  gameData: any;
  user1: any;
  user2: any;
  user1Inventory: any;
  user2Inventory: any;
  selectedCard2: any;
  selectedCard1: any;

  constructor(private http: HttpClient, private route: ActivatedRoute) { }

  ngOnInit() {
    this.route.paramMap.pipe(
      switchMap(params => {
        this.roomId = params.get('idRoom');
        return this.http.get('http://localhost:8888/rooms/' + this.roomId);
      }),
      switchMap(data => {
        this.gameData = data;
        return this.http.get('http://localhost:8888/users/' + this.gameData.room.idUser_1);
      }),
      switchMap(dataU1 => {
        this.user1 = dataU1;
        return this.http.get('http://localhost:8888/inventories/users/' + this.user1.id);
      }),
      switchMap(dataInv1 => {
        this.user1Inventory = dataInv1;
        return this.http.get('http://localhost:8888/users/' + this.gameData.room.idUser_2);
      }),
      switchMap(dataU2 => {
        this.user2 = dataU2;
        return this.http.get('http://localhost:8888/inventories/users/' + this.user2.id);
      })
    ).subscribe(dataInv2 => {
      this.user2Inventory = dataInv2;
    });
  }

  addCardToUser1(selectedCard1: any) {
    this.http.put('http://localhost:8888/rooms/'+this.roomId+'/users/1/cards/' + selectedCard1, null).subscribe(data => {
      this.ngOnInit();
    });
  }

  addCardToUser2(selectedCard2: any) {
    this.http.put('http://localhost:8888/rooms/'+this.roomId+'/users/1/cards/' + selectedCard2, null).subscribe(data => {
      this.ngOnInit();
    });
  }

}
