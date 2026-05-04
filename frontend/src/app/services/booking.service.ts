import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { BookingRequest, BookingResponse } from '../models/booking.model';
import { environment } from '../../environments/environment';

@Injectable({
    providedIn: 'root'
})
export class BookingService {
    private http = inject(HttpClient);
    private apiUrl = `${environment.apiUrl}/api/v1`;

    createBooking(booking: BookingRequest): Observable<BookingResponse> {
        return this.http.post<BookingResponse>(`${this.apiUrl}/booking`, booking);
    }
}
