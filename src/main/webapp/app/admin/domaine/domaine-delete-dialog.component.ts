import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IDomaine } from 'app/shared/model/domaine.model';
import { DomaineService } from './domaine.service';

@Component({
  selector: 'jhi-domaine-delete-dialog',
  templateUrl: './domaine-delete-dialog.component.html'
})
export class DomaineDeleteDialogComponent {
  domaine: IDomaine;

  constructor(protected domaineService: DomaineService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: number) {
    this.domaineService.delete(id).subscribe(response => {
      this.eventManager.broadcast({
        name: 'domaineListModification',
        content: 'Deleted an domaine'
      });
      this.activeModal.dismiss(true);
    });
  }
}

@Component({
  selector: 'jhi-domaine-delete-popup',
  template: ''
})
export class DomaineDeletePopupComponent implements OnInit, OnDestroy {
  protected ngbModalRef: NgbModalRef;

  constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ domaine }) => {
      setTimeout(() => {
        this.ngbModalRef = this.modalService.open(DomaineDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
        this.ngbModalRef.componentInstance.domaine = domaine;
        this.ngbModalRef.result.then(
          result => {
            this.router.navigate(['/domaine', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          },
          reason => {
            this.router.navigate(['/domaine', { outlets: { popup: null } }]);
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
