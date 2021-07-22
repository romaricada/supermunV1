import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpErrorResponse, HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { JhiDataUtils, JhiEventManager, JhiParseLinks } from 'ng-jhipster';

import { IValeurModalite } from 'app/shared/model/valeur-modalite.model';
import { AccountService } from 'app/core/auth/account.service';

import { ITEMS_PER_PAGE } from 'app/shared/constants/pagination.constants';
import { ValeurModaliteService } from './valeur-modalite.service';
import { IRegion, Region } from 'app/shared/model/region.model';
import { RegionService } from 'app/admin/region/region.service';
import { IProvince, Province } from 'app/shared/model/province.model';
import { ProvinceService } from 'app/admin/province/province.service';
import { Commune, ICommune } from 'app/shared/model/commune.model';
import { CommuneService } from 'app/admin/commune/commune.service';
import { Exercice, IExercice } from 'app/shared/model/exercice.model';
import { ExerciceService } from 'app/admin/exercice/exercice.service';
import { TypeIndicateurService } from 'app/admin/type-indicateur/type-indicateur.service';
import { ITypeIndicateur } from 'app/shared/model/type-indicateur.model';
import { saveAs } from 'file-saver';

@Component({
  selector: 'jhi-valeur-modalite',
  templateUrl: './valeur-modalite.component.html',
  styleUrls: ['./valeur-modalite.component.scss']
})
export class ValeurModaliteComponent implements OnInit, OnDestroy {
  currentAccount: any;
  valeurModalites: IValeurModalite[];
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
  regions: IRegion[];
  region: IRegion;
  provinces: IProvince[];
  province: IProvince;
  communes: ICommune[];
  commune: ICommune;
  exercices: IExercice[];
  exercice: IExercice;
  typeIndicateurs: ITypeIndicateur[];
  typeIndicateur: ITypeIndicateur;
  showModalImportation: boolean;
  showModalExportation: boolean;
  types: any[];
  type: any;
  fichier: File;

  constructor(
    protected valeurModaliteService: ValeurModaliteService,
    protected parseLinks: JhiParseLinks,
    protected accountService: AccountService,
    protected activatedRoute: ActivatedRoute,
    protected router: Router,
    protected eventManager: JhiEventManager,
    protected regionService: RegionService,
    protected provinceService: ProvinceService,
    protected communeService: CommuneService,
    protected exerciceService: ExerciceService,
    protected typeIndicateurService: TypeIndicateurService,
    protected dataUtils: JhiDataUtils
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
    this.region = new Region();
    this.regions = [];
    this.province = new Province();
    this.provinces = [];
    this.communes = [];
    this.commune = new Commune();
    this.exercice = new Exercice();
    this.exercices = [];
    this.showModalExportation = false;
    this.showModalImportation = false;
    this.typeIndicateur = null;
    this.type = null;
    this.types = [{ id: 0, libelle: 'Fichier Excel' }, { id: 1, libelle: 'Fichier Csv' }];
  }

  loadAll() {
    this.valeurModaliteService
      .getValeurModalitebyCommunebyExercice(this.exercice.id, this.commune.id, {
        page: this.page - 1,
        size: this.itemsPerPage,
        sort: this.sort()
      })
      .subscribe((res: HttpResponse<IValeurModalite[]>) => this.paginateValeurModalites(res.body, res.headers));
  }

  onRegionChange() {
    if (this.region !== null && this.region.id !== undefined) {
      this.provinceService.findProvinceByRegion(this.region.id).subscribe(
        (res: HttpResponse<IProvince[]>) => {
          this.provinces = res.body;
        },
        (resError: HttpErrorResponse) => this.error(resError.message)
      );
    } else {
      this.provinces = [];
    }
  }

  onProvinceChange() {
    if (this.province !== null && this.province.id !== undefined) {
      this.communeService.findCommunesByProvince(this.province.id).subscribe(
        (res: HttpResponse<ICommune[]>) => {
          this.communes = res.body;
        },
        (resError: HttpErrorResponse) => this.error(resError.message)
      );
    } else {
      this.communes = [];
    }
  }

