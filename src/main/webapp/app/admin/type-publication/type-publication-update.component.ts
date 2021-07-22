import { Component, OnInit } from '@angular/core';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { JhiEventManager } from 'ng-jhipster';
import { Observable } from 'rxjs';
import { ITypePublication, TypePublication } from 'app/shared/model/type-publication.model';
import { TypePublicationService } from './type-publication.service';

@Component({
  selector: 'jhi-type-publication-update',
  templateUrl: './type-publication-update.component.html'
})
export class TypePublicationUpdateComponent implements OnInit {
  isSaving: boolean;

  editForm = this.fb.group({
    id: [],
    libelle: [null, [Validators.required]]
  });

  constructor(
    protected typePublicationService: TypePublicationService,
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
    this.activatedRoute.data.subscribe(({ typePublication }) => {
      this.updateForm(typePublication);
    });
  }

  updateForm(typePublication: ITypePublication) {
    this.editForm.patchValue({
      id: typePublication.id,
      libelle: typePublication.libelle
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const typePublication = this.createFromForm();
    if (typePublication.id !== undefined) {
      this.subscribeToSaveResponse(this.typePublicationService.update(typePublication));
    } else {
      this.subscribeToSaveResponse(this.typePublicationService.create(typePublication));
    }
  }

  private createFromForm(): ITypePublication {
    return {
      ...new TypePublication(),
      id: this.editForm.get(['id']).value,
      libelle: this.editForm.get(['libelle']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITypePublication>>) {
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
