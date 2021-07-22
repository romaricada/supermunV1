import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Component, OnDestroy, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Observable, Subscription } from 'rxjs';
import { JhiAlertService, JhiEventManager, JhiParseLinks } from 'ng-jhipster';
import { IProvince, Province } from 'app/shared/model/province.model';
import { AccountService } from 'app/core/auth/account.service';
import { ITEMS_PER_PAGE } from 'app/shared/constants/pagination.constants';
import { ProvinceService } from './province.service';
import { Commune, ICommune } from 'app/shared/model/commune.model';
import { IRegion } from 'app/shared/model/region.model';
import { MessageService } from 'primeng/api';
import { FormBuilder, Validators } from '@angular/forms';

@Component({
  selector: 'jhi-province',
  templateUrl: './province.component.html',
  styleUrls: ['./province.component.scss'],
  providers: [MessageService]
})
export class ProvinceComponent implements OnInit, OnDestroy {
  currentAccount: any;
  provinces: IProvince[];
  province: IProvince;
  communes: ICommune[];
  commune: ICommune;
  provincesTMP: IProvince[];
  error: any;
  success: any;
  eventSubscriber: Subscription;
  routeData: any;
  links: any;
  totalItems: any;
  itemsPerPage: any;
  page: any;
  predicate: any;
  previousPage: any;
  reverse: any;
  editForm: any;
  display: boolean;
  isSaving: boolean;

  constructor(
    protected jhiAlertService: JhiAlertService,
    protected provinceService: ProvinceService,
    protected parseLinks: JhiParseLinks,
    protected accountService: AccountService,
    protected activatedRoute: ActivatedRoute,
    protected router: Router,
    protected eventManager: JhiEventManager,
    private messageService: MessageService,
    private fb: FormBuilder
  ) {
    this.itemsPerPage = ITEMS_PER_PAGE;
    this.routeData = this.activatedRoute.data.subscribe(data => {
      this.page = data.pagingParams.page;
      this.previousPage = data.pagingParams.page;
      this.reverse = data.pagingParams.ascending;
      this.predicate = data.pagingParams.predicate;
    });
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

  init() {
    this.editForm = this.fb.group({
      id: [],
      code: [],
      libelle: [null, [Validators.required]],
      population: [],
      deleted: [],
      regionId: []
    });
  }

  edit(commune) {
    this.commune = commune;
    this.display = true;
  }

  loadAll() {
    this.provinceService.findAllCommuesByProvince().subscribe(
      (res: HttpResponse<IProvince[]>) => {
        this.provinces = res.body;
        this.provinces = res.body;
        this.provincesTMP = res.body;
        window.console.log('--------------------> ');
        window.console.log(this.provinces);
        window.console.log('--------------------> ');
      },
      (error: HttpErrorResponse) => error.message
    );
  }

  loadPage(page: number) {
    if (page !== this.previousPage) {
      this.previousPage = page;
      this.transition();
    }
  }

  transition() {
    this.router.navigate(['/admin/province'], {
      queryParams: {
        page: this.page,
        size: this.itemsPerPage,
        sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
      }
    });
    this.loadAll();
  }

  clear() {
    this.page = 0;
    this.router.navigate([
      '/admin/province',
      {
        page: this.page,
        sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
      }
    ]);
    this.loadAll();
  }

  ngOnInit() {
    this.eventManager.broadcast({
      name: 'endpointChanged',
      content: 'User switch to portail !'
    });
    this.loadAll();
    this.commune = new Commune();
    this.accountService.identity().subscribe(account => {
      this.currentAccount = account;
    });
    this.registerChangeInProvinces();
    this.provincesTMP = [];
  }

  ngOnDestroy() {
    this.eventManager.destroy(this.eventSubscriber);
  }

  trackId(index: number, item: IProvince) {
    return item.id;
  }

  registerChangeInProvinces() {
    this.eventSubscriber = this.eventManager.subscribe('provinceListModification', response => this.loadAll());
  }

  sort() {
    const result = [this.predicate + ',' + (this.reverse ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
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

  annulerSaisie() {
    this.display = false;
    this.init();
    this.province = new Province();
  }
  doSearch(event: any) {
    this.provinces = event;
  }
}
