import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";

@Injectable({
  providedIn: 'root'
})
export class JwtClientService {

  constructor(private http:HttpClient) { }

  public generateToken(request: any){
    return this.http.post("http://localhost:8080/login", request, {responseType: "text" as "json"});
  }

  public welcome(token: string){
    let tokenStr = 'Bearer '+token;
    const headers = { 'Authorization': tokenStr }
    return this.http.get("http://localhost:8080/", {headers, responseType: "text" as "json"});
  }

}
