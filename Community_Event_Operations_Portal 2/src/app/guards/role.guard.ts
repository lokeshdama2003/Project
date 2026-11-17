import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { AuthService, UserRole } from '../services/auth';
export const roleGuard: CanActivateFn = (route) => {
  const authService = inject(AuthService);
  const router = inject(Router);
  const currentRole = authService.getRole();
  const allowedRoles = route.data['roles'] as UserRole[];

  if (!currentRole) {
    return router.createUrlTree(['/login']);
  }

  if (allowedRoles.includes(currentRole)) {
    return true;
  }
  if (currentRole === 'Admin') {
    return router.createUrlTree(['/admin']);
  }

  if (currentRole === 'Organizer') {
    return router.createUrlTree(['/organizer']);
  }

  return router.createUrlTree(['/attendee']);
};
