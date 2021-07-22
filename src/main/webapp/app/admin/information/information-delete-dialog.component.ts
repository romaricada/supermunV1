import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IInformation } from 'app/shared/model/information.model';
import { InformationService } from './information.service';

@Component({
  selector: 'jhi-information-delete-dialog',
  templateUrl: './information-delete-dialog.component.html'
})
export class InformationDeleteDialogComponent {
  information: IInformation;

  constructor(
    protected informationService: InformationService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: number) {
    this.informationService.delete(id).subscribe(response => {
      this.eventManager.broadcast({
        name: 'informationListModification',
        content: 'Deleted an information'
      });
      this.activeModal.dismiss(true);
    });
  }
}

@Component({
  selector: 'jhi-information-delete-popup',
  template: ''
})
export class InformationDeletePopupComponent implements OnInit, OnDestroy {
  protected ngbModalRef: NgbModalRef;

  constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ information }) => {
      setTimeout(() => {
        this.ngbModalRef = this.modalService.open(InformationDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
        this.ngbModalRef.componentInstance.information = information;
        this.ngbModalRef.result.then(
          result => {
            this.router.navigate(['/information', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          },
          reason => {
            this.router.navigate(['/information', { outlets: { popup: null } }]);
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
