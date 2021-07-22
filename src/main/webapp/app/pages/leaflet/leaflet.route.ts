import { Routes } from '@angular/router';

import { MapComponent } from './map.component';
export const LeafletRoute: Routes = [
  {
    path: 'leaflet-map',
    component: MapComponent,
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Maps'
    }
  }
];
