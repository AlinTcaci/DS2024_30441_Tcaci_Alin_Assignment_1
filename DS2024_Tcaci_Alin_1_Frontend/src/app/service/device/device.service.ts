import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {catchError, Observable, throwError} from 'rxjs';
import {Device} from '../../model/device/device.model';

@Injectable({
  providedIn: 'root'
})
export class DeviceService {

  private apiUrl = 'http://localhost:8081/devices';

  constructor(private http : HttpClient) {}

  getAllDevices(): Observable<Device[]> {
    const url = `${this.apiUrl}`+'/getAllDevices';
    return this.http.get<Device[]>(url).pipe(
      catchError((error: any) => {
        alert('Devices not found');
        return throwError(() => new Error(error));})
    );
  }

  getDevicesByUserId(userId: string): Observable<Device[]> {
    const url = `${this.apiUrl}`+'/getAllDevicesByUserId/'+userId;
    return this.http.get<Device[]>(url).pipe(
      catchError((error: any) => {
        alert('Devices not found');
        return throwError(() => new Error(error));})
    );
  }

  createDevice(device: Device): Observable<any> {
    const url = `${this.apiUrl}/createDevice`;
    return this.http.post(url, device, { responseType: 'text'}).pipe(
      catchError((error: any) => {
        console.error('Error in createDevice:', error);
        const errorMessage = error.error || 'Failed to create device';
        return throwError(() => new Error(errorMessage));
      })
    );
  }

  deleteDeviceById(id: string): Observable<any> {
    const url = `${this.apiUrl}/deleteDeviceById/${id}`;
    return this.http.delete(url, { responseType: 'text' }).pipe(
      catchError((error: any) => {
        console.error('Error deleting device:', error);
        const errorMessage = error.error || 'Failed to delete device';
        return throwError(() => new Error(errorMessage));
      })
    );
  }

  deleteAllDevicesByUserId(userId: string): Observable<any> {
    const url = `${this.apiUrl}/deleteAllDevicesByUserId/${userId}`;
    return this.http.delete(url, { responseType: 'text' }).pipe(
      catchError((error: any) => {
        console.error('Error deleting users devices:', error);
        const errorMessage = error.error || 'Failed to delete users devices';
        return throwError(() => new Error(errorMessage));
      })
    );
  }

  updateDeviceById(id: string, device: Device): Observable<any> {
    const url = `${this.apiUrl}/updateDeviceById/${id}`;
    return this.http.put(url, device, { responseType: 'text' }).pipe(
      catchError((error: any) => {
        console.error('Error updating device:', error);
        const errorMessage = error.error || 'Failed to update device';
        return throwError(() => new Error(errorMessage));
      })
    );
  }

}
