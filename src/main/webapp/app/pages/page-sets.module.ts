import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { SupermunLeafletModule } from './leaflet/leaflet.module';
/* jhipster-needle-add-pageset-module-import - JHipster will add entity modules imports here */

@NgModule({
  imports: [
    SupermunLeafletModule
    /* jhipster-needle-add-pageset-module - JHipster will add entity modules here */
  ],
  declarations: [],
  entryComponents: [],
  providers: [],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class SupermunPageSetsModule {}
