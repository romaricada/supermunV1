import { NgModule } from '@angular/core';
import { FlexLayoutModule } from '@angular/flex-layout';
import { BrowserModule } from '@angular/platform-browser';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { SupermunCoreModule } from 'app/core/core.module';
import { PortailModule } from 'app/portail/portail.module';
import { SupermunSharedModule } from 'app/shared/shared.module';
import 'hammerjs';
import { SupermunAppRoutingModule } from './app-routing.module';
import { SupermunHomeModule } from './home/home.module';
import { ErrorComponent } from './layouts/error/error.component';
import { FooterComponent } from './layouts/footer/footer.component';
// jhipster-needle-angular-add-module-import JHipster will add new module here
import { JhiMainComponent } from './layouts/main/main.component';
import { NavbarComponent } from './layouts/navbar/navbar.component';
import { PageRibbonComponent } from './layouts/profiles/page-ribbon.component';
import { SupermunPageSetsModule } from './pages/page-sets.module';

import './vendor';

@NgModule({
  imports: [
    BrowserModule,
    BrowserAnimationsModule,
    FlexLayoutModule,
    SupermunSharedModule,
    SupermunCoreModule,
    SupermunHomeModule,
    SupermunPageSetsModule,
    // jhipster-needle-angular-add-module JHipster will add new module here
    SupermunAppRoutingModule,
    PortailModule
  ],
  declarations: [JhiMainComponent, NavbarComponent, ErrorComponent, PageRibbonComponent, FooterComponent],
  bootstrap: [JhiMainComponent]
})
export class SupermunAppModule {}
