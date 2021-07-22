import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IEtatCommune } from 'app/shared/model/etat-commune.model';
import { EtatCommuneService } from './etat-commune.service';

@Component({
  selector: 'jhi-etat-commune-delete-dialog',
  templateUrl: './etat-commune-delete-dialog.component.html'
})
export class EtatCommuneDeleteDialogComponent {
  etatCommune: IEtatCommune;

  constructor(
    protected etatCommuneService: EtatCommuneService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: number) {
    this.etatCommuneService.delete(id).subscribe(response => {
      this.eventManager.broadcast({
        name: 'etatCommuneListModification',
        content: 'Deleted an etatCommune'
      });
      this.activeModal.dismiss(true);
    });
  }
}

@Component({
  selector: 'jhi-etat-commune-delete-popup',
  template: ''
})
export class EtatCommuneDeletePopupComponent implements OnInit, OnDestroy {
  protected ngbModalRef: NgbModalRef;

  constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ etatCommune }) => {
      setTimeout(() => {
        this.ngbModalRef = this.modalService.open(EtatCommuneDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
        this.ngbModalRef.componentInstance.etatCommune = etatCommune;
        this.ngbModalRef.result.then(
          result => {
            this.router.navigate(['/etat-commune', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          },
          reason => {
            this.router.navigate(['/etat-commune', { outlets: { popup: null } }]);
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
