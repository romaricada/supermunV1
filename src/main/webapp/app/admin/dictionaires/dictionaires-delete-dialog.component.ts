import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IDictionaires } from 'app/shared/model/dictionaires.model';
import { DictionairesService } from './dictionaires.service';

@Component({
  selector: 'jhi-dictionaires-delete-dialog',
  templateUrl: './dictionaires-delete-dialog.component.html'
})
export class DictionairesDeleteDialogComponent {
  dictionaires: IDictionaires;

  constructor(
    protected dictionairesService: DictionairesService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: number) {
    this.dictionairesService.delete(id).subscribe(response => {
      this.eventManager.broadcast({
        name: 'dictionairesListModification',
        content: 'Deleted an dictionaires'
      });
      this.activeModal.dismiss(true);
    });
  }
}

@Component({
  selector: 'jhi-dictionaires-delete-popup',
  template: ''
})
export class DictionairesDeletePopupComponent implements OnInit, OnDestroy {
  protected ngbModalRef: NgbModalRef;

  constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ dictionaires }) => {
      setTimeout(() => {
        this.ngbModalRef = this.modalService.open(DictionairesDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
        this.ngbModalRef.componentInstance.dictionaires = dictionaires;
        this.ngbModalRef.result.then(
          result => {
            this.router.navigate(['/dictionaires', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          },
          reason => {
            this.router.navigate(['/dictionaires', { outlets: { popup: null } }]);
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
