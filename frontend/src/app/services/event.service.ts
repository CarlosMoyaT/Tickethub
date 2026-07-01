import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { EventModel } from '../models/event.model';
import { environment } from 'src/environments/environment';

@Injectable({
    providedIn: 'root'
})
export class EventService {
    private http = inject(HttpClient);
    private apiUrl = `${environment.apiUrl}/api/events`;

    getEvents(): Observable<EventModel[]> {
        return this.http.get<EventModel[]>(this.apiUrl);
    }

    getEventById(id: number): Observable<EventModel> {
        return this.http.get<EventModel>(`${this.apiUrl}/${id}`);
    }

    getAvailableEvents(): Observable<EventModel[]> {
        return this.http.get<EventModel[]>(`${this.apiUrl}/available`);
    }
}