import { Injectable } from '@angular/core';
import {catchError, Observable, throwError} from 'rxjs';
import {User} from '../../model/user/user.model';
import {HttpClient} from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  private apiUrl = 'http://localhost:8080/users';

  constructor(private http : HttpClient) {}

  getUserByUsername(username: string): Observable<User> {
    const url = `${this.apiUrl}`+'/getUserByUsername/'+`${username}`;
    return this.http.get<User>(url).pipe(
      catchError((error: any) => {
        alert('User not found');
        return throwError(() => new Error(error));})
    );
  }

  getAllUsers(): Observable<User[]> {
    const url = `${this.apiUrl}`+'/getAllUsers';
    return this.http.get<User[]>(url).pipe(
      catchError((error: any) => {
        alert('Users not found');
        return throwError(() => new Error(error));})
    );
  }

  createUser(user: User): Observable<any> {
    const url = `${this.apiUrl}/createUser`;
    return this.http.post(url, user, { responseType: 'text' }).pipe(
      catchError((error: any) => {
        console.error('Error in createUser:', error);
        const errorMessage = error.error || 'Failed to create user';
        return throwError(() => new Error(errorMessage));
      })
    );
  }

  deleteUserById(id: string): Observable<any> {
    const url = `${this.apiUrl}/deleteUserById/${id}`;
    return this.http.delete(url, { responseType: 'text' }).pipe(
      catchError((error: any) => {
        console.error('Error deleting user:', error);
        const errorMessage = error.error || 'Failed to delete user';
        return throwError(() => new Error(errorMessage));
      })
    );
  }

  updateUserById(id: string, user: User): Observable<any> {
    const url = `${this.apiUrl}/updateUserById/${id}`;
    return this.http.put(url, user, { responseType: 'text' }).pipe(
      catchError((error: any) => {
        console.error('Error updating user:', error);
        const errorMessage = error.error || 'Failed to update user';
        return throwError(() => new Error(errorMessage));
      })
    );
  }

}
