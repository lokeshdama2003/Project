import { Injectable } from '@angular/core';

export type UserRole = 'Admin' | 'Organizer' | 'Attendee';

@Injectable({ providedIn: 'root' })
export class AuthService {
  private readonly roleKey = 'communityPortalRole';
  getRole(): UserRole | null {
    const savedRole = sessionStorage.getItem(this.roleKey);

    if (
      savedRole === 'Admin' ||
      savedRole === 'Organizer' ||
      savedRole === 'Attendee'
    ) {
      return savedRole;
    }

    return null;
  }
  login(role: UserRole): void {
    sessionStorage.setItem(this.roleKey, role);
  }
  logout(): void {
    sessionStorage.removeItem(this.roleKey);
  }

  isLoggedIn(): boolean {
    return this.getRole() !== null;
  }
}
