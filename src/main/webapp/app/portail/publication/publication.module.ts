import { NgModule } from '@angular/core';
import { routePublication } from 'app/portail/publication/publication.route';
import { SupermunSharedModule } from 'app/shared/shared.module';
import { RouterModule } from '@angular/router';
import { PublicationComponent } from 'app/portail/publication/publication.component';

const ENTITY_STATES = [...routePublication];

@NgModule({
  imports: [SupermunSharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [PublicationComponent]
})
export class PublicationModule {}
