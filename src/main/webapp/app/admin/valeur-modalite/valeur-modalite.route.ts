import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { ValeurModalite } from 'app/shared/model/valeur-modalite.model';
import { ValeurModaliteService } from './valeur-modalite.service';
import { ValeurModaliteComponent } from './valeur-modalite.component';
import { ValeurModaliteDetailComponent } from './valeur-modalite-detail.component';
import { ValeurModaliteUpdateComponent } from './valeur-modalite-update.component';
import { ValeurModaliteDeletePopupComponent } from './valeur-modalite-delete-dialog.component';
import { IValeurModalite } from 'app/shared/model/valeur-modalite.model';

@Injectable({ providedIn: 'root' })
export class ValeurModaliteResolve implements Resolve<IValeurModalite> {
  constructor(private service: ValeurModaliteService) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IValeurModalite> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        filter((response: HttpResponse<ValeurModalite>) => response.ok),
        map((valeurModalite: HttpResponse<ValeurModalite>) => valeurModalite.body)
      );
    }
    return of(new ValeurModalite());
  }
}

export const valeurModaliteRoute: Routes = [
  {
    path: '',
    component: ValeurModaliteComponent,
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      authorities: ['ROLE_USER'],
      defaultSort: 'id,asc',
      pageTitle: 'ValeurModalites'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: ValeurModaliteDetailComponent,
    resolve: {
      valeurModalite: ValeurModaliteResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'ValeurModalites'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: ValeurModaliteUpdateComponent,
    resolve: {
      valeurModalite: ValeurModaliteResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'ValeurModalites'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: ValeurModaliteUpdateComponent,
    resolve: {
      valeurModalite: ValeurModaliteResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'ValeurModalites'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const valeurModalitePopupRoute: Routes = [
  {
    path: ':id/delete',
    component: ValeurModaliteDeletePopupComponent,
    resolve: {
      valeurModalite: ValeurModaliteResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'ValeurModalites'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];
