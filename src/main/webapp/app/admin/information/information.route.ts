import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { Information } from 'app/shared/model/information.model';
import { InformationService } from './information.service';
import { InformationComponent } from './information.component';
import { InformationDetailComponent } from './information-detail.component';
import { InformationUpdateComponent } from './information-update.component';
import { InformationDeletePopupComponent } from './information-delete-dialog.component';
import { IInformation } from 'app/shared/model/information.model';

@Injectable({ providedIn: 'root' })
export class InformationResolve implements Resolve<IInformation> {
  constructor(private service: InformationService) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IInformation> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        filter((response: HttpResponse<Information>) => response.ok),
        map((information: HttpResponse<Information>) => information.body)
      );
    }
    return of(new Information());
  }
}

export const informationRoute: Routes = [
  {
    path: '',
    component: InformationComponent,
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      authorities: ['ROLE_USER'],
      defaultSort: 'id,asc',
      pageTitle: 'Information'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: InformationDetailComponent,
    resolve: {
      information: InformationResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Information'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: InformationUpdateComponent,
    resolve: {
      information: InformationResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Information'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: InformationUpdateComponent,
    resolve: {
      information: InformationResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Information'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const informationPopupRoute: Routes = [
  {
    path: ':id/delete',
    component: InformationDeletePopupComponent,
    resolve: {
      information: InformationResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Information'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];
