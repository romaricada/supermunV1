import { Routes } from '@angular/router';
import { PublicationComponent } from 'app/portail/publication/publication.component';
import { JhiResolvePagingParams } from 'ng-jhipster';

export const routePublication: Routes = [
  {
    path: '',
    component: PublicationComponent,
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      authorities: [],
      pageTitle: 'Publication'
    }
  }
];
