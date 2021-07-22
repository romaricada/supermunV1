import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ITypePublication } from 'app/shared/model/type-publication.model';
import { TypePublicationService } from './type-publication.service';

@Component({
  selector: 'jhi-type-publication-delete-dialog',
  templateUrl: './type-publication-delete-dialog.component.html'
})
export class TypePublicationDeleteDialogComponent {
  typePublication: ITypePublication;

  constructor(
    protected typePublicationService: TypePublicationService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: number) {
    this.typePublicationService.delete(id).subscribe(response => {
      this.eventManager.broadcast({
        name: 'typePublicationListModification',
        content: 'Deleted an typePublication'
      });
      this.activeModal.dismiss(true);
    });
  }
}

@Component({
  selector: 'jhi-type-publication-delete-popup',
  template: ''
})
export class TypePublicationDeletePopupComponent implements OnInit, OnDestroy {
  protected ngbModalRef: NgbModalRef;

  constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ typePublication }) => {
      setTimeout(() => {
        this.ngbModalRef = this.modalService.open(TypePublicationDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
        this.ngbModalRef.componentInstance.typePublication = typePublication;
        this.ngbModalRef.result.then(
          result => {
            this.router.navigate(['/type-publication', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          },
          reason => {
            this.router.navigate(['/type-publication', { outlets: { popup: null } }]);
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
