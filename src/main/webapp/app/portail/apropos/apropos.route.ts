import { Routes } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { AproposComponent } from 'app/portail/apropos/apropos.component';

export const routePropos: Routes = [
  {
    path: '',
    component: AproposComponent,
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      authorities: [],
      pageTitle: 'Apropos'
    }
  }
];
