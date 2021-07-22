import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { JhiDataUtils } from 'ng-jhipster';

import { ICommune } from 'app/shared/model/commune.model';

@Component({
  selector: 'jhi-commune-detail',
  templateUrl: './commune-detail.component.html'
})
export class CommuneDetailComponent implements OnInit {
  commune: ICommune;

  constructor(protected dataUtils: JhiDataUtils, protected activatedRoute: ActivatedRoute) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ commune }) => {
      this.commune = commune;
      window.console.log(this.commune);
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
