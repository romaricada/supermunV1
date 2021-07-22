import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { SupermunSharedModule } from 'app/shared/shared.module';
import { EtatCommuneComponent } from './etat-commune.component';
import { EtatCommuneDetailComponent } from './etat-commune-detail.component';
import { EtatCommuneUpdateComponent } from './etat-commune-update.component';
import { EtatCommuneDeletePopupComponent, EtatCommuneDeleteDialogComponent } from './etat-commune-delete-dialog.component';
import { etatCommuneRoute, etatCommunePopupRoute } from './etat-commune.route';

const ENTITY_STATES = [...etatCommuneRoute, ...etatCommunePopupRoute];

@NgModule({
  imports: [SupermunSharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [
    EtatCommuneComponent,
    EtatCommuneDetailComponent,
    EtatCommuneUpdateComponent,
    EtatCommuneDeleteDialogComponent,
    EtatCommuneDeletePopupComponent
  ],
  entryComponents: [EtatCommuneDeleteDialogComponent]
})
export class SupermunEtatCommuneModule {}
