import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { SupermunSharedModule } from 'app/shared/shared.module';
import { ModaliteComponent } from './modalite.component';
import { ModaliteDetailComponent } from './modalite-detail.component';
import { ModaliteUpdateComponent } from './modalite-update.component';
import { ModaliteDeletePopupComponent, ModaliteDeleteDialogComponent } from './modalite-delete-dialog.component';
import { modaliteRoute, modalitePopupRoute } from './modalite.route';

const ENTITY_STATES = [...modaliteRoute, ...modalitePopupRoute];

@NgModule({
  imports: [SupermunSharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [
    ModaliteComponent,
    ModaliteDetailComponent,
    ModaliteUpdateComponent,
    ModaliteDeleteDialogComponent,
    ModaliteDeletePopupComponent
  ],
  entryComponents: [ModaliteDeleteDialogComponent]
})
export class SupermunModaliteModule {}
