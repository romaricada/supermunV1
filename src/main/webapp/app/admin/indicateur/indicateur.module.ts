import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { SupermunSharedModule } from 'app/shared/shared.module';
import { IndicateurComponent } from './indicateur.component';
import { IndicateurDetailComponent } from './indicateur-detail.component';
import { IndicateurUpdateComponent } from './indicateur-update.component';
import { IndicateurDeletePopupComponent, IndicateurDeleteDialogComponent } from './indicateur-delete-dialog.component';
import { indicateurRoute, indicateurPopupRoute } from './indicateur.route';

const ENTITY_STATES = [...indicateurRoute, ...indicateurPopupRoute];

@NgModule({
  imports: [SupermunSharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [
    IndicateurComponent,
    IndicateurDetailComponent,
    IndicateurUpdateComponent,
    IndicateurDeleteDialogComponent,
    IndicateurDeletePopupComponent
  ],
  entryComponents: [IndicateurDeleteDialogComponent]
})
export class SupermunIndicateurModule {}
