import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { EtatCommune } from 'app/shared/model/etat-commune.model';
import { EtatCommuneService } from './etat-commune.service';
import { EtatCommuneComponent } from './etat-commune.component';
import { EtatCommuneDetailComponent } from './etat-commune-detail.component';
import { EtatCommuneUpdateComponent } from './etat-commune-update.component';
import { EtatCommuneDeletePopupComponent } from './etat-commune-delete-dialog.component';
import { IEtatCommune } from 'app/shared/model/etat-commune.model';

@Injectable({ providedIn: 'root' })
export class EtatCommuneResolve implements Resolve<IEtatCommune> {
  constructor(private service: EtatCommuneService) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IEtatCommune> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        filter((response: HttpResponse<EtatCommune>) => response.ok),
        map((etatCommune: HttpResponse<EtatCommune>) => etatCommune.body)
      );
    }
    return of(new EtatCommune());
  }
}

export const etatCommuneRoute: Routes = [
  {
    path: '',
    component: EtatCommuneComponent,
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      authorities: ['ROLE_USER'],
      defaultSort: 'id,asc',
      pageTitle: 'EtatCommunes'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: EtatCommuneDetailComponent,
    resolve: {
      etatCommune: EtatCommuneResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'EtatCommunes'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: EtatCommuneUpdateComponent,
    resolve: {
      etatCommune: EtatCommuneResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'EtatCommunes'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: EtatCommuneUpdateComponent,
    resolve: {
      etatCommune: EtatCommuneResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'EtatCommunes'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const etatCommunePopupRoute: Routes = [
  {
    path: ':id/delete',
    component: EtatCommuneDeletePopupComponent,
    resolve: {
      etatCommune: EtatCommuneResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'EtatCommunes'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];
