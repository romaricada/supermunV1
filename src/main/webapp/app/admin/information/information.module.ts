import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { SupermunSharedModule } from 'app/shared/shared.module';
import { InformationComponent } from './information.component';
import { InformationDetailComponent } from './information-detail.component';
import { InformationUpdateComponent } from './information-update.component';
import { InformationDeletePopupComponent, InformationDeleteDialogComponent } from './information-delete-dialog.component';
import { informationRoute, informationPopupRoute } from './information.route';

const ENTITY_STATES = [...informationRoute, ...informationPopupRoute];

@NgModule({
  imports: [SupermunSharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [
    InformationComponent,
    InformationDetailComponent,
    InformationUpdateComponent,
    InformationDeleteDialogComponent,
    InformationDeletePopupComponent
  ],
  entryComponents: [InformationDeleteDialogComponent]
})
export class SupermunInformationModule {}
