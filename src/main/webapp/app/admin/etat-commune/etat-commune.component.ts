import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { JhiEventManager, JhiParseLinks } from 'ng-jhipster';

import { IEtatCommune } from 'app/shared/model/etat-commune.model';
import { AccountService } from 'app/core/auth/account.service';

import { ITEMS_PER_PAGE } from 'app/shared/constants/pagination.constants';
import { EtatCommuneService } from './etat-commune.service';
import { CommuneService } from 'app/admin/commune/commune.service';
import { IExercice } from 'app/shared/model/exercice.model';
import { IRegion } from 'app/shared/model/region.model';
import { IProvince } from 'app/shared/model/province.model';

@Component({
  selector: 'jhi-etat-commune',
  templateUrl: './etat-commune.component.html'
})
export class EtatCommuneComponent implements OnInit, OnDestroy {
  currentAccount: any;

  exercices: IExercice[] = [];
  selectedExercice: IExercice;

  etatCommunes: IEtatCommune[] = [];
  etatCommunesTMP: IEtatCommune[] = [];

  regions: IRegion[] = [];
  region: IRegion;
  provinces: IProvince[] = [];
  province: IProvince;

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

  constructor(
    protected etatCommuneService: EtatCommuneService,
    protected communeService: CommuneService,
    protected parseLinks: JhiParseLinks,
    protected accountService: AccountService,
    protected activatedRoute: ActivatedRoute,
    protected router: Router,
    protected eventManager: JhiEventManager
  ) {
    this.itemsPerPage = ITEMS_PER_PAGE;
    this.routeData = this.activatedRoute.data.subscribe(data => {
      this.page = data.pagingParams.page;
      this.previousPage = data.pagingParams.page;
      this.reverse = data.pagingParams.ascending;
      this.predicate = data.pagingParams.predicate;
    });
  }

  loadAll() {
    if (this.selectedExercice) {
      this.communeService
        .getAllEtaCommuneByYears(this.selectedExercice.id, {
          page: this.page - 1,
          size: this.itemsPerPage,
          sort: this.sort()
        })
        .subscribe((res: HttpResponse<IEtatCommune[]>) => this.paginateEtatCommunes(res.body, res.headers));
    }
  }

  loadRegions() {}

  loadPage(page: number) {
    if (page !== this.previousPage) {
      this.previousPage = page;
      this.transition();
    }
  }

  transition() {
    this.router.navigate(['/etat-commune'], {
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
      '/etat-commune',
      {
        page: this.page,
        sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
      }
    ]);
    this.loadAll();
  }

  ngOnInit() {
    this.region = null;
    this.province = null;
    this.selectedExercice = null;

    this.loadAll();
    this.accountService.identity().subscribe(account => {
      this.currentAccount = account;
    });
    this.registerChangeInEtatCommunes();
  }

  ngOnDestroy() {
    this.eventManager.destroy(this.eventSubscriber);
  }

  trackId(index: number, item: IEtatCommune) {
    return item.id;
  }

  registerChangeInEtatCommunes() {
    this.eventSubscriber = this.eventManager.subscribe('etatCommuneListModification', response => this.loadAll());
  }

  sort() {
    const result = [this.predicate + ',' + (this.reverse ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected paginateEtatCommunes(data: IEtatCommune[], headers: HttpHeaders) {
    this.links = this.parseLinks.parse(headers.get('link'));
    this.totalItems = parseInt(headers.get('X-Total-Count'), 10);
    this.etatCommunes = data;
  }
}
