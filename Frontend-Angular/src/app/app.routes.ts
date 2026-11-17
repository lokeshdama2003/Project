import { Routes } from '@angular/router';
import { LoginComponent } from './login/login';
import { AdminComponent } from './admin/admin';
import { OrganizerComponent } from './organizer/organizer';
import { AttendeeComponent } from './attendee/attendee';
import { roleGuard } from './guards/role.guard';
import { unsavedGuard } from './guards/unsaved.guard';

export const routes: Routes = [
  {
    path: '',
    redirectTo: 'login',
    pathMatch: 'full'
  },
  {
    path: 'login',
    component: LoginComponent
  },
  {
    path: 'admin',
    component: AdminComponent,
    canActivate: [roleGuard],
    canDeactivate: [unsavedGuard],
    data: { roles: ['Admin'] }
  },
  {
    path: 'organizer',
    component: OrganizerComponent,
    canActivate: [roleGuard],
    data: { roles: ['Organizer'] }
  },
  {
    path: 'attendee',
    component: AttendeeComponent,
    canActivate: [roleGuard],
    data: { roles: ['Attendee'] }
  },
  {
    path: '**',
    redirectTo: 'login'
  }
];
