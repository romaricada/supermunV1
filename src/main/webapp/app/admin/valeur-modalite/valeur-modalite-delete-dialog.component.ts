import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IValeurModalite } from 'app/shared/model/valeur-modalite.model';
import { ValeurModaliteService } from './valeur-modalite.service';

@Component({
  selector: 'jhi-valeur-modalite-delete-dialog',
  templateUrl: './valeur-modalite-delete-dialog.component.html'
})
export class ValeurModaliteDeleteDialogComponent {
  valeurModalite: IValeurModalite;

  constructor(
    protected valeurModaliteService: ValeurModaliteService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: number) {
    this.valeurModaliteService.delete(id).subscribe(response => {
      this.eventManager.broadcast({
        name: 'valeurModaliteListModification',
        content: 'Deleted an valeurModalite'
      });
      this.activeModal.dismiss(true);
    });
  }
}

@Component({
  selector: 'jhi-valeur-modalite-delete-popup',
  template: ''
})
export class ValeurModaliteDeletePopupComponent implements OnInit, OnDestroy {
  protected ngbModalRef: NgbModalRef;

  constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ valeurModalite }) => {
      setTimeout(() => {
        this.ngbModalRef = this.modalService.open(ValeurModaliteDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
        this.ngbModalRef.componentInstance.valeurModalite = valeurModalite;
        this.ngbModalRef.result.then(
          result => {
            this.router.navigate(['/valeur-modalite', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          },
          reason => {
            this.router.navigate(['/valeur-modalite', { outlets: { popup: null } }]);
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
