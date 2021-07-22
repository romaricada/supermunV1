import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { AdminHomeComponent } from 'app/home/admin-home.component';

import { SupermunSharedModule } from 'app/shared/shared.module';
import { HomeComponent } from './home.component';
import { HOME_ROUTE } from './home.route';

@NgModule({
  imports: [SupermunSharedModule, RouterModule.forChild(HOME_ROUTE)],
  declarations: [HomeComponent, AdminHomeComponent]
})
export class SupermunHomeModule {}
