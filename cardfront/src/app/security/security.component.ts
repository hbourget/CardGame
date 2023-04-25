import {Component, OnInit} from '@angular/core';
import {JwtClientService} from "../jwt-client.service";

@Component({
  selector: 'app-security',
  templateUrl: './security.component.html',
  styleUrls: ['./security.component.css']
})
export class SecurityComponent implements OnInit{

  authRequest: any = {
    "username": "hugo",
    "password": "hugo"
  }
  constructor(private service: JwtClientService) {
  }
  ngOnInit() {
    this.getAccessToken(this.authRequest);
  }

  public getAccessToken(request: any){
    let resp = this.service.generateToken(request);
    resp.subscribe(data=>console.log(data));
  }
}
