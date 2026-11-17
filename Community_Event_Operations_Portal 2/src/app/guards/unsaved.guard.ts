import { CanDeactivateFn } from '@angular/router';
export interface CanLeavePage {
  canLeave: () => boolean;
}
export const unsavedGuard: CanDeactivateFn<CanLeavePage> = (component) => {
  return component.canLeave();
};
