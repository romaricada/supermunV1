import { NgModule } from '@angular/core';
import { SupermunSharedModule } from 'app/shared/shared.module';
import { RouterModule } from '@angular/router';
import { ClassementComponent } from 'app/portail/classement/classement.component';
import { routeClassement } from 'app/portail/classement/classement.route';

const ENTITY_STATES = [...routeClassement];

@NgModule({
  imports: [SupermunSharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [ClassementComponent]
})
export class ClassementModule {}
