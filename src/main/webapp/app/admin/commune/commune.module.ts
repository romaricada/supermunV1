import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { SupermunSharedModule } from 'app/shared/shared.module';
import { CommuneComponent } from './commune.component';
import { CommuneDetailComponent } from './commune-detail.component';
import { CommuneUpdateComponent } from './commune-update.component';
import { CommuneDeletePopupComponent, CommuneDeleteDialogComponent } from './commune-delete-dialog.component';
import { communeRoute, communePopupRoute } from './commune.route';

const ENTITY_STATES = [...communeRoute, ...communePopupRoute];

@NgModule({
  imports: [SupermunSharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [
    CommuneComponent,
    CommuneDetailComponent,
    CommuneUpdateComponent,
    CommuneDeleteDialogComponent,
    CommuneDeletePopupComponent
  ],
  entryComponents: [CommuneDeleteDialogComponent]
})
export class SupermunCommuneModule {}
