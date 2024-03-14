import { HttpClient  } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { environment } from './enviroment';


@Injectable({
  providedIn: 'root'
})
export class AuthService {
  apiUrl = environment.apiUrl;
  constructor(private http: HttpClient, private router: Router) {}

  login(username: string, password: string) {
    this.http.post(`${this.apiUrl}/user/login`, {username, password}).subscribe((response: any) => {
      if (response.role.name === 'ADMIN') {
        this.router.navigate(['/dashboard']);
      } else {
        this.router.navigate(['/welcome']);
      }
    }, (error) => {
      console.error(error);
    });
  }
}
