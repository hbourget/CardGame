// register.component.ts
import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router'; // Import the Router

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

  constructor(private http: HttpClient, private router: Router) { } // Inject the Router

  ngOnInit(): void {
  }

  onSubmit(form: { valid: any; }) {
    if (form.valid) {
      this.http.post('http://localhost:8080/auth/register', this.user).subscribe(
        (data: any) => {
          // Redirect to the login page
          this.router.navigate(['/login']);
        },
        (error) => {
          console.error(error);
          // Show an error message or handle the error accordingly
        }
      );
    }
  }
}
