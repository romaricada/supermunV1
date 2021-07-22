import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IValeurModalite } from 'app/shared/model/valeur-modalite.model';

@Component({
  selector: 'jhi-valeur-modalite-detail',
  templateUrl: './valeur-modalite-detail.component.html'
})
export class ValeurModaliteDetailComponent implements OnInit {
  valeurModalite: IValeurModalite;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ valeurModalite }) => {
      this.valeurModalite = valeurModalite;
    });
  }

  previousState() {
    window.history.back();
  }
}
