import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { SupermunSharedModule } from 'app/shared/shared.module';
import { TypeIndicateurComponent } from './type-indicateur.component';
import { TypeIndicateurDetailComponent } from './type-indicateur-detail.component';
import { TypeIndicateurUpdateComponent } from './type-indicateur-update.component';
import { TypeIndicateurDeletePopupComponent, TypeIndicateurDeleteDialogComponent } from './type-indicateur-delete-dialog.component';
import { typeIndicateurRoute, typeIndicateurPopupRoute } from './type-indicateur.route';

const ENTITY_STATES = [...typeIndicateurRoute, ...typeIndicateurPopupRoute];

@NgModule({
  imports: [SupermunSharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [
    TypeIndicateurComponent,
    TypeIndicateurDetailComponent,
    TypeIndicateurUpdateComponent,
    TypeIndicateurDeleteDialogComponent,
    TypeIndicateurDeletePopupComponent
  ],
  entryComponents: [TypeIndicateurDeleteDialogComponent]
})
export class SupermunTypeIndicateurModule {}
