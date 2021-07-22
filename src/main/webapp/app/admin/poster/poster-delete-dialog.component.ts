import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IPoster } from 'app/shared/model/poster.model';
import { PosterService } from './poster.service';

@Component({
  selector: 'jhi-poster-delete-dialog',
  templateUrl: './poster-delete-dialog.component.html'
})
export class PosterDeleteDialogComponent {
  poster: IPoster;

  constructor(protected posterService: PosterService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: number) {
    this.posterService.delete(id).subscribe(response => {
      this.eventManager.broadcast({
        name: 'posterListModification',
        content: 'Deleted an poster'
      });
      this.activeModal.dismiss(true);
    });
  }
}

@Component({
  selector: 'jhi-poster-delete-popup',
  template: ''
})
export class PosterDeletePopupComponent implements OnInit, OnDestroy {
  protected ngbModalRef: NgbModalRef;

  constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ poster }) => {
      setTimeout(() => {
        this.ngbModalRef = this.modalService.open(PosterDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
        this.ngbModalRef.componentInstance.poster = poster;
        this.ngbModalRef.result.then(
          result => {
            this.router.navigate(['/poster', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          },
          reason => {
            this.router.navigate(['/poster', { outlets: { popup: null } }]);
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
