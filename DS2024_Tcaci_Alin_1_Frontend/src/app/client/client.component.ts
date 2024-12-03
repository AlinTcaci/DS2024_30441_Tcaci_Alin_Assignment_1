// @ts-ignore

import {AfterViewInit, Component, ElementRef, OnInit, ViewChild} from '@angular/core';
import {AuthService} from '../service/auth/auth.service';
import {DeviceService} from '../service/device/device.service';
import {User} from '../model/user/user.model';
import {Device} from '../model/device/device.model';
import {Router} from '@angular/router';
import {WebSocketService} from '../service/socket/web-socket.service';
import {Chart, ChartConfiguration, registerables} from 'chart.js';
import {Monitoring} from '../model/monitor/monitoring.model';
import {MonitoringService} from '../service/monitor/monitoring.service';
import 'chartjs-adapter-date-fns'; // Add this import

@Component({
  selector: 'app-client',
  templateUrl: './client.component.html',
  styleUrls: ['./client.component.scss']
})
export class ClientComponent implements OnInit, AfterViewInit{
  @ViewChild('monitoringChart', { static: false }) monitoringChart!: ElementRef<HTMLCanvasElement>;
  chart!: Chart;

  constructor(
    private authService: AuthService,
    private deviceService: DeviceService,
    private router: Router,
    private webSocketService: WebSocketService,
    private monitoringService: MonitoringService
  ) {}

  currentUser: User | null = null;
  devices: Device[] = [];
  energyConsumption: Monitoring[] = [];

  selectedDate: string | null = null; // Holds the selected date from the input
  filteredData: Monitoring[] = []; // Holds filtered data for the chart


  ngOnInit() {
    this.currentUser = this.authService.getCurrentUser();
    console.log(this.currentUser);
    // this.getAllMonitorings();
    this.webSocketService.connect('ws://monitoring.localhost/notifications');
    this.webSocketService.getMessages().subscribe((rawMessage) => {
      const message = typeof rawMessage === 'string' ? JSON.parse(rawMessage) : rawMessage;

      if (message.user_id === this.currentUser?.id) {
        alert('Alert: Message for you!\nDeviceId: ' + message.device_id);
      }
      console.log(message);
    });
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

  ngAfterViewInit() {
    setTimeout(() => {
      this.initializeChart();
    });
  }


  logout(): void {
    this.authService.logout();
    this.router.navigateByUrl('/login');
  }

  initializeChart() {
    Chart.register(...registerables);
    this.energyConsumption.sort((a, b) => new Date(a.timestamp).getTime() - new Date(b.timestamp).getTime());

    const ctx = this.monitoringChart.nativeElement.getContext('2d');
    if (!ctx) {
      console.error('Chart context not found');
      return;
    }
    this.chart = new Chart(ctx, {
      type: 'line',
      data: {
        labels: this.energyConsumption.map(data => new Date(data.timestamp)),
        datasets: [{
          label: 'Measurement Value',
          data: this.energyConsumption.map(data => data.measurement_value),
          borderColor: 'rgba(75, 192, 192)',
          borderWidth: 1,
          fill: false
        }]
      },
      options: {
        scales: {
          x: {
            type: 'time',
            time: {
              unit: 'second'
            }
          },
          y: {
            beginAtZero: true
          }
        },
      }
    });
  }


  // getAllMonitorings(): void {
  //   this.monitoringService.getAllMonitorings().subscribe({
  //     next: (data: Monitoring[]) => {
  //       console.log(data);
  //       this.energyConsumption = data;
  //     },
  //     error: (err) => {
  //       console.error('Failed to fetch all monitorings:', err);
  //     },
  //   });
  // }

  getDeviceMonitorings(deviceId: string): void {
    this.monitoringService.getAllMonitoringsByDeviceId(deviceId).subscribe({
      next: (data: Monitoring[]) => {
        console.log(data); // To verify the data in the console
        this.energyConsumption = data;
        this.updateChart(); // Update the existing chart
      },
      error: (err) => {
        console.error('Failed to fetch monitorings for device:', err);
        alert('Failed to load monitorings for this device');
      },
    });
  }

  updateChart(): void {
    if (this.chart) {
      // Sort data by timestamp
      this.energyConsumption.sort((a, b) => new Date(a.timestamp).getTime() - new Date(b.timestamp).getTime());

      this.chart.data.labels = this.energyConsumption.map(data => new Date(data.timestamp)); // Sorted timestamps
      this.chart.data.datasets[0].data = this.energyConsumption.map(data => data.measurement_value); // Sorted values
      this.chart.update();
    } else {
      console.error('Chart not initialized');
    }
  }

  filterByDate(): void {
    if (!this.selectedDate) {
      alert('Please select a date.');
      return;
    }

    // Convert selectedDate to start and end timestamps
    const startOfDay = new Date(this.selectedDate);
    const endOfDay = new Date(this.selectedDate);
    endOfDay.setHours(23, 59, 59, 999);

    // Filter data within the selected date range
    this.filteredData = this.energyConsumption.filter(data => {
      const timestamp = new Date(data.timestamp).getTime();
      return timestamp >= startOfDay.getTime() && timestamp <= endOfDay.getTime();
    });

    // Update the chart with the filtered data
    this.updateChartWithFilteredData();
  }

  updateChartWithFilteredData(): void {
    if (this.chart) {
      // Update chart with filtered data
      this.chart.data.labels = this.filteredData.map(data => new Date(data.timestamp));
      this.chart.data.datasets[0].data = this.filteredData.map(data => data.measurement_value);
      this.chart.update();
    } else {
      console.error('Chart not initialized');
    }
  }


}
