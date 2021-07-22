import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { SupermunSharedModule } from 'app/shared/shared.module';
import { PublicationComponent } from './publication.component';
import { PublicationDetailComponent } from './publication-detail.component';
import { PublicationUpdateComponent } from './publication-update.component';
import { PublicationDeletePopupComponent, PublicationDeleteDialogComponent } from './publication-delete-dialog.component';
import { publicationRoute, publicationPopupRoute } from './publication.route';

const ENTITY_STATES = [...publicationRoute, ...publicationPopupRoute];

@NgModule({
  imports: [SupermunSharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [
    PublicationComponent,
    PublicationDetailComponent,
    PublicationUpdateComponent,
    PublicationDeleteDialogComponent,
    PublicationDeletePopupComponent
  ],
  entryComponents: [PublicationDeleteDialogComponent]
})
export class SupermunPublicationModule {}
