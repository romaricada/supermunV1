import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { Poster } from 'app/shared/model/poster.model';
import { PosterService } from './poster.service';
import { PosterComponent } from './poster.component';
import { PosterDetailComponent } from './poster-detail.component';
import { PosterUpdateComponent } from './poster-update.component';
import { PosterDeletePopupComponent } from './poster-delete-dialog.component';
import { IPoster } from 'app/shared/model/poster.model';

@Injectable({ providedIn: 'root' })
export class PosterResolve implements Resolve<IPoster> {
  constructor(private service: PosterService) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IPoster> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        filter((response: HttpResponse<Poster>) => response.ok),
        map((poster: HttpResponse<Poster>) => poster.body)
      );
    }
    return of(new Poster());
  }
}

export const posterRoute: Routes = [
  {
    path: '',
    component: PosterComponent,
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      authorities: ['ROLE_USER'],
      defaultSort: 'id,asc',
      pageTitle: 'Posters'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: PosterDetailComponent,
    resolve: {
      poster: PosterResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Posters'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: PosterUpdateComponent,
    resolve: {
      poster: PosterResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Posters'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: PosterUpdateComponent,
    resolve: {
      poster: PosterResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Posters'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const posterPopupRoute: Routes = [
  {
    path: ':id/delete',
    component: PosterDeletePopupComponent,
    resolve: {
      poster: PosterResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Posters'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];
