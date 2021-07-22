import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { SupermunSharedModule } from 'app/shared/shared.module';
import { NgxPrintModule } from 'ngx-print';
import { PosterComponent } from './poster.component';
import { routePoster } from './poster.route';

const ENTITY_STATES = [...routePoster];

@NgModule({
  imports: [CommonModule, SupermunSharedModule, NgxPrintModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [PosterComponent]
})
export class PosterModule {}
