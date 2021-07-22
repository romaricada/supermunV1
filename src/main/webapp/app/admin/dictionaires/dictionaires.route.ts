import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { Dictionaires } from 'app/shared/model/dictionaires.model';
import { DictionairesService } from './dictionaires.service';
import { DictionairesComponent } from './dictionaires.component';
import { DictionairesDetailComponent } from './dictionaires-detail.component';
import { DictionairesUpdateComponent } from './dictionaires-update.component';
import { DictionairesDeletePopupComponent } from './dictionaires-delete-dialog.component';
import { IDictionaires } from 'app/shared/model/dictionaires.model';

@Injectable({ providedIn: 'root' })
export class DictionairesResolve implements Resolve<IDictionaires> {
  constructor(private service: DictionairesService) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IDictionaires> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        filter((response: HttpResponse<Dictionaires>) => response.ok),
        map((dictionaires: HttpResponse<Dictionaires>) => dictionaires.body)
      );
    }
    return of(new Dictionaires());
  }
}

export const dictionairesRoute: Routes = [
  {
    path: '',
    component: DictionairesComponent,
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Dictionaires'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: DictionairesDetailComponent,
    resolve: {
      dictionaires: DictionairesResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Dictionaires'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: DictionairesUpdateComponent,
    resolve: {
      dictionaires: DictionairesResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Dictionaires'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: DictionairesUpdateComponent,
    resolve: {
      dictionaires: DictionairesResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Dictionaires'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const dictionairesPopupRoute: Routes = [
  {
    path: ':id/delete',
    component: DictionairesDeletePopupComponent,
    resolve: {
      dictionaires: DictionairesResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Dictionaires'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];
