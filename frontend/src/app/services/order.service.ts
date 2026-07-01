import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Order } from '../models/order.model';
import { environment } from '../../environments/environment';

@Injectable({
    providedIn: 'root'
})
export class OrderService {
    private http = inject(HttpClient);
    private apiUrl = `${environment.apiUrl}/api/v1`;

    getOrderById(id: number): Observable<Order> {
        return this.http.get<Order>(`${this.apiUrl}/orders/${id}`);
    }

    getCustomerOrders(customerId: number): Observable<Order[]> {
        return this.http.get<Order[]>(`${this.apiUrl}/orders/customer/${customerId}`);
    }

    getAllOrders(): Observable<Order[]> {
        return this.http.get<Order[]>(`${this.apiUrl}/orders`);
    }
}
