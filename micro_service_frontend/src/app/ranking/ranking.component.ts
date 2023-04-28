import { Component } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {catchError} from "rxjs/operators";
import {Subject, throwError} from "rxjs";

@Component({
  selector: 'app-ranking',
  templateUrl: './ranking.component.html',
  styleUrls: ['./ranking.component.css']
})
export class RankingComponent {
  users:any
  dtOptions: DataTables.Settings = {};
  dtTrigger: Subject<any> = new Subject<any>();
  constructor(private http:HttpClient) {
  }

  ngOnInit() {
    this.dtOptions = {
      pagingType: 'full_numbers',
      pageLength: 10,
      order: [[1, 'desc']] // Sort by balance in descending order
    };

    this.http.get('http://localhost:8080/users').subscribe((data) => {
      this.users = data;
      // @ts-ignore
      this.dtTrigger.next();
    });
  }
}
