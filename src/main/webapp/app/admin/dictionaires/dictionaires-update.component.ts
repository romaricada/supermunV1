import { Component, OnInit } from '@angular/core';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { IDictionaires, Dictionaires } from 'app/shared/model/dictionaires.model';
import { DictionairesService } from './dictionaires.service';

@Component({
  selector: 'jhi-dictionaires-update',
  templateUrl: './dictionaires-update.component.html'
})
export class DictionairesUpdateComponent implements OnInit {
  isSaving: boolean;

  editForm = this.fb.group({
    id: [],
    entite: [],
    definition: [null, [Validators.maxLength(1024)]],
    regleCalcule: [null, [Validators.maxLength(1024)]]
  });

  constructor(protected dictionairesService: DictionairesService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ dictionaires }) => {
      this.updateForm(dictionaires);
    });
  }

  updateForm(dictionaires: IDictionaires) {
    this.editForm.patchValue({
      id: dictionaires.id,
      entite: dictionaires.entite,
      definition: dictionaires.definition,
      regleCalcule: dictionaires.regleCalcule
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const dictionaires = this.createFromForm();
    if (dictionaires.id !== undefined) {
      this.subscribeToSaveResponse(this.dictionairesService.update(dictionaires));
    } else {
      this.subscribeToSaveResponse(this.dictionairesService.create(dictionaires));
    }
  }

  private createFromForm(): IDictionaires {
    return {
      ...new Dictionaires(),
      id: this.editForm.get(['id']).value,
      entite: this.editForm.get(['entite']).value,
      definition: this.editForm.get(['definition']).value,
      regleCalcule: this.editForm.get(['regleCalcule']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IDictionaires>>) {
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
