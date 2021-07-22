import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IExercice } from 'app/shared/model/exercice.model';

@Component({
  selector: 'jhi-exercice-detail',
  templateUrl: './exercice-detail.component.html'
})
export class ExerciceDetailComponent implements OnInit {
  exercice: IExercice;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ exercice }) => {
      this.exercice = exercice;
    });
  }

  previousState() {
    window.history.back();
  }
}
