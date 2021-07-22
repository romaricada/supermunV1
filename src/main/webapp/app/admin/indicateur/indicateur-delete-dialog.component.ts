import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IIndicateur } from 'app/shared/model/indicateur.model';
import { IndicateurService } from './indicateur.service';

@Component({
  selector: 'jhi-indicateur-delete-dialog',
  templateUrl: './indicateur-delete-dialog.component.html'
})
export class IndicateurDeleteDialogComponent {
  indicateur: IIndicateur;

  constructor(
    protected indicateurService: IndicateurService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: number) {
    this.indicateurService.delete(id).subscribe(response => {
      this.eventManager.broadcast({
        name: 'indicateurListModification',
        content: 'Deleted an indicateur'
      });
      this.activeModal.dismiss(true);
    });
  }
}

@Component({
  selector: 'jhi-indicateur-delete-popup',
  template: ''
})
export class IndicateurDeletePopupComponent implements OnInit, OnDestroy {
  protected ngbModalRef: NgbModalRef;

  constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ indicateur }) => {
      setTimeout(() => {
        this.ngbModalRef = this.modalService.open(IndicateurDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
        this.ngbModalRef.componentInstance.indicateur = indicateur;
        this.ngbModalRef.result.then(
          result => {
            this.router.navigate(['/indicateur', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          },
          reason => {
            this.router.navigate(['/indicateur', { outlets: { popup: null } }]);
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
