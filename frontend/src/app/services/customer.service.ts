import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';

export interface Customer {
  id: number;
  name: string;
  email: string;
  address: string;
}

export interface CustomerRequest {
  name: string;
  email: string;
  address?: string;
}

@Injectable({
    providedIn: 'root'
})
export class CustomerService {
    private http = inject(HttpClient);
    private apiUrl = `${environment.apiUrl}/api/v1/customers`;

    findByEmail(email: string): Observable<Customer> {
        return this.http.get<Customer>(`${this.apiUrl}/email/${email}`);
    }

    create(request: CustomerRequest): Observable<Customer> {
        return this.http.post<Customer>(this.apiUrl, request);
    }
}
