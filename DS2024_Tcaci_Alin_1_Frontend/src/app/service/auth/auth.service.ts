import { Injectable } from '@angular/core';
import { User } from '../../model/user/user.model';
import {HttpClient} from '@angular/common/http';
import {UserService} from '../user/user.service';
import {DeviceService} from '../device/device.service';
import {tap} from 'rxjs';
import {UserLogin} from '../../model/user/user-login.model';
import {MonitoringService} from '../monitor/monitoring.service';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  // private currentUser: User | null = null;
  //
  // constructor() {}
  //
  // setCurrentUser(user: User): void {
  //   this.currentUser = user;
  // }
  //
  // getCurrentUser(): User | null {
  //   return this.currentUser;
  // }
  //
  // logout(): void {
  //   this.currentUser = null;
  // }

  baseUrl: string = 'http://user.localhost/users';
  // baseUrl: string = 'http://localhost:8080/users';

  constructor(
    private httpClient: HttpClient,
    private userService: UserService,
    private deviceService: DeviceService,
    private monitoringService: MonitoringService
  ) {}

  login(user: UserLogin) {
    return this.httpClient.post(this.baseUrl + '/login', {
      username: user.username,
      password: user.password
    }, {
      headers: {
        'Content-Type': 'application/json'
      },
      responseType: 'text'
    }).pipe(
      tap((response: string) => {
        this.userService.setToken(response);
        this.deviceService.setToken(response);
        this.monitoringService.setToken(response);
      })
    );
  }



}
