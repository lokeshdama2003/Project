import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { EventService } from '../services/event.service';
import { AuthService } from '../services/auth';
import { CommunityEvent } from '../models/event.model';
import { FilterPipe } from '../shared/filter.pipe';
import { ButtonComponent } from '../shared/button/button';

@Component({
  selector: 'app-attendee',
  standalone: true,
  imports: [CommonModule, ButtonComponent],
  templateUrl: './attendee.html'
})
export class AttendeeComponent implements OnInit {
  events: CommunityEvent[] = [];
  searchText = '';
  errorMessage = '';

  constructor(
    private eventService: EventService,
    private authService: AuthService,
    private router: Router
  ) {}
  ngOnInit(): void {
    this.eventService.getEvents().subscribe({
      next: (data) => {
        this.events = data;
      },
      error: (error) => {
        console.error('Unable to load events:', error);
        this.errorMessage =
          'Unable to load events. Check that JSON Server is running.';
      }
    });
  }
  get visibleEvents(): CommunityEvent[] {
    const openEvents = this.events.filter(
      (event) => event.status === 'Open'
    );

    return new FilterPipe().transform(openEvents, this.searchText);
  }

  logout(): void {
    this.authService.logout();
    void this.router.navigate(['/login']);
  }
}
