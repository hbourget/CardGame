// game.component.ts
import { Component, OnDestroy, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import {catchError, switchMap} from 'rxjs/operators';
import { AuthService, User } from '../auth.service';
import {throwError} from 'rxjs';

@Component({
  selector: 'app-game',
  templateUrl: './game.component.html',
  styleUrls: ['./game.component.css']
})
export class GameComponent implements OnInit, OnDestroy {
  roomId: any;
  gameData: any;
  user1: any;
  user2: any;
  user1Inventory: any;
  user2Inventory: any;
  selectedCard2: any;
  selectedCard1: any;
  user: User | null = null;
  userSubscription: any;
  cooldownMessage: string | null = null;
  winningMessage: string | null = null;

  constructor(private http: HttpClient, private route: ActivatedRoute, private authService: AuthService) { }

  ngOnInit() {
    this.userSubscription = this.authService.user$.subscribe((user) => {
      this.user = user;
      if (user) {
        const token = this.authService.getAccessToken();
        const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);
        this.route.paramMap.pipe(
          switchMap(params => {
            this.roomId = params.get('idRoom');
            return this.http.get('http://proxyauth:8080/rooms/' + this.roomId, { headers });
          }),
          switchMap(data => {
            this.gameData = data;
            return this.http.get('http://proxyauth:8080/users/' + this.gameData.room.idUser_1, { headers });
          }),
          switchMap(dataU1 => {
            this.user1 = dataU1;
            return this.http.get('http://proxyauth:8080/inventories/users/' + this.user1.id, { headers });
          }),
          switchMap(dataInv1 => {
            this.user1Inventory = dataInv1;
            return this.http.get('http://proxyauth:8080/users/' + this.gameData.room.idUser_2, { headers });
          }),
          switchMap(dataU2 => {
            this.user2 = dataU2;
            return this.http.get('http://proxyauth:8080/inventories/users/' + this.user2.id, { headers });
          })
        ).subscribe(dataInv2 => {
          this.user2Inventory = dataInv2;
        });
      }
    });
  }

  ngOnDestroy() {
    this.userSubscription.unsubscribe();
  }

  addCardToUser1(selectedCard1: any) {
    const token = this.authService.getAccessToken();
    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);
    if(this.user?.username != this.user1.username) { alert("Vous n'êtes pas " + this.user1.username); return; }
    this.http.put('http://proxyauth:8080/rooms/'+this.roomId+'/users/'+this.user1.id+'/cards/' + selectedCard1, null, { headers }).subscribe(data => {
      this.ngOnInit();
    });
  }

  addCardToUser2(selectedCard2: any) {
    const token = this.authService.getAccessToken();
    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);
    if(this.user?.username != this.user2.username) { alert("Vous n'êtes pas " + this.user2.username); return; }
    this.http.put('http://proxyauth:8080/rooms/' + this.roomId + '/users/' + this.user2.id + '/cards/' + selectedCard2, null, { headers }).subscribe(data => {
      this.ngOnInit();
    });
  }

  attack() {
    const token = this.authService.getAccessToken();
    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);
    this.http.put('http://proxyauth:8080/rooms/play/' + this.roomId + '/users/' + this.user?.username, null, { headers })
      .pipe(
        catchError((error) => {
          if (error.status === 401) {
            this.cooldownMessage = "Vous avez déjà joué ce tour.";
          }
          else if (error.status === 409) {
            this.cooldownMessage = "La partie est terminé.";
          }
          return throwError(error);
        })
      )
      .subscribe(data => {
        this.authService.getUserData();
        this.ngOnInit();
      });
  }
}

