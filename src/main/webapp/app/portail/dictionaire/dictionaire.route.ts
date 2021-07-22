import { Routes } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { DictionaireComponent } from 'app/portail/dictionaire/dictionaire.component';

export const routeDictionaire: Routes = [
  {
    path: '',
    component: DictionaireComponent,
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      authorities: [],
      pageTitle: 'Dictionaire'
    }
  }
];
