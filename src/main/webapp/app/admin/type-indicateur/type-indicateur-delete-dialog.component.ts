import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ITypeIndicateur } from 'app/shared/model/type-indicateur.model';
import { TypeIndicateurService } from './type-indicateur.service';

@Component({
  selector: 'jhi-type-indicateur-delete-dialog',
  templateUrl: './type-indicateur-delete-dialog.component.html'
})
export class TypeIndicateurDeleteDialogComponent {
  typeIndicateur: ITypeIndicateur;

  constructor(
    protected typeIndicateurService: TypeIndicateurService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: number) {
    this.typeIndicateurService.delete(id).subscribe(response => {
      this.eventManager.broadcast({
        name: 'typeIndicateurListModification',
        content: 'Deleted an typeIndicateur'
      });
      this.activeModal.dismiss(true);
    });
  }
}

@Component({
  selector: 'jhi-type-indicateur-delete-popup',
  template: ''
})
export class TypeIndicateurDeletePopupComponent implements OnInit, OnDestroy {
  protected ngbModalRef: NgbModalRef;

  constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ typeIndicateur }) => {
      setTimeout(() => {
        this.ngbModalRef = this.modalService.open(TypeIndicateurDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
        this.ngbModalRef.componentInstance.typeIndicateur = typeIndicateur;
        this.ngbModalRef.result.then(
          result => {
            this.router.navigate(['/type-indicateur', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          },
          reason => {
            this.router.navigate(['/type-indicateur', { outlets: { popup: null } }]);
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
