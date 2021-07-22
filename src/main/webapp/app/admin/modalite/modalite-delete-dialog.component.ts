import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IModalite } from 'app/shared/model/modalite.model';
import { ModaliteService } from './modalite.service';

@Component({
  selector: 'jhi-modalite-delete-dialog',
  templateUrl: './modalite-delete-dialog.component.html'
})
export class ModaliteDeleteDialogComponent {
  modalite: IModalite;

  constructor(protected modaliteService: ModaliteService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: number) {
    this.modaliteService.delete(id).subscribe(response => {
      this.eventManager.broadcast({
        name: 'modaliteListModification',
        content: 'Deleted an modalite'
      });
      this.activeModal.dismiss(true);
    });
  }
}

@Component({
  selector: 'jhi-modalite-delete-popup',
  template: ''
})
export class ModaliteDeletePopupComponent implements OnInit, OnDestroy {
  protected ngbModalRef: NgbModalRef;

  constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ modalite }) => {
      setTimeout(() => {
        this.ngbModalRef = this.modalService.open(ModaliteDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
        this.ngbModalRef.componentInstance.modalite = modalite;
        this.ngbModalRef.result.then(
          result => {
            this.router.navigate(['/modalite', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          },
          reason => {
            this.router.navigate(['/modalite', { outlets: { popup: null } }]);
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
