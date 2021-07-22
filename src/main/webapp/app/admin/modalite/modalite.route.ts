import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { Modalite } from 'app/shared/model/modalite.model';
import { ModaliteService } from './modalite.service';
import { ModaliteComponent } from './modalite.component';
import { ModaliteDetailComponent } from './modalite-detail.component';
import { ModaliteUpdateComponent } from './modalite-update.component';
import { ModaliteDeletePopupComponent } from './modalite-delete-dialog.component';
import { IModalite } from 'app/shared/model/modalite.model';

@Injectable({ providedIn: 'root' })
export class ModaliteResolve implements Resolve<IModalite> {
  constructor(private service: ModaliteService) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IModalite> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        filter((response: HttpResponse<Modalite>) => response.ok),
        map((modalite: HttpResponse<Modalite>) => modalite.body)
      );
    }
    return of(new Modalite());
  }
}

export const modaliteRoute: Routes = [
  {
    path: '',
    component: ModaliteComponent,
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      authorities: ['ROLE_USER'],
      defaultSort: 'id,asc',
      pageTitle: 'Modalites'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: ModaliteDetailComponent,
    resolve: {
      modalite: ModaliteResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Modalites'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: ModaliteUpdateComponent,
    resolve: {
      modalite: ModaliteResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Modalites'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: ModaliteUpdateComponent,
    resolve: {
      modalite: ModaliteResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Modalites'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const modalitePopupRoute: Routes = [
  {
    path: ':id/delete',
    component: ModaliteDeletePopupComponent,
    resolve: {
      modalite: ModaliteResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Modalites'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];