  onCommuneChange(exercice, commune) {
    this.loadAll();
  }

  loadPage(page: number) {
    if (page !== this.previousPage) {
      this.previousPage = page;
      this.transition();
    }
  }

  transition() {
    this.router.navigate(['/admin/valeur-modalite'], {
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
      '/admin/valeur-modalite',
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
    this.init();
    // this.loadAll();
    this.accountService.identity().subscribe(account => {
      this.currentAccount = account;
    });
    this.exerciceService
      .query()
      .subscribe(
        (res: HttpResponse<IExercice[]>) => (this.exercices = res.body),
        (resError: HttpErrorResponse) => this.error(resError.message)
      );
    this.regionService.query().subscribe(
      (res: HttpResponse<IRegion[]>) => {
        this.regions = res.body;
      },
      (resError: HttpErrorResponse) => this.error(resError.message)
    );
    this.typeIndicateurService.query().subscribe(
      (res: HttpResponse<ITypeIndicateur[]>) => {
        this.typeIndicateurs = res.body;
      },
      (err: HttpErrorResponse) => this.error(err.message)
    );

    this.registerChangeInValeurModalites();
  }

  ngOnDestroy() {
    this.eventManager.destroy(this.eventSubscriber);
  }

  trackId(index: number, item: IValeurModalite) {
    return item.id;
  }

  registerChangeInValeurModalites() {
    this.eventSubscriber = this.eventManager.subscribe('valeurModaliteListModification', response => this.loadAll());
  }

  sort() {
    const result = [this.predicate + ',' + (this.reverse ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected paginateValeurModalites(data: IValeurModalite[], headers: HttpHeaders) {
    this.links = this.parseLinks.parse(headers.get('link'));
    this.totalItems = parseInt(headers.get('X-Total-Count'), 10);
    this.valeurModalites = data;
    window.console.log(data);
  }

  modalImportation() {
    this.init();
    this.showModalImportation = true;
    this.showModalExportation = false;
  }

  modalExportation() {
    this.init();
    this.showModalExportation = true;
    this.showModalImportation = false;
  }

  annuler() {
    this.showModalImportation = false;
    this.showModalExportation = false;
  }

  setFileData(event) {
    if (event.target.files.length > 0) {
      this.fichier = event.target.files[0];
      window.console.log(this.fichier);
    }
  }

  exportationCSV(idTypeIndicateur: number) {
    this.valeurModaliteService.exportationCSV(idTypeIndicateur).subscribe(value => {
      const filename = value.headers.get('filename');
      this.saveFile(value.body, filename, 'text/csv');
    });
  }

  exportExcel(idTypeIndicateur: number) {
    this.valeurModaliteService.exportationExcel(idTypeIndicateur).subscribe(value => {
      const filename = value.headers.get('filename');
      this.saveFile(value.body, filename, 'application/vnd.ms.excel');
    });
  }

  saveFile(data: any, filename?: string, type?: string) {
    const blob = new Blob([data], { type: `${type}; charset=utf-8` });
    saveAs(blob, filename);
  }

  exportationFichier(typeIndicateur, type) {
    if (type.id === 0) {
      this.exportExcel(typeIndicateur.id);
    } else {
      this.exportationCSV(typeIndicateur.id);
    }
  }

  importationExcel(exerciciceId: number, typeIndicateurId: number, file: File) {
    this.valeurModaliteService
      .updloadExcelFile(exerciciceId, typeIndicateurId, file)
      .subscribe((res: HttpResponse<boolean>) => {}, (err: HttpErrorResponse) => this.error(err.message));
  }

  importationCsv(exerciciceId: number, typeIndicateurId: number, file: File) {
    this.valeurModaliteService
      .updloadCSVFile(exerciciceId, typeIndicateurId, file)
      .subscribe((res: HttpResponse<boolean>) => {}, (err: HttpErrorResponse) => this.error(err.message));
  }

  importationFichier() {
    if (this.type.id === 0) {
      this.importationExcel(this.exercice.id, this.typeIndicateur.id, this.fichier);
    } else {
      this.importationCsv(this.exercice.id, this.typeIndicateur.id, this.fichier);
    }
  }
}
