import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { JhiDataUtils } from 'ng-jhipster';

import { IPublication } from 'app/shared/model/publication.model';

@Component({
  selector: 'jhi-publication-detail',
  templateUrl: './publication-detail.component.html'
})
export class PublicationDetailComponent implements OnInit {
  publication: IPublication;

  constructor(protected dataUtils: JhiDataUtils, protected activatedRoute: ActivatedRoute) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ publication }) => {
      this.publication = publication;
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
