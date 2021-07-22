import { Component, OnInit } from '@angular/core';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { ProvinceService } from 'app/admin/province/province.service';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiAlertService, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { ICommune, Commune } from 'app/shared/model/commune.model';
import { CommuneService } from './commune.service';
import { IProvince } from 'app/shared/model/province.model';

@Component({
  selector: 'jhi-commune-update',
  templateUrl: './commune-update.component.html'
})
export class CommuneUpdateComponent implements OnInit {
  isSaving: boolean;

  provinces: IProvince[];

  editForm = this.fb.group({
    id: [],
    code: [null, []],
    libelle: [null, [Validators.required]],
    population: [],
    superficie: [],
    positionLabelLat: [],
    positionLabelLon: [],
    geom: [],
    geomContentType: [],
    deleted: [],
    provinceId: []
  });

  constructor(
    protected dataUtils: JhiDataUtils,
    protected jhiAlertService: JhiAlertService,
    protected communeService: CommuneService,
    protected provinceService: ProvinceService,
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
    this.activatedRoute.data.subscribe(({ commune }) => {
      this.updateForm(commune);
    });
    this.provinceService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<IProvince[]>) => mayBeOk.ok),
        map((response: HttpResponse<IProvince[]>) => response.body)
      )
      .subscribe((res: IProvince[]) => (this.provinces = res), (res: HttpErrorResponse) => this.onError(res.message));
  }

  updateForm(commune: ICommune) {
    this.editForm.patchValue({
      id: commune.id,
      code: commune.code,
      libelle: commune.libelle,
      population: commune.population,
      superficie: commune.superficie,
      positionLabelLat: commune.positionLabelLat,
      positionLabelLon: commune.positionLabelLon,
      geom: commune.geom,
      deleted: commune.deleted,
      provinceId: commune.provinceId
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
    const commune = this.createFromForm();
    if (commune.id !== undefined) {
      this.subscribeToSaveResponse(this.communeService.update(commune));
    } else {
      this.subscribeToSaveResponse(this.communeService.create(commune));
    }
  }

  private createFromForm(): ICommune {
    return {
      ...new Commune(),
      id: this.editForm.get(['id']).value,
      code: this.editForm.get(['code']).value,
      libelle: this.editForm.get(['libelle']).value,
      population: this.editForm.get(['population']).value,
      superficie: this.editForm.get(['superficie']).value,
      positionLabelLat: this.editForm.get(['positionLabelLat']).value,
      positionLabelLon: this.editForm.get(['positionLabelLon']).value,
      geom: this.editForm.get(['geom']).value,
      deleted: this.editForm.get(['deleted']).value,
      provinceId: this.editForm.get(['provinceId']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICommune>>) {
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

  trackProvinceById(index: number, item: IProvince) {
    return item.id;
  }
}
