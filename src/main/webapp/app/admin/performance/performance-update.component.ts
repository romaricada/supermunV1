import { Component, OnInit } from '@angular/core';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { CommuneService } from 'app/admin/commune/commune.service';
import { ExerciceService } from 'app/admin/exercice/exercice.service';
import { IndicateurService } from 'app/admin/indicateur/indicateur.service';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiAlertService, JhiEventManager } from 'ng-jhipster';
import { IPerformance, Performance } from 'app/shared/model/performance.model';
import { PerformanceService } from './performance.service';
import { ICommune } from 'app/shared/model/commune.model';
import { IIndicateur } from 'app/shared/model/indicateur.model';
import { IExercice } from 'app/shared/model/exercice.model';

@Component({
  selector: 'jhi-performance-update',
  templateUrl: './performance-update.component.html'
})
export class PerformanceUpdateComponent implements OnInit {
  isSaving: boolean;

  communes: ICommune[];

  indicateurs: IIndicateur[];

  exercices: IExercice[];

  editForm = this.fb.group({
    id: [],
    score: [null, [Validators.required]],
    deleted: [],
    communeId: [],
    indicateurId: [],
    exerciceId: []
  });

  constructor(
    protected jhiAlertService: JhiAlertService,
    protected performanceService: PerformanceService,
    protected communeService: CommuneService,
    protected indicateurService: IndicateurService,
    protected exerciceService: ExerciceService,
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
    this.activatedRoute.data.subscribe(({ performance }) => {
      this.updateForm(performance);
    });
    this.communeService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<ICommune[]>) => mayBeOk.ok),
        map((response: HttpResponse<ICommune[]>) => response.body)
      )
      .subscribe((res: ICommune[]) => (this.communes = res), (res: HttpErrorResponse) => this.onError(res.message));
    this.indicateurService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<IIndicateur[]>) => mayBeOk.ok),
        map((response: HttpResponse<IIndicateur[]>) => response.body)
      )
      .subscribe((res: IIndicateur[]) => (this.indicateurs = res), (res: HttpErrorResponse) => this.onError(res.message));
    this.exerciceService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<IExercice[]>) => mayBeOk.ok),
        map((response: HttpResponse<IExercice[]>) => response.body)
      )
      .subscribe((res: IExercice[]) => (this.exercices = res), (res: HttpErrorResponse) => this.onError(res.message));
  }

  updateForm(performance: IPerformance) {
    this.editForm.patchValue({
      id: performance.id,
      score: performance.score,
      deleted: performance.deleted,
      communeId: performance.communeId,
      indicateurId: performance.indicateurId,
      exerciceId: performance.exerciceId
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    // const performance = this.createFromForm();
  }

  private createFromForm(): IPerformance {
    return {
      ...new Performance(),
      id: this.editForm.get(['id']).value,
      score: this.editForm.get(['score']).value,
      deleted: this.editForm.get(['deleted']).value,
      communeId: this.editForm.get(['communeId']).value,
      indicateurId: this.editForm.get(['indicateurId']).value,
      exerciceId: this.editForm.get(['exerciceId']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPerformance>>) {
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

  trackIndicateurById(index: number, item: IIndicateur) {
    return item.id;
  }

  trackExerciceById(index: number, item: IExercice) {
    return item.id;
  }
}
