import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { Indicateur } from 'app/shared/model/indicateur.model';
import { IndicateurService } from './indicateur.service';
import { IndicateurComponent } from './indicateur.component';
import { IndicateurDetailComponent } from './indicateur-detail.component';
import { IndicateurUpdateComponent } from './indicateur-update.component';
import { IndicateurDeletePopupComponent } from './indicateur-delete-dialog.component';
import { IIndicateur } from 'app/shared/model/indicateur.model';

@Injectable({ providedIn: 'root' })
export class IndicateurResolve implements Resolve<IIndicateur> {
  constructor(private service: IndicateurService) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IIndicateur> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        filter((response: HttpResponse<Indicateur>) => response.ok),
        map((indicateur: HttpResponse<Indicateur>) => indicateur.body)
      );
    }
    return of(new Indicateur());
  }
}

export const indicateurRoute: Routes = [
  {
    path: '',
    component: IndicateurComponent,
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      authorities: ['ROLE_USER'],
      defaultSort: 'id,asc',
      pageTitle: 'Indicateurs'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: IndicateurDetailComponent,
    resolve: {
      indicateur: IndicateurResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Indicateurs'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: IndicateurUpdateComponent,
    resolve: {
      indicateur: IndicateurResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Indicateurs'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: IndicateurUpdateComponent,
    resolve: {
      indicateur: IndicateurResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Indicateurs'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const indicateurPopupRoute: Routes = [
  {
    path: ':id/delete',
    component: IndicateurDeletePopupComponent,
    resolve: {
      indicateur: IndicateurResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Indicateurs'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];
