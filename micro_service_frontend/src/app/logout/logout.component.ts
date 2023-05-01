import {Component, OnInit} from '@angular/core';
import {AuthService} from "../auth.service";
import { User } from '../auth.service';
import {Observable} from "rxjs";
import {HttpClient} from "@angular/common/http";
import {Router} from "@angular/router";

@Component({
  selector: 'app-logout',
  templateUrl: './logout.component.html',
  styleUrls: ['./logout.component.css']
})
export class LogoutComponent implements OnInit {
  user$: Observable<User | null>;

  constructor(private http: HttpClient, private authService: AuthService, private router: Router) {
    this.user$ = this.authService.user$;
  }

  ngOnInit(): void {
    this.http.post('http://localhost:8080/auth/logout', {access_token: this.authService.getAccessToken()});
    this.authService.removeTokens();
    this.authService.userSubject.next(null);
    this.router.navigate(['/login']);
  }
}
