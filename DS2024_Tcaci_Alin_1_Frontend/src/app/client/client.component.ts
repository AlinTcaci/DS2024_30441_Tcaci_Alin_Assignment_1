import {Component, OnInit} from '@angular/core';
import {AuthService} from '../service/auth/auth.service';
import {DeviceService} from '../service/device/device.service';
import {User} from '../model/user/user.model';
import {Device} from '../model/device/device.model';
import {Router} from '@angular/router';

@Component({
  selector: 'app-client',
  templateUrl: './client.component.html',
  styleUrls: ['./client.component.scss']
})
export class ClientComponent implements OnInit{

  constructor(
    private authService: AuthService,
    private deviceService: DeviceService,
    private router: Router
  ) {}

  currentUser: User | null = null;
  devices: Device[] = [];

  ngOnInit() {
    this.currentUser = this.authService.getCurrentUser();
    if (this.currentUser) {
      this.loadUserDevices(this.currentUser.id);
    } else {
      this.logout();
      alert('User not logged in');
    }
  }

  loadUserDevices(userId: string): void {
    this.deviceService.getDevicesByUserId(userId).subscribe({
      next: (data: Device[]) => {
        this.devices = data;
      },
      error: (err: any) => {
        console.error('Error fetching devices:', err);
        alert('Failed to load devices');
      },
    });
  }

  logout(): void {
    this.authService.logout();
    this.router.navigateByUrl('/login');
  }

}
