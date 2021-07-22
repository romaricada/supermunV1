import { NgModule } from '@angular/core';
import { SupermunSharedModule } from 'app/shared/shared.module';
import { RouterModule } from '@angular/router';
import { AproposComponent } from 'app/portail/apropos/apropos.component';
import { routePropos } from 'app/portail/apropos/apropos.route';

const ENTITY_STATES = [...routePropos];
@NgModule({
  declarations: [AproposComponent],
  imports: [SupermunSharedModule, RouterModule.forChild(ENTITY_STATES)]
})
export class AproposModule {}
