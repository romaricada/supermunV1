import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IEtatCommune } from 'app/shared/model/etat-commune.model';

@Component({
  selector: 'jhi-etat-commune-detail',
  templateUrl: './etat-commune-detail.component.html'
})
export class EtatCommuneDetailComponent implements OnInit {
  etatCommune: IEtatCommune;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ etatCommune }) => {
      this.etatCommune = etatCommune;
    });
  }

  previousState() {
    window.history.back();
  }
}
