import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IDictionaires } from 'app/shared/model/dictionaires.model';

@Component({
  selector: 'jhi-dictionaires-detail',
  templateUrl: './dictionaires-detail.component.html'
})
export class DictionairesDetailComponent implements OnInit {
  dictionaires: IDictionaires;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ dictionaires }) => {
      this.dictionaires = dictionaires;
    });
  }

  previousState() {
    window.history.back();
  }
}
