import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IInformation } from 'app/shared/model/information.model';

@Component({
  selector: 'jhi-information-detail',
  templateUrl: './information-detail.component.html'
})
export class InformationDetailComponent implements OnInit {
  information: IInformation;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ information }) => {
      this.information = information;
    });
  }

  previousState() {
    window.history.back();
  }
}
