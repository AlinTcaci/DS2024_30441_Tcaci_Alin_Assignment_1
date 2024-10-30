import { Component, OnInit} from '@angular/core';
import {FormControl, FormGroup} from '@angular/forms';
import {UserService} from '../service/user/user.service';
import {Router} from '@angular/router';
import {AuthService} from '../service/auth/auth.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss'],
})
export class LoginComponent implements OnInit{

  constructor(
    private userService: UserService,
    private router: Router,
    private authService: AuthService
  ) {}

  loginForm: FormGroup = new FormGroup({});
  submitted = false;

  ngOnInit() {
    this.loginForm = new FormGroup({
      'login': new FormControl(''),
      'password': new FormControl('')
    });
  }

  onSubmit() {
    this.submitted = true;
    if (this.loginForm.valid) {
      const username = this.loginForm.get('login')?.value;
      this.userService.getUserByUsername(username).subscribe({
        next: (user) => {
          if (user.password === this.loginForm.get('password')?.value) {
            this.authService.setCurrentUser(user);
            if(user.role=='ADMIN') {
              this.router.navigateByUrl('/admin');
            }
            else if(user.role=='CLIENT'){
              this.router.navigateByUrl('/client');
            }
          } else {
            alert('Invalid password');
          }
        },
        error: (err) => {
          console.error('Error fetching user:', err);
          alert('User not found');
        }
      });
    } else {
      alert('Form is invalid');
    }
  }

}
