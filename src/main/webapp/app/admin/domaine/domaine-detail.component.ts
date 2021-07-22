import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { JhiDataUtils } from 'ng-jhipster';

import { IDomaine } from 'app/shared/model/domaine.model';

@Component({
  selector: 'jhi-domaine-detail',
  templateUrl: './domaine-detail.component.html'
})
export class DomaineDetailComponent implements OnInit {
  domaine: IDomaine;

  constructor(protected dataUtils: JhiDataUtils, protected activatedRoute: ActivatedRoute) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ domaine }) => {
      this.domaine = domaine;
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
