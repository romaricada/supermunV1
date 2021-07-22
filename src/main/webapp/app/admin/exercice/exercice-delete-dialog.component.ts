import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IExercice } from 'app/shared/model/exercice.model';
import { ExerciceService } from './exercice.service';

@Component({
  selector: 'jhi-exercice-delete-dialog',
  templateUrl: './exercice-delete-dialog.component.html'
})
export class ExerciceDeleteDialogComponent {
  exercice: IExercice;

  constructor(protected exerciceService: ExerciceService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: number) {
    this.exerciceService.delete(id).subscribe(response => {
      this.eventManager.broadcast({
        name: 'exerciceListModification',
        content: 'Deleted an exercice'
      });
      this.activeModal.dismiss(true);
    });
  }
}

@Component({
  selector: 'jhi-exercice-delete-popup',
  template: ''
})
export class ExerciceDeletePopupComponent implements OnInit, OnDestroy {
  protected ngbModalRef: NgbModalRef;

  constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ exercice }) => {
      setTimeout(() => {
        this.ngbModalRef = this.modalService.open(ExerciceDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
        this.ngbModalRef.componentInstance.exercice = exercice;
        this.ngbModalRef.result.then(
          result => {
            this.router.navigate(['/exercice', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          },
          reason => {
            this.router.navigate(['/exercice', { outlets: { popup: null } }]);
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
