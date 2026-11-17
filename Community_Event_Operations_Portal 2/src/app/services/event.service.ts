import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { CommunityEvent } from '../models/event.model';

@Injectable({ providedIn: 'root' })
export class EventService {
  private readonly apiUrl = 'http://localhost:3000/events';

  constructor(private http: HttpClient) {}

  getEvents(): Observable<CommunityEvent[]> {
    return this.http.get<CommunityEvent[]>(this.apiUrl);
  }
  addEvent(event: CommunityEvent): Observable<CommunityEvent> {
    return this.http.post<CommunityEvent>(this.apiUrl, event);
  }
  updateEvent(
    id: number,
    event: CommunityEvent
  ): Observable<CommunityEvent> {
    return this.http.put<CommunityEvent>(`${this.apiUrl}/${id}`, event);
  }
  deleteEvent(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}
