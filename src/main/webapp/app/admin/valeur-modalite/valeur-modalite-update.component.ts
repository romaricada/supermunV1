import { Component, OnInit } from '@angular/core';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { CommuneService } from 'app/admin/commune/commune.service';
import { ExerciceService } from 'app/admin/exercice/exercice.service';
import { ModaliteService } from 'app/admin/modalite/modalite.service';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiAlertService, JhiEventManager } from 'ng-jhipster';
import { IValeurModalite, ValeurModalite } from 'app/shared/model/valeur-modalite.model';
import { ValeurModaliteService } from './valeur-modalite.service';
import { IExercice } from 'app/shared/model/exercice.model';
import { ICommune } from 'app/shared/model/commune.model';
import { IModalite } from 'app/shared/model/modalite.model';

@Component({
  selector: 'jhi-valeur-modalite-update',
  templateUrl: './valeur-modalite-update.component.html'
})
export class ValeurModaliteUpdateComponent implements OnInit {
  isSaving: boolean;

  exercices: IExercice[];

  communes: ICommune[];

  modalites: IModalite[];

  editForm = this.fb.group({
    id: [],
    valeur: [null, [Validators.required]],
    exerciceId: [],
    communeId: [],
    modaliteId: []
  });

  constructor(
    protected jhiAlertService: JhiAlertService,
    protected valeurModaliteService: ValeurModaliteService,
    protected exerciceService: ExerciceService,
    protected communeService: CommuneService,
    protected modaliteService: ModaliteService,
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
    this.activatedRoute.data.subscribe(({ valeurModalite }) => {
      this.updateForm(valeurModalite);
    });
    this.exerciceService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<IExercice[]>) => mayBeOk.ok),
        map((response: HttpResponse<IExercice[]>) => response.body)
      )
      .subscribe((res: IExercice[]) => (this.exercices = res), (res: HttpErrorResponse) => this.onError(res.message));
    this.communeService
      .query({ size: 400 })
      .pipe(
        filter((mayBeOk: HttpResponse<ICommune[]>) => mayBeOk.ok),
        map((response: HttpResponse<ICommune[]>) => response.body)
      )
      .subscribe((res: ICommune[]) => (this.communes = res), (res: HttpErrorResponse) => this.onError(res.message));
    this.modaliteService
      .query({ size: 200 })
      .pipe(
        filter((mayBeOk: HttpResponse<IModalite[]>) => mayBeOk.ok),
        map((response: HttpResponse<IModalite[]>) => response.body)
      )
      .subscribe((res: IModalite[]) => (this.modalites = res), (res: HttpErrorResponse) => this.onError(res.message));
  }

  updateForm(valeurModalite: IValeurModalite) {
    this.editForm.patchValue({
      id: valeurModalite.id,
      valeur: valeurModalite.valeur,
      exerciceId: valeurModalite.exerciceId,
      communeId: valeurModalite.communeId,
      modaliteId: valeurModalite.modaliteId
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const valeurModalite = this.createFromForm();
    if (valeurModalite.id !== undefined) {
      this.subscribeToSaveResponse(this.valeurModaliteService.update(valeurModalite));
    } else {
      this.subscribeToSaveResponse(this.valeurModaliteService.create(valeurModalite));
    }
  }

  private createFromForm(): IValeurModalite {
    return {
      ...new ValeurModalite(),
      id: this.editForm.get(['id']).value,
      valeur: this.editForm.get(['valeur']).value,
      exerciceId: this.editForm.get(['exerciceId']).value,
      communeId: this.editForm.get(['communeId']).value,
      modaliteId: this.editForm.get(['modaliteId']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IValeurModalite>>) {
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

  trackExerciceById(index: number, item: IExercice) {
    return item.id;
  }

  trackCommuneById(index: number, item: ICommune) {
    return item.id;
  }

  trackModaliteById(index: number, item: IModalite) {
    return item.id;
  }
}
