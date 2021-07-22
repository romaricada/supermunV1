import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { Performance } from 'app/shared/model/performance.model';
import { PerformanceService } from './performance.service';
import { PerformanceComponent } from './performance.component';
import { PerformanceDetailComponent } from './performance-detail.component';
import { PerformanceUpdateComponent } from './performance-update.component';
import { PerformanceDeletePopupComponent } from './performance-delete-dialog.component';
import { IPerformance } from 'app/shared/model/performance.model';

@Injectable({ providedIn: 'root' })
export class PerformanceResolve implements Resolve<IPerformance> {
  constructor(private service: PerformanceService) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IPerformance> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        filter((response: HttpResponse<Performance>) => response.ok),
        map((performance: HttpResponse<Performance>) => performance.body)
      );
    }
    return of(new Performance());
  }
}

export const performanceRoute: Routes = [
  {
    path: '',
    component: PerformanceComponent,
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      authorities: ['ROLE_USER'],
      defaultSort: 'id,asc',
      pageTitle: 'Performances'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: PerformanceDetailComponent,
    resolve: {
      performance: PerformanceResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Performances'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: PerformanceUpdateComponent,
    resolve: {
      performance: PerformanceResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Performances'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: PerformanceUpdateComponent,
    resolve: {
      performance: PerformanceResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Performances'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const performancePopupRoute: Routes = [
  {
    path: ':id/delete',
    component: PerformanceDeletePopupComponent,
    resolve: {
      performance: PerformanceResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Performances'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];
