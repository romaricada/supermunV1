import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { Counte } from 'app/shared/model/counte.model';
import { CounteService } from './counte.service';
import { CounteComponent } from './counte.component';
import { ICounte } from 'app/shared/model/counte.model';

@Injectable({ providedIn: 'root' })
export class CounteResolve implements Resolve<ICounte> {
  constructor(private service: CounteService) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<ICounte> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        filter((response: HttpResponse<Counte>) => response.ok),
        map((counte: HttpResponse<Counte>) => counte.body)
      );
    }
    return of(new Counte());
  }
}

export const counteRoute: Routes = [
  {
    path: '',
    component: CounteComponent,
    data: {
      authorities: [],
      pageTitle: 'Countes'
    }
  }
];
