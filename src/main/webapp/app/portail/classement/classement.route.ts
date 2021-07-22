import { Routes } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { ClassementComponent } from 'app/portail/classement/classement.component';

export const routeClassement: Routes = [
  {
    path: '',
    component: ClassementComponent,
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      authorities: [],
      pageTitle: 'Classement'
    }
  }
];
