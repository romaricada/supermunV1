import { NgModule } from '@angular/core';
import { SupermunSharedModule } from 'app/shared/shared.module';
import { RouterModule } from '@angular/router';
import { routeDonnee } from 'app/portail/donnee/donnee.route';
import { DonneeComponent } from 'app/portail/donnee/donnee.component';

const ENTITY_STATES = [...routeDonnee];
@NgModule({
  declarations: [DonneeComponent],
  imports: [SupermunSharedModule, RouterModule.forChild(ENTITY_STATES)]
})
export class DonneeModule {}
