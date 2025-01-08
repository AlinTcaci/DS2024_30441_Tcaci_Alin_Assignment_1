import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import {catchError, Observable, throwError} from 'rxjs';
import {Device} from '../../model/device/device.model';
import {Monitoring} from '../../model/monitor/monitoring.model';

@Injectable({
  providedIn: 'root'
})
export class MonitoringService {
  private apiUrl = 'http://monitoring.localhost/monitor';
  // private apiUrl = 'http://localhost:8082/monitor';

  private token: string | null = null;

  setToken(token: string): void {
    this.token = token;
  }

  constructor(private http: HttpClient) {}

  // getDevicesByUserId(userId: string): Observable<Device[]> {
  //   const url = `${this.apiUrl}`+'/getAllDevicesByUserId/'+userId;
  //   return this.http.get<Device[]>(url).pipe(
  //     catchError((error: any) => {
  //       alert('Devices not found');
  //       return throwError(() => new Error(error));})
  //   );
  // }

  getAllMonitoringsByDeviceId(deviceId: string): Observable<Monitoring[]> {
    const url = `${this.apiUrl}`+'/getAllMonitoringsByDeviceId/'+deviceId;
    return this.http.get<Monitoring[]>(url,
      {
        headers:{
          'Content-Type': 'application/json',
          'Authorization': 'Bearer ' + this.token
        }
      }).pipe(
      catchError((error: any) => {
        alert('Monitorings not found');
        return throwError(() => new Error(error));})
    );
  }

  getEnergyConsumptionByDay(deviceId: string, date: string): Observable<Monitoring[]> {
    const url = `${this.apiUrl}/getEnergyConsumptionByDay/${deviceId}/${date}`;
    return this.http.get<Monitoring[]>(url).pipe(
      catchError((error: any) => {
        alert('Energy consumption data not found');
        return throwError(() => new Error(error));
      })
    );
  }

  getAllMonitorings(): Observable<Monitoring[]> {
    const url = `${this.apiUrl}`+'/getAllMonitorings';
    return this.http.get<Monitoring[]>(url).pipe(
      catchError((error: any) => {
        alert('Monitorings not found');
        return throwError(() => new Error(error));})
    );
  }

}
