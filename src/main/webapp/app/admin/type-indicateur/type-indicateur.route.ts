import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { TypeIndicateur } from 'app/shared/model/type-indicateur.model';
import { TypeIndicateurService } from './type-indicateur.service';
import { TypeIndicateurComponent } from './type-indicateur.component';
import { TypeIndicateurDetailComponent } from './type-indicateur-detail.component';
import { TypeIndicateurUpdateComponent } from './type-indicateur-update.component';
import { TypeIndicateurDeletePopupComponent } from './type-indicateur-delete-dialog.component';
import { ITypeIndicateur } from 'app/shared/model/type-indicateur.model';

@Injectable({ providedIn: 'root' })
export class TypeIndicateurResolve implements Resolve<ITypeIndicateur> {
  constructor(private service: TypeIndicateurService) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<ITypeIndicateur> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        filter((response: HttpResponse<TypeIndicateur>) => response.ok),
        map((typeIndicateur: HttpResponse<TypeIndicateur>) => typeIndicateur.body)
      );
    }
    return of(new TypeIndicateur());
  }
}

export const typeIndicateurRoute: Routes = [
  {
    path: '',
    component: TypeIndicateurComponent,
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      authorities: ['ROLE_USER'],
      defaultSort: 'id,asc',
      pageTitle: 'TypeIndicateurs'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: TypeIndicateurDetailComponent,
    resolve: {
      typeIndicateur: TypeIndicateurResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'TypeIndicateurs'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: TypeIndicateurUpdateComponent,
    resolve: {
      typeIndicateur: TypeIndicateurResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'TypeIndicateurs'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: TypeIndicateurUpdateComponent,
    resolve: {
      typeIndicateur: TypeIndicateurResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'TypeIndicateurs'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const typeIndicateurPopupRoute: Routes = [
  {
    path: ':id/delete',
    component: TypeIndicateurDeletePopupComponent,
    resolve: {
      typeIndicateur: TypeIndicateurResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'TypeIndicateurs'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];
