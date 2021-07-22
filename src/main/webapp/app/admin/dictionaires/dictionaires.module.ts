import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { SupermunSharedModule } from 'app/shared/shared.module';
import { DictionairesComponent } from './dictionaires.component';
import { DictionairesDetailComponent } from './dictionaires-detail.component';
import { DictionairesUpdateComponent } from './dictionaires-update.component';
import { DictionairesDeletePopupComponent, DictionairesDeleteDialogComponent } from './dictionaires-delete-dialog.component';
import { dictionairesRoute, dictionairesPopupRoute } from './dictionaires.route';

const ENTITY_STATES = [...dictionairesRoute, ...dictionairesPopupRoute];

@NgModule({
  imports: [SupermunSharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [
    DictionairesComponent,
    DictionairesDetailComponent,
    DictionairesUpdateComponent,
    DictionairesDeleteDialogComponent,
    DictionairesDeletePopupComponent
  ],
  entryComponents: [DictionairesDeleteDialogComponent]
})
export class SupermunDictionairesModule {}
