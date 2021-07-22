import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ICouleur } from 'app/shared/model/couleur.model';

@Component({
  selector: 'jhi-couleur-detail',
  templateUrl: './couleur-detail.component.html'
})
export class CouleurDetailComponent implements OnInit {
  couleur: ICouleur;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ couleur }) => {
      this.couleur = couleur;
    });
  }

  previousState() {
    window.history.back();
  }
}
