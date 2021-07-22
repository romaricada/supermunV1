import { Directive, EventEmitter, HostListener, Input, Output } from '@angular/core';

@Directive({
  selector: '[jhiTableGlobalSearch]'
})
export class TableGlobalSearchDirective {
  @Input()
  values: any[];

  @Output()
  search: EventEmitter<any[]> = new EventEmitter<any[]>();

  constructor() {}

  @HostListener('input', ['$event.target.value'])
  filterTable(value: string) {
    const tab: any[] = [];
    this.values.forEach(value1 => {
      const pList: string[] = Object.keys(value1);
      if (pList.length > 0) {
        if (
          pList.some(
            value2 =>
              value1[value2] &&
              value1[value2]
                .toString()
                .toLowerCase()
                .includes(value.toLowerCase())
          )
        ) {
          tab.push(value1);
        }
      }
    });
    this.search.emit(tab);
  }
}
