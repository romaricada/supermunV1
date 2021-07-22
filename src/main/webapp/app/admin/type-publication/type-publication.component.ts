import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { Observable, Subscription } from 'rxjs';
import { JhiEventManager, JhiParseLinks } from 'ng-jhipster';

import { ITypePublication, TypePublication } from 'app/shared/model/type-publication.model';
import { AccountService } from 'app/core/auth/account.service';

import { ITEMS_PER_PAGE } from 'app/shared/constants/pagination.constants';
import { TypePublicationService } from './type-publication.service';
import { ConfirmationService, MessageService } from 'primeng/api';

@Component({
  selector: 'jhi-type-publication',
  templateUrl: './type-publication.component.html',
  styleUrls: ['./type-publication.component.scss']
})
export class TypePublicationComponent implements OnInit, OnDestroy {
  currentAccount: any;
  typePublications: ITypePublication[];
  typePublication: ITypePublication;
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
    protected typePublicationService: TypePublicationService,
    protected parseLinks: JhiParseLinks,
    protected accountService: AccountService,
    protected activatedRoute: ActivatedRoute,
    protected router: Router,
    protected eventManager: JhiEventManager,
    protected confirmationService: ConfirmationService,
    protected messageService: MessageService
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
    this.typePublicationService
      .query({
        page: this.page - 1,
        size: this.itemsPerPage,
        sort: this.sort()
      })
      .subscribe((res: HttpResponse<ITypePublication[]>) => this.paginateTypePublications(res.body, res.headers));
  }

  loadPage(page: number) {
    if (page !== this.previousPage) {
      this.previousPage = page;
      this.transition();
    }
  }

  transition() {
    this.router.navigate(['/type-publication'], {
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
      '/type-publication',
      {
        page: this.page,
        sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
      }
    ]);
    this.loadAll();
  }

  add(type: ITypePublication) {
    if (type === null) {
      this.typePublication = new TypePublication();
    } else {
      this.typePublication = type;
    }
    this.display = true;
  }

  ngOnInit() {
    this.eventManager.broadcast({
      name: 'endpointChanged',
      content: 'User switch to portail !'
    });
    this.isSaving = false;
    this.display = false;
    this.typePublication = new TypePublication();
    this.loadAll();
    this.accountService.identity().subscribe(account => {
      this.currentAccount = account;
    });
    this.registerChangeInTypePublications();
  }

  ngOnDestroy() {
    this.eventManager.destroy(this.eventSubscriber);
  }

  trackId(index: number, item: ITypePublication) {
    return item.id;
  }

  registerChangeInTypePublications() {
    this.eventSubscriber = this.eventManager.subscribe('typePublicationListModification', response => this.loadAll());
  }

  sort() {
    const result = [this.predicate + ',' + (this.reverse ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected paginateTypePublications(data: ITypePublication[], headers: HttpHeaders) {
    this.links = this.parseLinks.parse(headers.get('link'));
    this.totalItems = parseInt(headers.get('X-Total-Count'), 10);
    this.typePublications = data;
  }

  annuler() {
    this.display = false;
    this.typePublication = new TypePublication();
    this.isSaving = false;
  }

  save() {
    this.isSaving = true;
    if (this.typePublication.id !== undefined) {
      this.subscribeToSaveResponse(this.typePublicationService.update(this.typePublication));
    } else {
      this.subscribeToSaveResponse(this.typePublicationService.create(this.typePublication));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITypePublication>>) {
    result.subscribe(() => this.onSaveSuccess(), () => this.onSaveError());
  }

  protected onSaveSuccess() {
    this.isSaving = false;
    this.display = false;
    this.loadAll();
  }

  deleteElement(typPublication: ITypePublication) {
    this.confirmationService.confirm({
      header: 'Confirmation',
      message: 'Etes-vous sûr de vouloir supprimer ?',
      accept: () => {
        if (typPublication === null) {
          return;
        } else {
          this.typePublicationService.delete(typPublication.id).subscribe(() => {
            this.loadAll();
            this.showMessage('success', 'SUPPRESSION', 'Suppression effectuée avec succès !');
          });
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

  protected onSaveError() {
    this.isSaving = false;
  }
}
