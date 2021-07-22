import { Component, OnInit } from '@angular/core';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { CommuneService } from 'app/admin/commune/commune.service';
import { ExerciceService } from 'app/admin/exercice/exercice.service';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiAlertService } from 'ng-jhipster';
import { IEtatCommune, EtatCommune } from 'app/shared/model/etat-commune.model';
import { EtatCommuneService } from './etat-commune.service';
import { ICommune } from 'app/shared/model/commune.model';
import { IExercice } from 'app/shared/model/exercice.model';

@Component({
  selector: 'jhi-etat-commune-update',
  templateUrl: './etat-commune-update.component.html'
})
export class EtatCommuneUpdateComponent implements OnInit {
  isSaving: boolean;

  communes: ICommune[];

  exercices: IExercice[];

  editForm = this.fb.group({
    id: [],
    validated: [null, [Validators.required]],
    priseEnCompte: [null, [Validators.required]],
    communeId: [null, Validators.required],
    exerciceId: []
  });

  constructor(
    protected jhiAlertService: JhiAlertService,
    protected etatCommuneService: EtatCommuneService,
    protected communeService: CommuneService,
    protected exerciceService: ExerciceService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ etatCommune }) => {
      this.updateForm(etatCommune);
    });
    this.communeService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<ICommune[]>) => mayBeOk.ok),
        map((response: HttpResponse<ICommune[]>) => response.body)
      )
      .subscribe((res: ICommune[]) => (this.communes = res), (res: HttpErrorResponse) => this.onError(res.message));
    this.exerciceService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<IExercice[]>) => mayBeOk.ok),
        map((response: HttpResponse<IExercice[]>) => response.body)
      )
      .subscribe((res: IExercice[]) => (this.exercices = res), (res: HttpErrorResponse) => this.onError(res.message));
  }

  updateForm(etatCommune: IEtatCommune) {
    this.editForm.patchValue({
      id: etatCommune.id,
      validated: etatCommune.validated,
      priseEnCompte: etatCommune.priseEnCompte,
      communeId: etatCommune.communeId,
      exerciceId: etatCommune.exerciceId
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const etatCommune = this.createFromForm();
    if (etatCommune.id !== undefined) {
      this.subscribeToSaveResponse(this.etatCommuneService.update(etatCommune));
    } else {
      this.subscribeToSaveResponse(this.etatCommuneService.create(etatCommune));
    }
  }

  private createFromForm(): IEtatCommune {
    return {
      ...new EtatCommune(),
      id: this.editForm.get(['id']).value,
      validated: this.editForm.get(['validated']).value,
      priseEnCompte: this.editForm.get(['priseEnCompte']).value,
      communeId: this.editForm.get(['communeId']).value,
      exerciceId: this.editForm.get(['exerciceId']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IEtatCommune>>) {
    result.subscribe(() => this.onSaveSuccess(), () => this.onSaveError());
  }

  protected onSaveSuccess() {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError() {
    this.isSaving = false;
  }
  protected onError(errorMessage: string) {
    this.jhiAlertService.error(errorMessage, null, null);
  }

  trackCommuneById(index: number, item: ICommune) {
    return item.id;
  }

  trackExerciceById(index: number, item: IExercice) {
    return item.id;
  }
}
