import { Component } from '@angular/core';
import { Subject, throwError } from "rxjs";
import { HttpClient } from "@angular/common/http";
import { catchError } from "rxjs/operators";
import { Router } from '@angular/router'; // 1. Import the Router

@Component({
  selector: 'app-rooms',
  templateUrl: './rooms.component.html',
  styleUrls: ['./rooms.component.css']
})
export class RoomsComponent {
  rooms: any;
  dtOptions: DataTables.Settings = {};
  dtTrigger: Subject<any> = new Subject<any>();

  constructor(private http: HttpClient, private router: Router) { }

  ngOnInit() {
    this.dtOptions = {
      pagingType: 'full_numbers',
      pageLength: 10,
    };

    this.http.get('http://localhost:8080/rooms').subscribe((data) => {
      this.rooms = data;
      // @ts-ignore
      this.dtTrigger.next();
    });
  }

  join(idRoom: any) {
    let response = this.http.put('http://localhost:8080/rooms/join/' + idRoom + '/users/1', { id: idRoom }).pipe(
      catchError((error) => {
        if (error.status === 401) {
          alert("Vous ne pouvez pas rejoindre cette room. Il est possible que celle-ci soit déjà pleine ou que vous soyez déjà engagé dans une autre partie");
        }
        return throwError(error);
      })
    );

    // 3. Subscribe to the response observable and use the navigate() method
    response.subscribe(() => {
      this.router.navigate(['/game', idRoom]);
    });
  }
  view(idRoom: any) {
    this.router.navigate(['/game', idRoom]);
  }
}
