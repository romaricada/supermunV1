import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IModalite } from 'app/shared/model/modalite.model';

@Component({
  selector: 'jhi-modalite-detail',
  templateUrl: './modalite-detail.component.html'
})
export class ModaliteDetailComponent implements OnInit {
  modalite: IModalite;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ modalite }) => {
      this.modalite = modalite;
    });
  }

  previousState() {
    window.history.back();
  }
}
