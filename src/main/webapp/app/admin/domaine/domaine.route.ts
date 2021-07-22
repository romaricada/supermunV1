import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { Domaine } from 'app/shared/model/domaine.model';
import { DomaineService } from './domaine.service';
import { DomaineComponent } from './domaine.component';
import { DomaineDetailComponent } from './domaine-detail.component';
import { DomaineUpdateComponent } from './domaine-update.component';
import { DomaineDeletePopupComponent } from './domaine-delete-dialog.component';
import { IDomaine } from 'app/shared/model/domaine.model';

@Injectable({ providedIn: 'root' })
export class DomaineResolve implements Resolve<IDomaine> {
  constructor(private service: DomaineService) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IDomaine> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        filter((response: HttpResponse<Domaine>) => response.ok),
        map((domaine: HttpResponse<Domaine>) => domaine.body)
      );
    }
    return of(new Domaine());
  }
}

export const domaineRoute: Routes = [
  {
    path: '',
    component: DomaineComponent,
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      authorities: ['ROLE_USER'],
      defaultSort: 'id,asc',
      pageTitle: 'Domaines'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: DomaineDetailComponent,
    resolve: {
      domaine: DomaineResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Domaines'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: DomaineUpdateComponent,
    resolve: {
      domaine: DomaineResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Domaines'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: DomaineUpdateComponent,
    resolve: {
      domaine: DomaineResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Domaines'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const domainePopupRoute: Routes = [
  {
    path: ':id/delete',
    component: DomaineDeletePopupComponent,
    resolve: {
      domaine: DomaineResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Domaines'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];
