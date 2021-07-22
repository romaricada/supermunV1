import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { SupermunSharedModule } from 'app/shared/shared.module';
import { DomaineComponent } from './domaine.component';
import { DomaineDetailComponent } from './domaine-detail.component';
import { DomaineUpdateComponent } from './domaine-update.component';
import { DomaineDeletePopupComponent, DomaineDeleteDialogComponent } from './domaine-delete-dialog.component';
import { domaineRoute, domainePopupRoute } from './domaine.route';

const ENTITY_STATES = [...domaineRoute, ...domainePopupRoute];

@NgModule({
  imports: [SupermunSharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [
    DomaineComponent,
    DomaineDetailComponent,
    DomaineUpdateComponent,
    DomaineDeleteDialogComponent,
    DomaineDeletePopupComponent
  ],
  entryComponents: [DomaineDeleteDialogComponent]
})
export class SupermunDomaineModule {}
