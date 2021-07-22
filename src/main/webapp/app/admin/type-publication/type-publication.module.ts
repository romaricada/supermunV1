import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { SupermunSharedModule } from 'app/shared/shared.module';
import { TypePublicationComponent } from './type-publication.component';
import { TypePublicationDetailComponent } from './type-publication-detail.component';
import { TypePublicationUpdateComponent } from './type-publication-update.component';
import { TypePublicationDeletePopupComponent, TypePublicationDeleteDialogComponent } from './type-publication-delete-dialog.component';
import { typePublicationRoute, typePublicationPopupRoute } from './type-publication.route';

const ENTITY_STATES = [...typePublicationRoute, ...typePublicationPopupRoute];

@NgModule({
  imports: [SupermunSharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [
    TypePublicationComponent,
    TypePublicationDetailComponent,
    TypePublicationUpdateComponent,
    TypePublicationDeleteDialogComponent,
    TypePublicationDeletePopupComponent
  ],
  entryComponents: [TypePublicationDeleteDialogComponent]
})
export class SupermunTypePublicationModule {}
