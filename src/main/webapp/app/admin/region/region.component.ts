import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { Observable, Subscription } from 'rxjs';
import { JhiEventManager, JhiParseLinks } from 'ng-jhipster';

import { IRegion, Region } from 'app/shared/model/region.model';
import { AccountService } from 'app/core/auth/account.service';

import { ITEMS_PER_PAGE } from 'app/shared/constants/pagination.constants';
import { RegionService } from './region.service';

@Component({
  selector: 'jhi-region',
  templateUrl: './region.component.html',
  styleUrls: ['./region.component.scss']
})
export class RegionComponent implements OnInit, OnDestroy {
  currentAccount: any;
  regions: IRegion[];
  region: IRegion;
  regionsTMP: IRegion[];
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
  display: boolean;
  isSaving: boolean;

  constructor(
    protected regionService: RegionService,
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
    this.regionService
      .query({
        page: this.page - 1,
        size: this.itemsPerPage,
        sort: this.sort()
      })
      .subscribe((res: HttpResponse<IRegion[]>) => this.paginateRegions(res.body, res.headers));
  }

  loadPage(page: number) {
    if (page !== this.previousPage) {
      this.previousPage = page;
      this.transition();
    }
  }

  transition() {
    this.router.navigate(['/admin/region'], {
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
      '/admin/region',
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
    this.isSaving = false;
    this.display = false;
    this.region = new Region();
    this.loadAll();
    this.accountService.identity().subscribe(account => {
      this.currentAccount = account;
    });
    this.registerChangeInRegions();
    this.regionsTMP = [];
  }

  add(region: IRegion) {
    if (region === null) {
      this.region = new Region();
    } else {
      this.region = region;
    }
    this.display = true;
  }

  ngOnDestroy() {
    this.eventManager.destroy(this.eventSubscriber);
  }

  trackId(index: number, item: IRegion) {
    return item.id;
  }

  registerChangeInRegions() {
    this.eventSubscriber = this.eventManager.subscribe('regionListModification', response => this.loadAll());
  }

  sort() {
    const result = [this.predicate + ',' + (this.reverse ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected paginateRegions(data: IRegion[], headers: HttpHeaders) {
    this.links = this.parseLinks.parse(headers.get('link'));
    this.totalItems = parseInt(headers.get('X-Total-Count'), 10);
    this.regions = data.filter(d => d.deleted !== null && d.deleted === false);
    this.regions = data;
    this.regionsTMP = data;
  }

  save() {
    this.isSaving = true;
    if (this.region.id !== undefined) {
      this.subscribeToSaveResponse(this.regionService.update(this.region));
    } else {
      this.subscribeToSaveResponse(this.regionService.create(this.region));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IRegion>>) {
    result.subscribe(() => this.onSaveSuccess(), () => this.onSaveError());
  }

  protected onSaveSuccess() {
    this.isSaving = false;
    this.display = false;
    this.loadAll();
  }

  protected onSaveError() {
    this.isSaving = false;
  }

  annulerSaisie() {
    this.region = new Region();
    this.display = false;
    this.isSaving = false;
    this.loadAll();
  }

  doSearch(event: any) {
    this.regions = event;
  }
}
