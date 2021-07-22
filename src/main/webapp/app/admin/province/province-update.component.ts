import { Component, OnInit } from '@angular/core';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RegionService } from 'app/admin/region/region.service';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiAlertService, JhiEventManager } from 'ng-jhipster';
import { IProvince, Province } from 'app/shared/model/province.model';
import { ProvinceService } from './province.service';
import { IRegion } from 'app/shared/model/region.model';

@Component({
  selector: 'jhi-province-update',
  templateUrl: './province-update.component.html'
})
export class ProvinceUpdateComponent implements OnInit {
  isSaving: boolean;

  regions: IRegion[];

  editForm = this.fb.group({
    id: [],
    code: [null, []],
    libelle: [null, [Validators.required]],
    deleted: [],
    regionId: []
  });

  constructor(
    protected jhiAlertService: JhiAlertService,
    protected provinceService: ProvinceService,
    protected regionService: RegionService,
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
    this.activatedRoute.data.subscribe(({ province }) => {
      this.updateForm(province);
    });
    this.regionService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<IRegion[]>) => mayBeOk.ok),
        map((response: HttpResponse<IRegion[]>) => response.body)
      )
      .subscribe((res: IRegion[]) => (this.regions = res), (res: HttpErrorResponse) => this.onError(res.message));
  }

  updateForm(province: IProvince) {
    this.editForm.patchValue({
      id: province.id,
      code: province.code,
      libelle: province.libelle,
      deleted: province.deleted,
      regionId: province.regionId
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const province = this.createFromForm();
    if (province.id !== undefined) {
      this.subscribeToSaveResponse(this.provinceService.update(province));
    } else {
      this.subscribeToSaveResponse(this.provinceService.create(province));
    }
  }

  private createFromForm(): IProvince {
    return {
      ...new Province(),
      id: this.editForm.get(['id']).value,
      code: this.editForm.get(['code']).value,
      libelle: this.editForm.get(['libelle']).value,
      deleted: this.editForm.get(['deleted']).value,
      regionId: this.editForm.get(['regionId']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IProvince>>) {
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

  trackRegionById(index: number, item: IRegion) {
    return item.id;
  }
}
