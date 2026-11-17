import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'filter',
  standalone: true
})
export class FilterPipe implements PipeTransform {
  transform<T extends object>(
    items: T[] | null | undefined,
    searchText: string
  ): T[] {
    if (!items) {
      return [];
    }

    const searchValue = (searchText ?? '').trim().toLowerCase();

    if (!searchValue) {
      return items;
    }
    return items.filter((item) =>
      Object.values(item).some((value) =>
        String(value ?? '').toLowerCase().includes(searchValue)
      )
    );
  }
}
