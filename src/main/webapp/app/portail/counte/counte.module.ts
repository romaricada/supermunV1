import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { SupermunSharedModule } from 'app/shared/shared.module';
import { CounteComponent } from './counte.component';
import { counteRoute } from './counte.route';

const ENTITY_STATES = [...counteRoute];

@NgModule({
  imports: [SupermunSharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [CounteComponent],
  entryComponents: []
})
export class SupermunCounteModule {}
