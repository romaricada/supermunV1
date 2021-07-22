import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { TypePublication } from 'app/shared/model/type-publication.model';
import { TypePublicationService } from './type-publication.service';
import { TypePublicationComponent } from './type-publication.component';
import { TypePublicationDetailComponent } from './type-publication-detail.component';
import { TypePublicationUpdateComponent } from './type-publication-update.component';
import { TypePublicationDeletePopupComponent } from './type-publication-delete-dialog.component';
import { ITypePublication } from 'app/shared/model/type-publication.model';

@Injectable({ providedIn: 'root' })
export class TypePublicationResolve implements Resolve<ITypePublication> {
  constructor(private service: TypePublicationService) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<ITypePublication> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        filter((response: HttpResponse<TypePublication>) => response.ok),
        map((typePublication: HttpResponse<TypePublication>) => typePublication.body)
      );
    }
    return of(new TypePublication());
  }
}

export const typePublicationRoute: Routes = [
  {
    path: '',
    component: TypePublicationComponent,
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      authorities: ['ROLE_USER'],
      defaultSort: 'id,asc',
      pageTitle: 'TypePublications'
    }
  },
  {
    path: ':id/view',
    component: TypePublicationDetailComponent,
    resolve: {
      typePublication: TypePublicationResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'TypePublications'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: TypePublicationUpdateComponent,
    resolve: {
      typePublication: TypePublicationResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'TypePublications'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: TypePublicationUpdateComponent,
    resolve: {
      typePublication: TypePublicationResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'TypePublications'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const typePublicationPopupRoute: Routes = [
  {
    path: ':id/delete',
    component: TypePublicationDeletePopupComponent,
    resolve: {
      typePublication: TypePublicationResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'TypePublications'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];
