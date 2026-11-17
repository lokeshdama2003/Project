import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { EventService } from '../services/event.service';
import { AuthService } from '../services/auth';
import { CommunityEvent } from '../models/event.model';
import { FilterPipe } from '../shared/filter.pipe';
import { TableComponent } from '../shared/table/table';
import { ButtonComponent } from '../shared/button/button';

@Component({
  selector: 'app-organizer',
  standalone: true,
  imports: [CommonModule, TableComponent, ButtonComponent],
  templateUrl: './organizer.html'
})
export class OrganizerComponent implements OnInit {
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

  get filteredEvents(): CommunityEvent[] {
    return new FilterPipe().transform(this.events, this.searchText);
  }

  logout(): void {
    this.authService.logout();
    void this.router.navigate(['/login']);
  }
}
