import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { AuthService } from '../auth.service'; // Import the AuthService
import { Router } from '@angular/router'; // Import the Router

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {
  user = {
    username: '',
    password: ''
  };
  loginError = false; // Variable to track login error

  constructor(private http: HttpClient, private authService: AuthService, private router: Router) { }

  ngOnInit(): void {
  }

  onSubmit(form: { valid: any; }) {
    if (form.valid) {
      this.http.post('http://localhost:8080/auth/login', this.user).subscribe(
        (data: any) => {
          this.authService.setAccessToken(data.access_token);
          this.authService.setRefreshToken(data.refresh_token);
          this.authService.getUserData();
          this.router.navigate(['/home']);
        },
        (error) => {
          console.error(error);
          this.loginError = true; // Set login error to true
        }
      );
    }
  }
}
