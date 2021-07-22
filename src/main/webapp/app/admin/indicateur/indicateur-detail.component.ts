import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { JhiDataUtils } from 'ng-jhipster';

import { IIndicateur } from 'app/shared/model/indicateur.model';

@Component({
  selector: 'jhi-indicateur-detail',
  templateUrl: './indicateur-detail.component.html'
})
export class IndicateurDetailComponent implements OnInit {
  indicateur: IIndicateur;

  constructor(protected dataUtils: JhiDataUtils, protected activatedRoute: ActivatedRoute) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ indicateur }) => {
      this.indicateur = indicateur;
    });
  }

  byteSize(field) {
    return this.dataUtils.byteSize(field);
  }

  openFile(contentType, field) {
    return this.dataUtils.openFile(contentType, field);
  }
  previousState() {
    window.history.back();
  }
}
