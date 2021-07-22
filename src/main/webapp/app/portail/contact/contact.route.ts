import { Routes } from '@angular/router';
import { ContactComponent } from 'app/portail/contact/contact.component';

export const contactRoute: Routes = [
  {
    path: '',
    component: ContactComponent,
    data: {
      authorities: [],
      pageTitle: 'Contact'
    }
  }
];
