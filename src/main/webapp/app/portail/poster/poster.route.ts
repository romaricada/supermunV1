import { Routes, Resolve, ActivatedRouteSnapshot, RouterStateSnapshot } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { PosterComponent } from 'app/portail/poster/poster.component';
import { Injectable } from '@angular/core';
@Injectable({ providedIn: 'root' })
export class PosterResolve implements Resolve<any> {
  constructor() {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): any {
    const idY = route.queryParams.idY;
    const idC = route.queryParams.idC;
    return { idY, idC };
  }
}
export const routePoster: Routes = [
  {
    path: '',
    component: PosterComponent,
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      authorities: [],
      pageTitle: 'Posters'
    }
  },
  {
    path: 'params/:id1/:id2',
    component: PosterComponent
  },
  {
    path: ':idY/:idC',
    component: PosterComponent,
    resolve: {
      ids: PosterResolve
    },
    data: {
      authorities: [],
      pageTitle: 'Posters'
    }
    // ,canActivate: [UserRouteAccessService]
  }
];
