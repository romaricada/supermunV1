import { Routes } from '@angular/router';
import { AdminHomeComponent } from 'app/home/admin-home.component';
import { HomeComponent } from 'app/home/home.component';

export const HOME_ROUTE: Routes = [
  {
    path: '',
    component: HomeComponent,
    data: {
      authorities: [],
      pageTitle: 'Bienvenue sur supermun!'
    }
  },
  {
    path: 'admin',
    component: AdminHomeComponent,
    data: {
      authorities: [],
      pageTitle: "Page d'administration"
    }
  }
];
