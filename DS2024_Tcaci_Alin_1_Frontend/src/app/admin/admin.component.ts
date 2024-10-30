import {Component, OnInit} from '@angular/core';
import {Router} from '@angular/router';
import {UserService} from '../service/user/user.service';
import {User} from '../model/user/user.model';
import {DeviceService} from '../service/device/device.service';
import {Device} from '../model/device/device.model';
import {AuthService} from '../service/auth/auth.service';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';

@Component({
  selector: 'app-admin',
  templateUrl: './admin.component.html',
  styleUrls: ['./admin.component.scss']
})
export class AdminComponent implements OnInit{

  newUserForm: FormGroup;
  newDeviceForm: FormGroup;

  currentUser: User | null = null;
  users: User[] = [];
  clientUsers: User[] = [];
  devices: Device[] = [];

  editingUserId: string | null = null;
  userEditData: Partial<User> = {};

  editingDeviceId: string | null = null;
  deviceEditData: Partial<Device> = {};

  userIdToUsername: Map<string, string> = new Map<string, string>();


  constructor(
    private authService: AuthService,
    private userService: UserService,
    private deviceService: DeviceService,
    private router: Router,
    private fb: FormBuilder
  ) {
    this.newUserForm = this.fb.group({
      username: ['', Validators.required],
      password: ['', Validators.required],
      role: ['CLIENT', Validators.required]
    });

    this.newDeviceForm = this.fb.group({
      userId: ['', Validators.required],
      description: ['', Validators.required],
      address: ['', Validators.required],
      maxEnergyHourly: ['', [Validators.required, Validators.min(0)]]
    });
  }

  ngOnInit() {
    this.currentUser = this.authService.getCurrentUser();
    if (this.currentUser) {
      this.loadAllUsers();
      this.loadAllDevices();
    } else {
      this.logout();
      alert('User not logged in');
    }
  }

  loadAllUsers(): void {
    this.userService.getAllUsers().subscribe({
      next: (data: User[]) => {
        this.users = data;
        this.clientUsers = this.users.filter(user => user.role === 'CLIENT');
        this.userIdToUsername.clear();
        this.users.forEach((user) => {
          this.userIdToUsername.set(user.id, user.username);
        });
        this.loadAllDevices();
      },
      error: (err) => {
        console.error('Error fetching users:', err);
        alert('Failed to load users');
      }
    });
  }

  loadAllDevices(): void {
    this.deviceService.getAllDevices().subscribe({
      next: (data: Device[]) => {
        this.devices = data.filter(device => {
          const user = this.users.find(u => u.id === device.userId);
          return user && user.role === 'CLIENT';
        });
      },
      error: (err) => {
        console.error('Error fetching devices:', err);
        alert('Failed to load devices');
      }
    });
  }

  createUser(): void {
    if (this.newUserForm.valid) {
      const user: User = this.newUserForm.value;
      this.userService.createUser(user).subscribe({
        next: () => {
          alert('User created successfully');
          this.loadAllUsers();
          this.newUserForm.reset({ role: 'CLIENT' });
        },
        error: (err: any) => {
          console.error('Error creating user:', err);
          alert('Failed to create user');
        }
      });
    } else {
      alert('Please fill in all required fields');
    }
  }

  editUser(user: User): void {
    this.editingUserId = user.id;
    this.userEditData = { ...user };
  }

  saveUser(user: User): void {
    const updatedUser: User = {
      ...user,
      ...this.userEditData,
    };

    this.userService.updateUserById(updatedUser.id, updatedUser).subscribe({
      next: () => {
        alert('User updated successfully');
        this.editingUserId = null;
        this.loadAllUsers();
        this.loadAllDevices();
      },
      error: (err: any) => {
        console.error('Error updating user:', err);
        alert(`Failed to update user: ${err.message}`);
      },
    });
  }

  cancelUserEdit(): void {
    this.editingUserId = null;
    this.userEditData = {};
  }

  createDevice(): void {
    if (this.newDeviceForm.valid) {
      const device: Device = this.newDeviceForm.value;
      const user = this.users.find(u => u.id === device.userId);

      if (user && user.role === 'CLIENT') {
        this.deviceService.createDevice(device).subscribe({
          next: () => {
            alert('Device created successfully');
            this.loadAllDevices();
            this.newDeviceForm.reset();
          },
          error: (err) => {
            console.error('Error creating device:', err);
            alert(`Failed to create device: ${err.message}`);
          },
        });
      } else {
        alert('Selected user is not a CLIENT');
      }
    } else {
      alert('Please fill in all required fields');
    }
  }

  deleteDevice(deviceId: string): void {
    if (confirm('Are you sure you want to delete this device?')) {
      this.deviceService.deleteDeviceById(deviceId).subscribe({
        next: () => {
          alert('Device deleted successfully');
          this.loadAllDevices();
        },
        error: (err) => {
          console.error('Error deleting device:', err);
          alert(`Failed to delete device: ${err.message}`);
        },
      });
    }
  }

  deleteUser(userId: string): void {
    if (confirm('Are you sure you want to delete this user and all their devices?')) {
      this.deviceService.deleteAllDevicesByUserId(userId).subscribe({
        next: () => {
          this.userService.deleteUserById(userId).subscribe({
            next: () => {
              alert('User and their devices deleted successfully');
              this.loadAllUsers();
              this.loadAllDevices();
            },
            error: (err) => {
              console.error('Error deleting user:', err);
              alert(`Failed to delete user: ${err.message}`);
            },
          });
        },
        error: (err: any) => {
          console.error('Error deleting user devices:', err);
          alert(`Failed to delete user's devices: ${err.message}`);
        },
      });
    }
  }

  editDevice(device: Device): void {
    this.editingDeviceId = device.id;
    this.deviceEditData = { ...device };
  }

  saveDevice(device: Device): void {
    const updatedDevice: Device = {
      ...device,
      ...this.deviceEditData,
    };

    const user = this.users.find(u => u.id === updatedDevice.userId);

    if (user && user.role === 'CLIENT') {
      this.deviceService.updateDeviceById(updatedDevice.id, updatedDevice).subscribe({
        next: () => {
          alert('Device updated successfully');
          this.editingDeviceId = null;
          this.loadAllDevices();
        },
        error: (err) => {
          console.error('Error updating device:', err);
          alert(`Failed to update device: ${err.message}`);
        },
      });
    } else {
      alert('Selected user is not a CLIENT');
    }
  }

  cancelDeviceEdit(): void {
    this.editingDeviceId = null;
    this.deviceEditData = {};
  }

  logout(): void {
    this.authService.logout();
    this.router.navigateByUrl('/login');
  }
}
