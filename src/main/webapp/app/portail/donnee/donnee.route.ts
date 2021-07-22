import { ActivatedRouteSnapshot, Resolve, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { DonneeComponent } from 'app/portail/donnee/donnee.component';
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

export const routeDonnee: Routes = [
  {
    path: '',
    component: DonneeComponent,
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      authorities: [],
      pageTitle: 'Données'
    }
  },
  {
    path: ':idY/:idC',
    component: DonneeComponent,
    resolve: {
      ids: PosterResolve
    },
    data: {
      authorities: [],
      pageTitle: 'Données'
    }
  }
];
