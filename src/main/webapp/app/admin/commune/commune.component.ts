import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpErrorResponse, HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { Observable, Subscription } from 'rxjs';
import { JhiEventManager, JhiParseLinks, JhiDataUtils, JhiAlertService } from 'ng-jhipster';

import { Commune, ICommune } from 'app/shared/model/commune.model';
import { AccountService } from 'app/core/auth/account.service';

import { ITEMS_PER_PAGE } from 'app/shared/constants/pagination.constants';
import { CommuneService } from './commune.service';
import { IProvince, Province } from 'app/shared/model/province.model';
import { ConfirmationService, MessageService } from 'primeng/api';
import { FormBuilder, Validators } from '@angular/forms';
import { IRegion, Region } from 'app/shared/model/region.model';
import { RegionService } from 'app/admin/region/region.service';
import { ProvinceService } from 'app/admin/province/province.service';
import { IExercice } from 'app/shared/model/exercice.model';
import { ExerciceService } from 'app/admin/exercice/exercice.service';
import { IEtatCommune } from 'app/shared/model/etat-commune.model';
import { EtatCommuneService } from 'app/admin/etat-commune/etat-commune.service';

@Component({
  selector: 'jhi-commune',
  templateUrl: './commune.component.html',
  styleUrls: ['./commune.component.scss'],
  providers: [MessageService]
})
export class CommuneComponent implements OnInit, OnDestroy {
  isSaving: boolean;
  currentAccount: any;

  communes: ICommune[];
  commune: ICommune;
  communesTMP: ICommune[];

  communesNotSelected: ICommune[] = [];
  communesNotSelected1: ICommune[] = [];
  selectedCommunes: ICommune[] = [];
  selectedCommunesToRemove: ICommune[] = [];

  exercices: IExercice[] = [];
  selectedExercice: IExercice;

  etatCommunes: IEtatCommune[] = [];
  etatCommunesTMP: IEtatCommune[] = [];

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
  regions: IRegion[];
  region: IRegion;
  region1: IRegion;
  provinces: IProvince[];
  province: IProvince;
  province1: IProvince;
  displaySelect: boolean;
  checked: boolean;
  typeLoad: string;

  constructor(
    protected communeService: CommuneService,
    protected parseLinks: JhiParseLinks,
    protected accountService: AccountService,
    protected activatedRoute: ActivatedRoute,
    protected dataUtils: JhiDataUtils,
    protected router: Router,
    protected jhiAlertService: JhiAlertService,
    private fb: FormBuilder,
    protected eventManager: JhiEventManager,
    private messageService: MessageService,
    protected regionService: RegionService,
    protected provinceService: ProvinceService,
    protected exercixeService: ExerciceService,
    protected etatCommuneService: EtatCommuneService,
    protected confirmationService: ConfirmationService
  ) {
    this.itemsPerPage = ITEMS_PER_PAGE;
    this.routeData = this.activatedRoute.data.subscribe(data => {
      this.page = data.pagingParams.page;
      this.previousPage = data.pagingParams.page;
      this.reverse = data.pagingParams.ascending;
      this.predicate = data.pagingParams.predicate;
    });
  }

  init() {
    this.eventManager.broadcast({
      name: 'endpointChanged',
      content: 'User switch to portail !'
    });
    this.editForm = this.fb.group({
      id: [],
      code: [],
      libelle: [null, [Validators.required]],
      population: [],
      superficie: [],
      positionLabelLat: [],
      positionLabelLon: [],
      geom: [],
      deleted: [],
      provinceId: []
    });
  }

  loadPage(page: number) {
    if (page !== this.previousPage) {
      this.previousPage = page;
      this.loadByType(this.typeLoad);
    }
  }

  transition() {
    this.router.navigate(['/admin/commune'], {
      queryParams: {
        page: this.page,
        size: this.itemsPerPage,
        sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
      }
    });
    this.loadByType(this.typeLoad);
  }

  clear() {
    this.page = 0;
    this.router.navigate([
      '/admin/commune',
      {
        page: this.page,
        sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
      }
    ]);
  }

