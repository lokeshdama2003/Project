import { Component, EventEmitter, Input, Output } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-button',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './button.html'
})
export class ButtonComponent {

  @Input() label: string = 'Click';

  @Input() btnClass: string = 'btn-primary';

  @Input() buttonType: 'button' | 'submit' = 'button';

  @Input() disabled: boolean = false;

  @Output() buttonClick = new EventEmitter<void>();

  onClick(): void {
    this.buttonClick.emit();
  }

} 