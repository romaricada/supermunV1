import { Component, OnInit } from '@angular/core';
import { JhiEventManager } from 'ng-jhipster';

import { JhiMetricsService } from './metrics.service';

@Component({
  selector: 'jhi-metrics',
  templateUrl: './metrics.component.html'
})
export class JhiMetricsMonitoringComponent implements OnInit {
  metrics: any = {};
  threadData: any = {};
  updatingMetrics = true;
  JCACHE_KEY: string;

  constructor(private metricsService: JhiMetricsService, private eventManager: JhiEventManager) {
    this.JCACHE_KEY = 'jcache.statistics';
  }

  ngOnInit() {
    this.eventManager.broadcast({
      name: 'endpointChanged',
      content: 'User switch to portail !'
    });
    this.refresh();
  }

  refresh() {
    this.updatingMetrics = true;
    this.metricsService.getMetrics().subscribe(metrics => {
      this.metrics = metrics;
      this.metricsService.threadDump().subscribe(data => {
        this.threadData = data.threads;
        this.updatingMetrics = false;
      });
    });
  }

  isObjectExisting(metrics: any, key: string) {
    return metrics && metrics[key];
  }

  isObjectExistingAndNotEmpty(metrics: any, key: string) {
    return this.isObjectExisting(metrics, key) && JSON.stringify(metrics[key]) !== '{}';
  }
}
