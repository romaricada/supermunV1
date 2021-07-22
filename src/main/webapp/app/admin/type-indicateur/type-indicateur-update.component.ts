import { Component, OnInit } from '@angular/core';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { JhiEventManager } from 'ng-jhipster';
import { Observable } from 'rxjs';
import { ITypeIndicateur, TypeIndicateur } from 'app/shared/model/type-indicateur.model';
import { TypeIndicateurService } from './type-indicateur.service';

@Component({
  selector: 'jhi-type-indicateur-update',
  templateUrl: './type-indicateur-update.component.html'
})
export class TypeIndicateurUpdateComponent implements OnInit {
  isSaving: boolean;

  editForm = this.fb.group({
    id: [],
    libelle: [null, []],
    deleted: []
  });

  constructor(
    protected typeIndicateurService: TypeIndicateurService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder,
    private eventManager: JhiEventManager
  ) {}

  ngOnInit() {
    this.eventManager.broadcast({
      name: 'endpointChanged',
      content: 'User switch to portail !'
    });
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ typeIndicateur }) => {
      this.updateForm(typeIndicateur);
    });
  }

  updateForm(typeIndicateur: ITypeIndicateur) {
    this.editForm.patchValue({
      id: typeIndicateur.id,
      libelle: typeIndicateur.libelle,
      deleted: typeIndicateur.deleted
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const typeIndicateur = this.createFromForm();
    if (typeIndicateur.id !== undefined) {
      this.subscribeToSaveResponse(this.typeIndicateurService.update(typeIndicateur));
    } else {
      this.subscribeToSaveResponse(this.typeIndicateurService.create(typeIndicateur));
    }
  }

  private createFromForm(): ITypeIndicateur {
    return {
      ...new TypeIndicateur(),
      id: this.editForm.get(['id']).value,
      libelle: this.editForm.get(['libelle']).value,
      deleted: this.editForm.get(['deleted']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITypeIndicateur>>) {
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
