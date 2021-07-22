import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { Couleur } from 'app/shared/model/couleur.model';
import { CouleurService } from './couleur.service';
import { CouleurComponent } from './couleur.component';
import { CouleurDetailComponent } from './couleur-detail.component';
import { CouleurUpdateComponent } from './couleur-update.component';
import { CouleurDeletePopupComponent } from './couleur-delete-dialog.component';
import { ICouleur } from 'app/shared/model/couleur.model';

@Injectable({ providedIn: 'root' })
export class CouleurResolve implements Resolve<ICouleur> {
  constructor(private service: CouleurService) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<ICouleur> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        filter((response: HttpResponse<Couleur>) => response.ok),
        map((couleur: HttpResponse<Couleur>) => couleur.body)
      );
    }
    return of(new Couleur());
  }
}

export const couleurRoute: Routes = [
  {
    path: '',
    component: CouleurComponent,
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      authorities: ['ROLE_USER'],
      defaultSort: 'id,asc',
      pageTitle: 'Couleurs'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: CouleurDetailComponent,
    resolve: {
      couleur: CouleurResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Couleurs'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: CouleurUpdateComponent,
    resolve: {
      couleur: CouleurResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Couleurs'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: CouleurUpdateComponent,
    resolve: {
      couleur: CouleurResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Couleurs'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const couleurPopupRoute: Routes = [
  {
    path: ':id/delete',
    component: CouleurDeletePopupComponent,
    resolve: {
      couleur: CouleurResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Couleurs'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];
