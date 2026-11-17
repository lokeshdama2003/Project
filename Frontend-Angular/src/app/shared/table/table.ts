
import {
  Component,
  EventEmitter,
  Input,
  Output,
  OnChanges,
  SimpleChanges
} from '@angular/core';

import { CommonModule } from '@angular/common';
import { ButtonComponent } from '../button/button';

@Component({
  selector: 'app-table',
  standalone: true,
  imports: [CommonModule, ButtonComponent],
  templateUrl: './table.html'
})
export class TableComponent implements OnChanges {

  @Input() title = 'Records';

  @Input() headers: string[] = [];

  @Input() columns: string[] = [];

  @Input() rows: any[] = [];

  @Input() sortColumn = '';

  @Input() sortDirection: 'asc' | 'desc' = 'asc';

  @Input() showActions = false;
  @Input() pageSize = 5;

  currentPage = 1;

  @Output() sortChange = new EventEmitter<string>();

  @Output() edit = new EventEmitter<any>();

  @Output() remove = new EventEmitter<any>();

  ngOnChanges(changes: SimpleChanges): void {

    if (changes['rows']) {
      this.currentPage = 1;
    }

    if (changes['pageSize']) {
      this.currentPage = 1;
    }

  }
  get totalPages(): number {

    return Math.max(
      1,
      Math.ceil(this.rows.length / this.pageSize)
    );

  }
  get pageNumbers(): number[] {

    return Array.from(
      { length: this.totalPages },
      (_, index) => index + 1
    );

  }
  get paginatedRows(): any[] {

    const startIndex =
      (this.currentPage - 1) * this.pageSize;

    const endIndex =
      startIndex + this.pageSize;

    return this.rows.slice(startIndex, endIndex);

  }
  get firstItemNumber(): number {

    if (this.rows.length === 0) {
      return 0;
    }

    return (this.currentPage - 1) * this.pageSize + 1;

  }
  get lastItemNumber(): number {

    return Math.min(
      this.currentPage * this.pageSize,
      this.rows.length
    );

  }
  goToPage(page: number): void {

    if (page < 1 || page > this.totalPages) {
      return;
    }

    this.currentPage = page;

  }
  previousPage(): void {
    this.goToPage(this.currentPage - 1);

  }
  nextPage(): void {
    this.goToPage(this.currentPage + 1);

  }

}
