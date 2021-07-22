import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { SupermunSharedModule } from 'app/shared/shared.module';
import { ExerciceComponent } from './exercice.component';
import { ExerciceDetailComponent } from './exercice-detail.component';
import { ExerciceUpdateComponent } from './exercice-update.component';
import { ExerciceDeletePopupComponent, ExerciceDeleteDialogComponent } from './exercice-delete-dialog.component';
import { exerciceRoute, exercicePopupRoute } from './exercice.route';

const ENTITY_STATES = [...exerciceRoute, ...exercicePopupRoute];

@NgModule({
  imports: [SupermunSharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [
    ExerciceComponent,
    ExerciceDetailComponent,
    ExerciceUpdateComponent,
    ExerciceDeleteDialogComponent,
    ExerciceDeletePopupComponent
  ],
  entryComponents: [ExerciceDeleteDialogComponent]
})
export class SupermunExerciceModule {}
