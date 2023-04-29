// auth.guard.ts
import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot, UrlTree } from '@angular/router';
import { Observable } from 'rxjs';
import { AuthService } from './auth.service'; // Import the AuthService

@Injectable({
  providedIn: 'root'
})
export class AuthGuard implements CanActivate {

  constructor(private authService: AuthService, private router: Router) { } // Inject the AuthService and Router

  canActivate(
    route: ActivatedRouteSnapshot,
    state: RouterStateSnapshot): Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {
    const token = this.authService.getAccessToken();
    if (token && !this.authService.isTokenExpired(token)) {
      return true;
    }
    // Redirect to the login page if not authenticated or token is expired
    this.router.navigate(['/login']);
    return false;
  }
}
