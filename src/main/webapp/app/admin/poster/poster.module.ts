import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { SupermunSharedModule } from 'app/shared/shared.module';
import { PosterComponent } from './poster.component';
import { PosterDetailComponent } from './poster-detail.component';
import { PosterUpdateComponent } from './poster-update.component';
import { PosterDeletePopupComponent, PosterDeleteDialogComponent } from './poster-delete-dialog.component';
import { posterRoute, posterPopupRoute } from './poster.route';

const ENTITY_STATES = [...posterRoute, ...posterPopupRoute];

@NgModule({
  imports: [SupermunSharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [PosterComponent, PosterDetailComponent, PosterUpdateComponent, PosterDeleteDialogComponent, PosterDeletePopupComponent],
  entryComponents: [PosterDeleteDialogComponent]
})
export class SupermunPosterModule {}
