import { NgModule } from '@angular/core';
import { SupermunSharedModule } from 'app/shared/shared.module';
import { RouterModule } from '@angular/router';
import { DictionaireComponent } from 'app/portail/dictionaire/dictionaire.component';
import { routeDictionaire } from 'app/portail/dictionaire/dictionaire.route';
const ENTITY_STATES = [...routeDictionaire];

@NgModule({
  declarations: [DictionaireComponent],
  imports: [SupermunSharedModule, RouterModule.forChild(ENTITY_STATES)]
})
export class DictionaireModule {}
