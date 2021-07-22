// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { HttpResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { IInformation, Information } from 'app/shared/model/information.model';
import { JhiEventManager } from 'ng-jhipster';
import { Observable } from 'rxjs';
import { InformationService } from './information.service';

@Component({
  selector: 'jhi-information-update',
  templateUrl: './information-update.component.html'
})
export class InformationUpdateComponent implements OnInit {
  isSaving: boolean;

  editForm = this.fb.group({
    id: [],
    presentation: [],
    contact: [],
    deleted: []
  });

  constructor(
    protected eventManager: JhiEventManager,
    protected informationService: InformationService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.eventManager.broadcast({
      name: 'endpointChanged',
      content: 'User switch to portail !'
    });
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ information }) => {
      this.updateForm(information);
    });
  }

  updateForm(information: IInformation) {
    this.editForm.patchValue({
      id: information.id,
      presentation: information.presentation,
      contact: information.contact,
      deleted: information.deleted
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const information = this.createFromForm();
    if (information.id !== undefined) {
      this.subscribeToSaveResponse(this.informationService.update(information));
    } else {
      this.subscribeToSaveResponse(this.informationService.create(information));
    }
  }

  private createFromForm(): IInformation {
    return {
      ...new Information(),
      id: this.editForm.get(['id']).value,
      presentation: this.editForm.get(['presentation']).value,
      contact: this.editForm.get(['contact']).value,
      deleted: this.editForm.get(['deleted']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IInformation>>) {
    result.subscribe(() => this.onSaveSuccess(), () => this.onSaveError());
  }

  protected onSaveSuccess() {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError() {
    this.isSaving = false;
  }
}
