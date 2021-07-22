import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { SupermunSharedModule } from 'app/shared/shared.module';
import { ProvinceComponent } from './province.component';
import { ProvinceDetailComponent } from './province-detail.component';
import { ProvinceUpdateComponent } from './province-update.component';
import { ProvinceDeletePopupComponent, ProvinceDeleteDialogComponent } from './province-delete-dialog.component';
import { provinceRoute, provincePopupRoute } from './province.route';

const ENTITY_STATES = [...provinceRoute, ...provincePopupRoute];

@NgModule({
  imports: [SupermunSharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [
    ProvinceComponent,
    ProvinceDetailComponent,
    ProvinceUpdateComponent,
    ProvinceDeleteDialogComponent,
    ProvinceDeletePopupComponent
  ],
  entryComponents: [ProvinceDeleteDialogComponent]
})
export class SupermunProvinceModule {}
