import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ITypePublication } from 'app/shared/model/type-publication.model';

@Component({
  selector: 'jhi-type-publication-detail',
  templateUrl: './type-publication-detail.component.html'
})
export class TypePublicationDetailComponent implements OnInit {
  typePublication: ITypePublication;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ typePublication }) => {
      this.typePublication = typePublication;
    });
  }

  previousState() {
    window.history.back();
  }
}