  loadAnnees() {
    this.exercixeService.query().subscribe((res: HttpResponse<IExercice[]>) => {
      this.exercices = res.body;
    });
  }

  ngOnInit() {
    this.province = new Province();
    this.provinces = [];
    this.region = new Region();
    this.regions = [];
    this.commune = new Commune();
    this.selectedExercice = null;
    this.displaySelect = false;
    this.loadRegion();
    this.loadAnnees();

    this.accountService.identity().subscribe(account => {
      this.currentAccount = account;
    });
    this.registerChangeInCommunes();
    this.communesTMP = [];
    this.checked = false;
  }

  ngOnDestroy() {
    this.eventManager.destroy(this.eventSubscriber);
  }

  trackId(index: number, item: ICommune) {
    return item.id;
  }

  byteSize(field) {
    return this.dataUtils.byteSize(field);
  }

  openFile(contentType, field) {
    return this.dataUtils.openFile(contentType, field);
  }

  registerChangeInCommunes() {
    this.eventSubscriber = this.eventManager.subscribe('communeListModification', response => this.loadEtaCommune());
  }

  sort() {
    const result = [this.predicate + ',' + (this.reverse ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected paginateCommunes(data: IEtatCommune[], headers: HttpHeaders) {
    this.links = this.parseLinks.parse(headers.get('link'));
    this.totalItems = parseInt(headers.get('X-Total-Count'), 10);
    this.etatCommunes = data;
    this.etatCommunesTMP = data;
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

    this.display = true;
  }

  edit(commune) {
    this.commune = commune;
    this.display = true;
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

  save() {
    this.isSaving = true;
    if (this.commune.id !== undefined) {
      this.subscribeToSaveResponse(this.communeService.update(this.commune));
    } else {
      this.subscribeToSaveResponse(this.communeService.create(this.commune));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICommune>>) {
    result.subscribe(() => this.onSaveSuccess(), () => this.onSaveError());
  }

  protected onSaveSuccess() {
    this.isSaving = false;
    // this.previousState();
    this.display = false;
  }

  protected onSaveError() {
    this.isSaving = false;
  }

  protected onError(errorMessage: string) {
    this.jhiAlertService.error(errorMessage, null, null);
  }

  annulerSaisie() {
    this.display = false;
    this.init();
    this.commune = new Commune();
  }

  doSearch(event: any) {
    this.communes = event;
  }

  // load eta commune whene exercice change
  loadEtaCommune() {
    this.provinces = [];
    this.region = null;
    this.province = null;
    this.selectedCommunes = [];
    if (this.selectedExercice) {
      this.communeService
        .getAllEtaCommuneByYears(this.selectedExercice.id, {
          page: this.page - 1,
          size: this.itemsPerPage,
          sort: this.sort()
        })
        .subscribe((res: HttpResponse<IEtatCommune[]>) => {
          this.paginateCommunes(res.body, res.headers);
          this.typeLoad = 'all';
        });
    }
  }

  loadRegion() {
    this.regionService.query().subscribe((res: HttpResponse<IRegion[]>) => {
      this.regions = res.body;
    });
  }

  provinceChange1() {
    if (this.province1) {
      // window.console.log(this.communesNotSelected);
      this.communesNotSelected = this.communesNotSelected1.filter(value => value.provinceId === this.province1.id);
    } else {
      this.regionChange1();
    }
  }

  regionChange1() {
    if (this.region1) {
      this.provinces = [];
      this.provinceService.findProvinceByRegion(this.region1.id).subscribe(
        (res: HttpResponse<IProvince[]>) => {
          this.communesNotSelected = this.communesNotSelected1.filter(com => com.libelleRegion === this.region1.libelle);
          this.provinces = res.body;
        },
        (res: HttpErrorResponse) => this.onError(res.error)
      );
    } else {
      this.loadCommuneNoteSelected();
    }
  }

  regionChange() {
    if (this.region !== null && this.region.id !== undefined) {
      this.communeService
        .getAllEtaCommuneByYearAndProvince(this.selectedExercice.id, this.region.id, 'byRegion', {
          page: this.page - 1,
          size: this.itemsPerPage,
          sort: this.sort()
        })
        .subscribe((res1: HttpResponse<IEtatCommune[]>) => {
          // this.paginateCommunes(res1.body, res1.headers);
          this.typeLoad = 'byRegion';
          this.provinceService.findProvinceByRegion(this.region.id).subscribe(
            (res: HttpResponse<IProvince[]>) => {
              this.provinces = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.error)
          );
        });
    } else {
      this.region = new Region();
      this.provinces = [];
      this.province = new Province();
      this.loadEtaCommune();
    }
  }

  provinceChange() {
    if (this.selectedExercice) {
      if (this.province) {
        this.communeService
          .getAllEtaCommuneByYearAndProvince(this.selectedExercice.id, this.province.id, 'byProvince', {
            page: this.page - 1,
            size: this.itemsPerPage,
            sort: this.sort()
          })
          .subscribe((res: HttpResponse<IEtatCommune[]>) => {
            this.paginateCommunes(res.body, res.headers);
            this.typeLoad = 'byProvince';
          });
      } else {
        this.communesTMP = [];
        this.regionChange();
      }
    }
  }

  loadCommuneNoteSelected() {
    this.communeService.getAllNonPriseEnCompte(this.selectedExercice.id).subscribe((res: HttpResponse<ICommune[]>) => {
      this.communesNotSelected = res.body;
      this.communesNotSelected1 = res.body;
    });
  }

  displayDialog() {
    if (this.displaySelect) {
      this.selectedCommunes = [];
      this.displaySelect = false;
    } else {
      if (this.selectedExercice.validated) {
        this.showMessage(
          'info',
          'Information',
          'Les donnée pour cet exercice ont dejà été publier, veuillez les depublier avant toutes modification!'
        );
      } else {
        this.region1 = null;
        this.provinces = [];
        this.province1 = null;
        this.loadCommuneNoteSelected();
        this.selectedCommunes = [];
        this.displaySelect = true;
      }
    }
  }

  addCommunesToCurrentYear() {
    this.confirmationService.confirm({
      header: 'Confirmation',
      message: "Etes-vous sûr de vouloir rajouter ces communes à l'année selectionnée ?",
      accept: () => {
        if (this.checked) {
          this.selectedCommunes = [];
        }
        this.communeService
          .updateCommuneForCurrentYear(this.selectedExercice.id, this.selectedCommunes, this.checked)
          .subscribe((res: HttpResponse<boolean>) => {
            if (res.body) {
              this.displaySelect = false;
              this.loadCommuneNoteSelected();
              this.loadEtaCommune();
            }
          });
      }
    });
  }

  removeCommuneFromYear(option?: string) {
    this.confirmationService.confirm({
      header: 'Confirmation',
      message: 'Etes-vous sûr de vouloir rétirer les commune selectionnées ?',
      accept: () => {
        if (this.selectedExercice.validated) {
          this.showMessage(
            'info',
            'Information',
            'Les donnée pour cet exercice ont dejà été publier, veuillez les depublier avant toutes modification!'
          );
        } else {
          if (option === 'all') {
            this.communeService.removeAllFromYear(this.selectedExercice.id).subscribe(() => {
              this.loadEtaCommune();
            });
          } else {
            this.selectedCommunesToRemove.forEach(commune => {
              this.etatCommuneService.delete(commune.id).subscribe(() => {
                this.loadEtaCommune();
              });
            });
          }
          this.loadCommuneNoteSelected();
          this.selectedCommunesToRemove = [];
        }
      }
    });
  }

  showMessage(sever: string, sum: string, det: string) {
    this.messageService.add({
      severity: sever,
      summary: sum,
      detail: det
    });
  }

  loadByType(loadType: string) {
    switch (loadType) {
      case 'all':
        this.loadEtaCommune();
        break;
      case 'byRegion':
        this.regionChange();
        break;
      case 'byProvince':
        this.provinceChange();
        break;
    }
  }

  onAllChecked(event) {
    this.checked = event.checked;
  }
}
