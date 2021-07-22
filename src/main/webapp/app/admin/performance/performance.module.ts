import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { ImportExportComponent } from 'app/admin/performance/import-export.component';

import { SupermunSharedModule } from 'app/shared/shared.module';
import { PerformanceComponent } from './performance.component';
import { PerformanceDetailComponent } from './performance-detail.component';
import { PerformanceUpdateComponent } from './performance-update.component';
import { PerformanceDeletePopupComponent, PerformanceDeleteDialogComponent } from './performance-delete-dialog.component';
import { performanceRoute, performancePopupRoute } from './performance.route';

const ENTITY_STATES = [...performanceRoute, ...performancePopupRoute];

@NgModule({
  imports: [SupermunSharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [
    PerformanceComponent,
    PerformanceDetailComponent,
    PerformanceUpdateComponent,
    PerformanceDeleteDialogComponent,
    PerformanceDeletePopupComponent,
    ImportExportComponent
  ],
  entryComponents: [PerformanceDeleteDialogComponent]
})
export class SupermunPerformanceModule {}
