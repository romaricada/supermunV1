import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { SupermunSharedModule } from 'app/shared/shared.module';
import { ValeurModaliteComponent } from './valeur-modalite.component';
import { ValeurModaliteDetailComponent } from './valeur-modalite-detail.component';
import { ValeurModaliteUpdateComponent } from './valeur-modalite-update.component';
import { ValeurModaliteDeletePopupComponent, ValeurModaliteDeleteDialogComponent } from './valeur-modalite-delete-dialog.component';
import { valeurModaliteRoute, valeurModalitePopupRoute } from './valeur-modalite.route';
import { FileUploadModule } from 'primeng/primeng';

const ENTITY_STATES = [...valeurModaliteRoute, ...valeurModalitePopupRoute];

@NgModule({
  imports: [SupermunSharedModule, FileUploadModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [
    ValeurModaliteComponent,
    ValeurModaliteDetailComponent,
    ValeurModaliteUpdateComponent,
    ValeurModaliteDeleteDialogComponent,
    ValeurModaliteDeletePopupComponent
  ],
  entryComponents: [ValeurModaliteDeleteDialogComponent]
})
export class SupermunValeurModaliteModule {}
