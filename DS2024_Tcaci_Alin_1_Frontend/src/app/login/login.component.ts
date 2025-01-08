import { Component, OnInit} from '@angular/core';
import {FormControl, FormGroup} from '@angular/forms';
import {UserService} from '../service/user/user.service';
import {Router} from '@angular/router';
import {AuthService} from '../service/auth/auth.service';
import {EncryptionService} from '../service/encryption/encryption.service';
import {User} from '../model/user/user.model';
import {UserLogin} from '../model/user/user-login.model';
import {switchMap, tap} from 'rxjs';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss'],
})
export class LoginComponent implements OnInit{
  username = '';
  password = '';
  users: User[] = [];

  constructor(
    private userService: UserService,
    private router: Router,
    private encryptService: EncryptionService,
    private authService: AuthService
  ) {}

  login(){
    let user = new UserLogin();
    user.username = this.loginForm.get('login')?.value;
    this.username = this.loginForm.get('login')?.value;
    user.password = this.loginForm.get('password')?.value;
    this.password = this.loginForm.get('password')?.value;
    console.log(user);
    this.authService.login(user).pipe(
      tap(token => this.userService.setToken(token)), // Set the token
      switchMap(() => this.userService.getAllUsers()) // Fetch users after setting the token
    ).subscribe({
      next: (users) => {
        this.users = users; // Update the users array
        this.checkLogin();
      },
      error: (error) => {
        console.error('Error during login or fetching users:', error);
        alert('Invalid username or password!');
      }
    });
  }

  checkLogin(): void {
    let isUserExist = false;
    for (let user of this.users) {
      if (user.username === this.username && user.password === this.password && user.role === 'CLIENT') {
        sessionStorage.setItem('userId', this.encryptService.encrypt(user.id.toString()));
        sessionStorage.setItem('userRole', this.encryptService.encrypt(user.role));
        sessionStorage.setItem('username', this.encryptService.encrypt(user.username));
        this.router.navigateByUrl('/client');
        isUserExist = true;
      } else if (user.username === this.username && user.password === this.password && user.role === 'ADMIN') {
        sessionStorage.setItem('userId', this.encryptService.encrypt(user.id.toString()));
        sessionStorage.setItem('userRole', this.encryptService.encrypt(user.role));
        sessionStorage.setItem('username', this.encryptService.encrypt(user.username));
        this.router.navigateByUrl('/admin');
        isUserExist = true;
      }
    }
    if (!isUserExist) {
      console.log('Invalid username or password!');
      alert('Invalid username or password!');
    }
  }

  loginForm: FormGroup = new FormGroup({});

  ngOnInit() {
    this.loginForm = new FormGroup({
      'login': new FormControl(''),
      'password': new FormControl('')
    });
  }


}
