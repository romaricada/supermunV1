import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { JhiDataUtils } from 'ng-jhipster';

import { IPoster } from 'app/shared/model/poster.model';

@Component({
  selector: 'jhi-poster-detail',
  templateUrl: './poster-detail.component.html'
})
export class PosterDetailComponent implements OnInit {
  poster: IPoster;

  constructor(protected dataUtils: JhiDataUtils, protected activatedRoute: ActivatedRoute) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ poster }) => {
      this.poster = poster;
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
