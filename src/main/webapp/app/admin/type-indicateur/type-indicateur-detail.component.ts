import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ITypeIndicateur } from 'app/shared/model/type-indicateur.model';

@Component({
  selector: 'jhi-type-indicateur-detail',
  templateUrl: './type-indicateur-detail.component.html'
})
export class TypeIndicateurDetailComponent implements OnInit {
  typeIndicateur: ITypeIndicateur;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ typeIndicateur }) => {
      this.typeIndicateur = typeIndicateur;
    });
  }

  previousState() {
    window.history.back();
  }
}
