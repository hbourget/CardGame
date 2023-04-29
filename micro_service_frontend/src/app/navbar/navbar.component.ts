// navbar.component.ts
import { Component, OnInit } from '@angular/core';
import { AuthService } from '../auth.service';
import { Observable } from 'rxjs';
import { User } from '../auth.service';

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.css'],
})
export class NavbarComponent implements OnInit {
  user$: Observable<User | null>;

  constructor(private authService: AuthService) {
    this.user$ = this.authService.user$;
  }

  ngOnInit() {
    const token = this.authService.getAccessToken();
    if (token && !this.authService.isTokenExpired(token)) {
      this.authService.getUserData();
    }
  }
}
