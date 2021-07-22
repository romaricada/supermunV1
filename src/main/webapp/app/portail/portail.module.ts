import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'publicationDoc',
        loadChildren: () => import('./publication/publication.module').then(m => m.PublicationModule)
      },
      {
        path: 'poster',
        loadChildren: () => import('./poster/poster.module').then(m => m.PosterModule)
      },

      {
        path: 'donnee',
        loadChildren: () => import('./donnee/donnee.module').then(m => m.DonneeModule)
      },
      {
        path: 'apropos',
        loadChildren: () => import('./apropos/apropos.module').then(m => m.AproposModule)
      },
      {
        path: 'classement',
        loadChildren: () => import('./classement/classement.module').then(m => m.ClassementModule)
      },
      {
        path: 'contact',
        loadChildren: () => import('./contact/contact.module').then(m => m.ContactModule)
      },
      {
        path: 'dictionnaire',
        loadChildren: () => import('./dictionaire/dictionaire.module').then(m => m.DictionaireModule)
      }
    ])
  ],
  declarations: []
})
export class PortailModule {}
