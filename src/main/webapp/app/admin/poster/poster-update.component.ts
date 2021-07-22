import { Component, OnInit } from '@angular/core';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { CommuneService } from 'app/admin/commune/commune.service';
import { ExerciceService } from 'app/admin/exercice/exercice.service';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiAlertService, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { IPoster, Poster } from 'app/shared/model/poster.model';
import { PosterService } from './poster.service';
import { IExercice } from 'app/shared/model/exercice.model';
import { ICommune } from 'app/shared/model/commune.model';

@Component({
  selector: 'jhi-poster-update',
  templateUrl: './poster-update.component.html'
})
export class PosterUpdateComponent implements OnInit {
  isSaving: boolean;

  exercices: IExercice[];

  communes: ICommune[];

  editForm = this.fb.group({
    id: [],
    url: [],
    contenu: [],
    contenuContentType: [],
    deleted: [],
    exerciceId: [],
    communeId: []
  });

  constructor(
    protected dataUtils: JhiDataUtils,
    protected jhiAlertService: JhiAlertService,
    protected posterService: PosterService,
    protected exerciceService: ExerciceService,
    protected communeService: CommuneService,
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
    this.activatedRoute.data.subscribe(({ poster }) => {
      this.updateForm(poster);
    });
    this.exerciceService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<IExercice[]>) => mayBeOk.ok),
        map((response: HttpResponse<IExercice[]>) => response.body)
      )
      .subscribe((res: IExercice[]) => (this.exercices = res), (res: HttpErrorResponse) => this.onError(res.message));
    this.communeService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<ICommune[]>) => mayBeOk.ok),
        map((response: HttpResponse<ICommune[]>) => response.body)
      )
      .subscribe((res: ICommune[]) => (this.communes = res), (res: HttpErrorResponse) => this.onError(res.message));
  }

  updateForm(poster: IPoster) {
    this.editForm.patchValue({
      id: poster.id,
      url: poster.url,
      contenu: poster.contenu,
      contenuContentType: poster.contenuContentType,
      deleted: poster.deleted,
      exerciceId: poster.exerciceId,
      communeId: poster.communeId
    });
  }

  byteSize(field) {
    return this.dataUtils.byteSize(field);
  }

  openFile(contentType, field) {
    return this.dataUtils.openFile(contentType, field);
  }

  setFileData(event, field: string, isImage) {
    return new Promise((resolve, reject) => {
      if (event && event.target && event.target.files && event.target.files[0]) {
        const file: File = event.target.files[0];
        if (isImage && !file.type.startsWith('image/')) {
          reject(`File was expected to be an image but was found to be ${file.type}`);
        } else {
          const filedContentType: string = field + 'ContentType';
          this.dataUtils.toBase64(file, base64Data => {
            this.editForm.patchValue({
              [field]: base64Data,
              [filedContentType]: file.type
            });
          });
        }
      } else {
        reject(`Base64 data was not set as file could not be extracted from passed parameter: ${event}`);
      }
    }).then(
      // eslint-disable-next-line no-console
      () => console.log('blob added'), // success
      this.onError
    );
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const poster = this.createFromForm();
    if (poster.id !== undefined) {
      this.subscribeToSaveResponse(this.posterService.update(poster));
    } else {
      this.subscribeToSaveResponse(this.posterService.create(poster));
    }
  }

  private createFromForm(): IPoster {
    return {
      ...new Poster(),
      id: this.editForm.get(['id']).value,
      url: this.editForm.get(['url']).value,
      contenuContentType: this.editForm.get(['contenuContentType']).value,
      contenu: this.editForm.get(['contenu']).value,
      deleted: this.editForm.get(['deleted']).value,
      exerciceId: this.editForm.get(['exerciceId']).value,
      communeId: this.editForm.get(['communeId']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPoster>>) {
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
}
