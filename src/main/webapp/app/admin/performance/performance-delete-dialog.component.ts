import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IPerformance } from 'app/shared/model/performance.model';
import { PerformanceService } from './performance.service';

@Component({
  selector: 'jhi-performance-delete-dialog',
  templateUrl: './performance-delete-dialog.component.html'
})
export class PerformanceDeleteDialogComponent {
  performance: IPerformance;

  constructor(
    protected performanceService: PerformanceService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: number) {
    this.performanceService.delete(id).subscribe(response => {
      this.eventManager.broadcast({
        name: 'performanceListModification',
        content: 'Deleted an performance'
      });
      this.activeModal.dismiss(true);
    });
  }
}

@Component({
  selector: 'jhi-performance-delete-popup',
  template: ''
})
export class PerformanceDeletePopupComponent implements OnInit, OnDestroy {
  protected ngbModalRef: NgbModalRef;

  constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ performance }) => {
      setTimeout(() => {
        this.ngbModalRef = this.modalService.open(PerformanceDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
        this.ngbModalRef.componentInstance.performance = performance;
        this.ngbModalRef.result.then(
          result => {
            this.router.navigate(['/performance', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          },
          reason => {
            this.router.navigate(['/performance', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          }
        );
      }, 0);
    });
  }

  ngOnDestroy() {
    this.ngbModalRef = null;
  }
}
