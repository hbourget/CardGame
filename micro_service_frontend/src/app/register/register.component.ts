import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent implements OnInit {
  user = {
    username: '',
    password: ''
  };

  constructor(private http: HttpClient, private router: Router) { }

  ngOnInit(): void {
  }

  onSubmit(form: { valid: any; }) {
    if (form.valid) {
      this.http.post('http://proxyauth:8080/auth/register', this.user).subscribe(
        (data: any) => {
          this.router.navigate(['/login']);
        },
        (error) => {
          console.error(error);
        }
      );
    }
  }
}
