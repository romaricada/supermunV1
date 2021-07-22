import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { Publication } from 'app/shared/model/publication.model';
import { PublicationService } from './publication.service';
import { PublicationComponent } from './publication.component';
import { PublicationDetailComponent } from './publication-detail.component';
import { PublicationUpdateComponent } from './publication-update.component';
import { PublicationDeletePopupComponent } from './publication-delete-dialog.component';
import { IPublication } from 'app/shared/model/publication.model';

@Injectable({ providedIn: 'root' })
export class PublicationResolve implements Resolve<IPublication> {
  constructor(private service: PublicationService) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IPublication> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        filter((response: HttpResponse<Publication>) => response.ok),
        map((publication: HttpResponse<Publication>) => publication.body)
      );
    }
    return of(new Publication());
  }
}

export const publicationRoute: Routes = [
  {
    path: '',
    component: PublicationComponent,
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      authorities: ['ROLE_USER'],
      defaultSort: 'id,asc',
      pageTitle: 'Publications'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: PublicationDetailComponent,
    resolve: {
      publication: PublicationResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Publications'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: PublicationUpdateComponent,
    resolve: {
      publication: PublicationResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Publications'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: PublicationUpdateComponent,
    resolve: {
      publication: PublicationResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Publications'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const publicationPopupRoute: Routes = [
  {
    path: ':id/delete',
    component: PublicationDeletePopupComponent,
    resolve: {
      publication: PublicationResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Publications'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];
