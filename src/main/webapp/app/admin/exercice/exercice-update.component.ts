// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { HttpResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Exercice, IExercice } from 'app/shared/model/exercice.model';
import { JhiEventManager } from 'ng-jhipster';
import { Observable } from 'rxjs';
import { ExerciceService } from './exercice.service';

@Component({
  selector: 'jhi-exercice-update',
  templateUrl: './exercice-update.component.html'
})
export class ExerciceUpdateComponent implements OnInit {
  isSaving: boolean;

  editForm = this.fb.group({
    id: [],
    annee: [null, [Validators.required]],
    deleted: []
  });

  constructor(
    protected eventManager: JhiEventManager,
    protected exerciceService: ExerciceService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.eventManager.broadcast({
      name: 'endpointChanged',
      content: 'User switch to portail !'
    });
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ exercice }) => {
      this.updateForm(exercice);
    });
  }

  updateForm(exercice: IExercice) {
    this.editForm.patchValue({
      id: exercice.id,
      annee: exercice.annee,
      deleted: exercice.deleted
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const exercice = this.createFromForm();
    if (exercice.id !== undefined) {
      this.subscribeToSaveResponse(this.exerciceService.update(exercice));
    } else {
      this.subscribeToSaveResponse(this.exerciceService.create(exercice));
    }
  }

  private createFromForm(): IExercice {
    return {
      ...new Exercice(),
      id: this.editForm.get(['id']).value,
      annee: this.editForm.get(['annee']).value,
      deleted: this.editForm.get(['deleted']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IExercice>>) {
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
