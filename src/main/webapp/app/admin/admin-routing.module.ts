import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

/* jhipster-needle-add-admin-module-import - JHipster will add admin modules imports here */

@NgModule({
  imports: [
    /* jhipster-needle-add-admin-module - JHipster will add admin modules here */
    RouterModule.forChild([
      {
        path: 'user-management',
        loadChildren: () => import('./user-management/user-management.module').then(m => m.UserManagementModule)
      },
      {
        path: 'audits',
        loadChildren: () => import('./audits/audits.module').then(m => m.AuditsModule)
      },
      {
        path: 'configuration',
        loadChildren: () => import('./configuration/configuration.module').then(m => m.ConfigurationModule)
      },
      {
        path: 'docs',
        loadChildren: () => import('./docs/docs.module').then(m => m.DocsModule)
      },
      {
        path: 'health',
        loadChildren: () => import('./health/health.module').then(m => m.HealthModule)
      },
      {
        path: 'logs',
        loadChildren: () => import('./logs/logs.module').then(m => m.LogsModule)
      },
      {
        path: 'metrics',
        loadChildren: () => import('./metrics/metrics.module').then(m => m.MetricsModule)
      },
      {
        path: 'region',
        loadChildren: () => import('./region/region.module').then(m => m.SupermunRegionModule)
      },
      {
        path: 'province',
        loadChildren: () => import('./province/province.module').then(m => m.SupermunProvinceModule)
      },
      {
        path: 'commune',
        loadChildren: () => import('./commune/commune.module').then(m => m.SupermunCommuneModule)
      },
      {
        path: 'indicateur',
        loadChildren: () => import('./indicateur/indicateur.module').then(m => m.SupermunIndicateurModule)
      },
      {
        path: 'type-indicateur',
        loadChildren: () => import('./type-indicateur/type-indicateur.module').then(m => m.SupermunTypeIndicateurModule)
      },
      {
        path: 'domaine',
        loadChildren: () => import('./domaine/domaine.module').then(m => m.SupermunDomaineModule)
      },
      {
        path: 'performance',
        loadChildren: () => import('./performance/performance.module').then(m => m.SupermunPerformanceModule)
      },
      {
        path: 'couleur',
        loadChildren: () => import('./couleur/couleur.module').then(m => m.SupermunCouleurModule)
      },
      {
        path: 'modalite',
        loadChildren: () => import('./modalite/modalite.module').then(m => m.SupermunModaliteModule)
      },
      {
        path: 'valeur-modalite',
        loadChildren: () => import('./valeur-modalite/valeur-modalite.module').then(m => m.SupermunValeurModaliteModule)
      },
      {
        path: 'exercice',
        loadChildren: () => import('./exercice/exercice.module').then(m => m.SupermunExerciceModule)
      },
      {
        path: 'poster',
        loadChildren: () => import('./poster/poster.module').then(m => m.SupermunPosterModule)
      },
      {
        path: 'information',
        loadChildren: () => import('./information/information.module').then(m => m.SupermunInformationModule)
      },
      {
        path: 'publication',
        loadChildren: () => import('./publication/publication.module').then(m => m.SupermunPublicationModule)
      },
      {
        path: 'profil',
        loadChildren: () => import('./profil/profil.module').then(m => m.SupermunProfilModule)
      },
      {
        path: 'type-publication',
        loadChildren: () => import('./type-publication/type-publication.module').then(m => m.SupermunTypePublicationModule)
      },
      {
        path: 'etat-commune',
        loadChildren: () => import('./etat-commune/etat-commune.module').then(m => m.SupermunEtatCommuneModule)
      },
      {
        path: 'dictionaires',
        loadChildren: () => import('./dictionaires/dictionaires.module').then(m => m.SupermunDictionairesModule)
      }

      /* jhipster-needle-add-admin-route - JHipster will add admin routes here */
    ])
  ]
})
export class AdminRoutingModule {}
