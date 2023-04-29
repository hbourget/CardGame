// rooms.component.ts
import { Component, OnDestroy, OnInit } from '@angular/core';
import { throwError } from "rxjs";
import {HttpClient, HttpHeaders} from "@angular/common/http";
import { catchError } from "rxjs/operators";
import { Router } from '@angular/router';
import { AuthService, User } from '../auth.service';

@Component({
  selector: 'app-rooms',
  templateUrl: './rooms.component.html',
  styleUrls: ['./rooms.component.css']
})
export class RoomsComponent implements OnInit, OnDestroy {
  rooms: any;
  user: User | null = null;
  userSubscription: any;
  roomName: any;

  constructor(private http: HttpClient, private router: Router, private authService: AuthService) { }

  ngOnInit() {

    this.userSubscription = this.authService.user$.subscribe((user) => {
      this.user = user;
      if (user) {
        const token = this.authService.getAccessToken();
        const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);
        this.http.get('http://localhost:8080/rooms', { headers }).subscribe((data) => {
          this.rooms = data;
        });
      }
    });
  }

  ngOnDestroy() {
    this.userSubscription.unsubscribe();
  }

  join(idRoom: any) {
    if (this.user) {
      const token = this.authService.getAccessToken();
      const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);
      let response = this.http
        .put(`http://localhost:8080/rooms/join/${idRoom}/users/${this.user.username}`, { id: idRoom }, { headers })
        .pipe(
          catchError((error) => {
            if (error.status === 401) {
              alert("Vous ne pouvez pas rejoindre cette room. Il est possible que celle-ci soit déjà pleine ou que vous soyez déjà engagé dans une autre partie");
            }
            return throwError(error);
          })
        );

      response.subscribe(() => {
        this.router.navigate(['/game', idRoom]);
      });
    }
  }

  leave(idRoom: any) {
    if (this.user) {
      const token = this.authService.getAccessToken();
      const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);
      let response = this.http
        .put(`http://localhost:8080/rooms/leave/${idRoom}/users/${this.user.username}`, { id: idRoom }, { headers })
        .pipe(
          catchError((error) => {
            if (error.status === 401) {
              alert("Vous n'êtes pas dans cette room.");
            }
            return throwError(error);
          })
        );

      response.subscribe(() => {
        this.ngOnInit();
      });
    }
  }

  view(idRoom: any) {
    this.router.navigate(['/game', idRoom]);
  }

  create() {
    if (this.user) {
      const token = this.authService.getAccessToken();
      const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);
      let response = this.http
        .post(`http://localhost:8080/rooms`, { name: this.roomName }, { headers })
        .pipe(
          catchError((error) => {
            return throwError(error);
          })
        );

      response.subscribe(() => {
        this.ngOnInit();
      });
    }
  }
}
