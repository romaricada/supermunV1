import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IPublication } from 'app/shared/model/publication.model';
import { PublicationService } from './publication.service';

@Component({
  selector: 'jhi-publication-delete-dialog',
  templateUrl: './publication-delete-dialog.component.html'
})
export class PublicationDeleteDialogComponent {
  publication: IPublication;

  constructor(
    protected publicationService: PublicationService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: number) {
    this.publicationService.delete(id).subscribe(response => {
      this.eventManager.broadcast({
        name: 'publicationListModification',
        content: 'Deleted an publication'
      });
      this.activeModal.dismiss(true);
    });
  }
}

@Component({
  selector: 'jhi-publication-delete-popup',
  template: ''
})
export class PublicationDeletePopupComponent implements OnInit, OnDestroy {
  protected ngbModalRef: NgbModalRef;

  constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ publication }) => {
      setTimeout(() => {
        this.ngbModalRef = this.modalService.open(PublicationDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
        this.ngbModalRef.componentInstance.publication = publication;
        this.ngbModalRef.result.then(
          result => {
            this.router.navigate(['/publication', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          },
          reason => {
            this.router.navigate(['/publication', { outlets: { popup: null } }]);
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
