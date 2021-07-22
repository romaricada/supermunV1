import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IPerformance } from 'app/shared/model/performance.model';

@Component({
  selector: 'jhi-performance-detail',
  templateUrl: './performance-detail.component.html'
})
export class PerformanceDetailComponent implements OnInit {
  performance: IPerformance;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ performance }) => {
      this.performance = performance;
    });
  }

  previousState() {
    window.history.back();
  }
}
