import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { EventService } from '../services/event.service';
import { AuthService } from '../services/auth';
import { CommunityEvent } from '../models/event.model';
import { FilterPipe } from '../shared/filter.pipe';
import { TableComponent } from '../shared/table/table';
import { ButtonComponent } from '../shared/button/button';
import { CanLeavePage } from '../guards/unsaved.guard';


@Component({
  selector: 'app-admin',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    TableComponent,
    ButtonComponent
  ],
  templateUrl: './admin.html'
})
export class AdminComponent implements OnInit, CanLeavePage {
  events: CommunityEvent[] = [];
  searchText = '';
  selectedEventId: number | null = null;

  statusMessage = '';
  errorMessage = '';

  sortColumn = 'date';
  sortDirection: 'asc' | 'desc' = 'asc';

  hasUnsavedChanges = false;
  eventForm;

  constructor(
    private formBuilder: FormBuilder,
    private eventService: EventService,
    private authService: AuthService,
    private router: Router
  ) {
    this.eventForm = this.formBuilder.group({
      title: ['', [Validators.required, Validators.minLength(3)]],
      category: ['', Validators.required],
      date: ['', Validators.required],
      venue: ['', Validators.required],
      capacity: [1, [Validators.required, Validators.min(1)]],
      status: ['Planning', Validators.required]
    });

    this.eventForm.valueChanges.subscribe(() => {
      this.hasUnsavedChanges = this.eventForm.dirty;
    });
  }

  ngOnInit(): void {
    this.loadEvents();
  }
  get filteredEvents(): CommunityEvent[] {
    const filterPipe = new FilterPipe();
    const matchingEvents = filterPipe.transform(this.events, this.searchText);

    return [...matchingEvents].sort((firstEvent, secondEvent) => {
      const firstValue = String(
        firstEvent[this.sortColumn as keyof CommunityEvent] ?? ''
      );
      const secondValue = String(
        secondEvent[this.sortColumn as keyof CommunityEvent] ?? ''
      );

      const comparison = firstValue.localeCompare(
        secondValue,
        undefined,
        { numeric: true }
      );

      return this.sortDirection === 'asc' ? comparison : -comparison;
    });
  }

  get totalEvents(): number {
    return this.events.length;
  }

  get openEvents(): number {
    return this.events.filter((event) => event.status === 'Open').length;
  }

  loadEvents(): void {
    this.eventService.getEvents().subscribe({
      next: (data) => {
        this.events = data;
      },
      error: (error) => {
        console.error('Unable to load events:', error);
        this.errorMessage =
          'Unable to load events. Make sure JSON Server is running on port 3000.';
      }
    });
  }

  sortEvents(column: string): void {
    if (this.sortColumn === column) {
      this.sortDirection = this.sortDirection === 'asc' ? 'desc' : 'asc';
      return;
    }

    this.sortColumn = column;
    this.sortDirection = 'asc';
  }
  saveEvent(): void {
    this.statusMessage = '';
    this.errorMessage = '';

    if (this.eventForm.invalid) {
      this.eventForm.markAllAsTouched();
      return;
    }

    const formValue = this.eventForm.getRawValue();

    const event: CommunityEvent = {
      title: (formValue.title ?? '').trim(),
      category: (formValue.category ?? '').trim(),
      date: formValue.date ?? '',
      venue: (formValue.venue ?? '').trim(),
      capacity: Number(formValue.capacity),
      status: formValue.status as CommunityEvent['status']
    };
    const duplicateEvent = this.events.some((existingEvent) => {
      const sameTitle =
        existingEvent.title.trim().toLowerCase() ===
        event.title.toLowerCase();
      const sameDate = existingEvent.date === event.date;
      const isDifferentEvent = existingEvent.id !== this.selectedEventId;

      return sameTitle && sameDate && isDifferentEvent;
    });

    if (duplicateEvent) {
      this.errorMessage =
        'An event with this title is already scheduled on this date.';
      return;
    }

    const isEditing = this.selectedEventId !== null;
    const request = isEditing
      ? this.eventService.updateEvent(this.selectedEventId as number, event)
      : this.eventService.addEvent(event);

    request.subscribe({
      next: () => {
        this.statusMessage = isEditing
          ? 'Event updated successfully.'
          : 'Event created successfully.';

        this.resetForm();
        this.loadEvents();
      },
      error: (error) => {
        console.error('Unable to save event:', error);
        this.errorMessage =
          'Unable to save event. Check the API and try again.';
      }
    });
  }
  editEvent(row: unknown): void {
    const event = row as CommunityEvent;

    if (event.id === undefined) {
      return;
    }

    this.selectedEventId = event.id;
    this.statusMessage = '';
    this.errorMessage = '';

    this.eventForm.patchValue({
      title: event.title,
      category: event.category,
      date: event.date,
      venue: event.venue,
      capacity: event.capacity,
      status: event.status
    });

    this.eventForm.markAsPristine();
    this.hasUnsavedChanges = false;
  }
  deleteEvent(row: unknown): void {
    const event = row as CommunityEvent;

    if (event.id === undefined) {
      return;
    }

    const confirmed = window.confirm(`Delete "${event.title}"?`);

    if (!confirmed) {
      return;
    }

    this.eventService.deleteEvent(event.id).subscribe({
      next: () => {
        this.statusMessage = 'Event deleted successfully.';

        if (this.selectedEventId === event.id) {
          this.resetForm();
        }

        this.loadEvents();
      },
      error: (error) => {
        console.error('Unable to delete event:', error);
        this.errorMessage = 'Unable to delete event.';
      }
    });
  }
  resetForm(): void {
    this.selectedEventId = null;

    this.eventForm.reset({
      title: '',
      category: '',
      date: '',
      venue: '',
      capacity: 1,
      status: 'Planning'
    });

    this.eventForm.markAsPristine();
    this.hasUnsavedChanges = false;
  }
  isInvalid(controlName: string): boolean {
    const control = this.eventForm.get(controlName);

    return !!control && control.invalid && (control.touched || control.dirty);
  }
  canLeave(): boolean {
    if (!this.hasUnsavedChanges) {
      return true;
    }

    return window.confirm(
      'You have unsaved event changes. Leave this page?'
    );
  }

  logout(): void {
    this.authService.logout();
    void this.router.navigate(['/login']);
  }
}
