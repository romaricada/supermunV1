import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { SupermunSharedModule } from 'app/shared/shared.module';
import { ProfilComponent } from './profil.component';
import { profilRoute } from './profil.route';

const ENTITY_STATES = [...profilRoute];

@NgModule({
  imports: [SupermunSharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [ProfilComponent]
})
export class SupermunProfilModule {}
