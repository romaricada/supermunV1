import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { Exercice } from 'app/shared/model/exercice.model';
import { ExerciceService } from './exercice.service';
import { ExerciceComponent } from './exercice.component';
import { ExerciceDetailComponent } from './exercice-detail.component';
import { ExerciceUpdateComponent } from './exercice-update.component';
import { ExerciceDeletePopupComponent } from './exercice-delete-dialog.component';
import { IExercice } from 'app/shared/model/exercice.model';

@Injectable({ providedIn: 'root' })
export class ExerciceResolve implements Resolve<IExercice> {
  constructor(private service: ExerciceService) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IExercice> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        filter((response: HttpResponse<Exercice>) => response.ok),
        map((exercice: HttpResponse<Exercice>) => exercice.body)
      );
    }
    return of(new Exercice());
  }
}

export const exerciceRoute: Routes = [
  {
    path: '',
    component: ExerciceComponent,
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      authorities: ['ROLE_USER'],
      defaultSort: 'id,asc',
      pageTitle: 'Exercices'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: ExerciceDetailComponent,
    resolve: {
      exercice: ExerciceResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Exercices'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: ExerciceUpdateComponent,
    resolve: {
      exercice: ExerciceResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Exercices'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: ExerciceUpdateComponent,
    resolve: {
      exercice: ExerciceResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Exercices'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const exercicePopupRoute: Routes = [
  {
    path: ':id/delete',
    component: ExerciceDeletePopupComponent,
    resolve: {
      exercice: ExerciceResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Exercices'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];
