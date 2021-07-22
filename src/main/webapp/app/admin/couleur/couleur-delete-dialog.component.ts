import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ICouleur } from 'app/shared/model/couleur.model';
import { CouleurService } from './couleur.service';

@Component({
  selector: 'jhi-couleur-delete-dialog',
  templateUrl: './couleur-delete-dialog.component.html'
})
export class CouleurDeleteDialogComponent {
  couleur: ICouleur;

  constructor(protected couleurService: CouleurService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: number) {
    this.couleurService.delete(id).subscribe(response => {
      this.eventManager.broadcast({
        name: 'couleurListModification',
        content: 'Deleted an couleur'
      });
      this.activeModal.dismiss(true);
    });
  }
}

@Component({
  selector: 'jhi-couleur-delete-popup',
  template: ''
})
export class CouleurDeletePopupComponent implements OnInit, OnDestroy {
  protected ngbModalRef: NgbModalRef;

  constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ couleur }) => {
      setTimeout(() => {
        this.ngbModalRef = this.modalService.open(CouleurDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
        this.ngbModalRef.componentInstance.couleur = couleur;
        this.ngbModalRef.result.then(
          result => {
            this.router.navigate(['/couleur', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          },
          reason => {
            this.router.navigate(['/couleur', { outlets: { popup: null } }]);
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
