import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { EventModel } from '../models/event.model';
import { environment } from '../../environments/environment';

@Injectable({
    providedIn: 'root'
})
export class EventService {
    private http = inject(HttpClient);
    private apiUrl = `${environment.apiUrl}/api/v1/inventory`;

    getEvents(): Observable<EventModel[]> {
        return this.http.get<EventModel[]>(`${this.apiUrl}/events`);
    }

    getEventById(id: number): Observable<EventModel> {
        return this.http.get<EventModel>(`${this.apiUrl}/event/${id}`);
    }

    getAvailableEvents(): Observable<EventModel[]> {
        return this.http.get<EventModel[]>(`${this.apiUrl}/events`);
    }
}