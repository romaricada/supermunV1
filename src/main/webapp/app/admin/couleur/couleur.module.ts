import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { SupermunSharedModule } from 'app/shared/shared.module';
import { CouleurComponent } from './couleur.component';
import { CouleurDetailComponent } from './couleur-detail.component';
import { CouleurUpdateComponent } from './couleur-update.component';
import { CouleurDeletePopupComponent, CouleurDeleteDialogComponent } from './couleur-delete-dialog.component';
import { couleurRoute, couleurPopupRoute } from './couleur.route';

const ENTITY_STATES = [...couleurRoute, ...couleurPopupRoute];

@NgModule({
  imports: [SupermunSharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [
    CouleurComponent,
    CouleurDetailComponent,
    CouleurUpdateComponent,
    CouleurDeleteDialogComponent,
    CouleurDeletePopupComponent
  ],
  entryComponents: [CouleurDeleteDialogComponent]
})
export class SupermunCouleurModule {}
