import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpErrorResponse, HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { Observable, Subscription } from 'rxjs';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { JhiEventManager, JhiParseLinks } from 'ng-jhipster';

import { ITypeIndicateur, TypeIndicateur } from 'app/shared/model/type-indicateur.model';
import { AccountService } from 'app/core/auth/account.service';

import { ITEMS_PER_PAGE } from 'app/shared/constants/pagination.constants';
import { TypeIndicateurService } from './type-indicateur.service';
import { FormBuilder, Validators } from '@angular/forms';
import { MessageService } from 'primeng/api';
import { IIndicateur } from 'app/shared/model/indicateur.model';

@Component({
  selector: 'jhi-type-indicateur',
  templateUrl: './type-indicateur.component.html',
  styleUrls: ['./type-indicateur.component.scss'],
  providers: [MessageService]
})
export class TypeIndicateurComponent implements OnInit, OnDestroy {
  currentAccount: any;
  typeIndicateurs: ITypeIndicateur[];
  typeIndicateursTMP: IIndicateur[];
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
  displayDel: boolean;
  typeIndicateurSelects: ITypeIndicateur[];
  editForm: any;
  checkedAll: boolean;
  blockSpecial = /^[^<>*!%£=+!/$£#@]+$/;

  constructor(
    protected typeIndicateurService: TypeIndicateurService,
    protected parseLinks: JhiParseLinks,
    protected accountService: AccountService,
    protected activatedRoute: ActivatedRoute,
    protected router: Router,
    protected eventManager: JhiEventManager,
    private fb: FormBuilder,
    private messageService: MessageService
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
    this.editForm = this.fb.group({
      id: [],
      libelle: [null, [Validators.required]],
      deleted: []
    });
  }

  delTypeIndicateur(typeIndicateur: ITypeIndicateur) {
    this.typeIndicateurSelects.push(typeIndicateur);
    this.displayDel = true;
  }

  loadAll() {
    this.typeIndicateurService
      .query({
        page: this.page - 1,
        size: this.itemsPerPage,
        sort: this.sort()
      })
      .subscribe((res: HttpResponse<ITypeIndicateur[]>) => this.paginateTypeIndicateurs(res.body, res.headers));
  }

  loadPage(page: number) {
    if (page !== this.previousPage) {
      this.previousPage = page;
      this.transition();
    }
  }

  transition() {
    this.router.navigate(['/type-indicateur'], {
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
      '/type-indicateur',
      {
        page: this.page,
        sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
      }
    ]);
    this.loadAll();
  }

  ngOnInit() {
    this.typeIndicateursTMP = [];
    this.eventManager.broadcast({
      name: 'endpointChanged',
      content: 'User switch to portail !'
    });
    this.typeIndicateurSelects = [];
    this.init();

    this.loadAll();
    this.accountService.identity().subscribe(account => {
      this.currentAccount = account;
    });
    this.registerChangeInTypeIndicateurs();
  }

  updateForm(typeIndicateur: ITypeIndicateur) {
    this.editForm.patchValue({
      id: typeIndicateur.id,
      libelle: typeIndicateur.libelle,
      deleted: typeIndicateur.deleted
    });

    this.display = true;
  }

  save() {
    this.isSaving = true;
    const typeIndicateur = this.createFromForm();
    typeIndicateur.deleted = false;
    if (typeIndicateur.id !== null) {
      this.subscribeToSaveResponse(this.typeIndicateurService.update(typeIndicateur));
    } else {
      this.subscribeToSaveResponse(this.typeIndicateurService.create(typeIndicateur));
    }
  }

  private createFromForm(): ITypeIndicateur {
    return {
      ...new TypeIndicateur(),
      id: this.editForm.get(['id']).value,
      libelle: this.editForm.get(['libelle']).value,
      deleted: this.editForm.get(['deleted']).value
    };
  }

  ngOnDestroy() {
    this.eventManager.destroy(this.eventSubscriber);
  }

  trackId(index: number, item: ITypeIndicateur) {
    return item.id;
  }

  registerChangeInTypeIndicateurs() {
    this.eventSubscriber = this.eventManager.subscribe('typeIndicateurListModification', response => this.loadAll());
  }

  sort() {
    const result = [this.predicate + ',' + (this.reverse ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected paginateTypeIndicateurs(data: ITypeIndicateur[], headers: HttpHeaders) {
    this.links = this.parseLinks.parse(headers.get('link'));
    this.totalItems = parseInt(headers.get('X-Total-Count'), 10);
    this.typeIndicateurs = data;
    this.typeIndicateursTMP = data;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITypeIndicateur>>) {
    result.subscribe(() => this.onSaveSuccess(), () => this.onSaveError());
  }

  protected onSaveSuccess() {
    this.isSaving = false;
    this.loadAll();
    this.init();
    this.display = false;
    // his.previousState();
  }

  protected onSaveError() {
    this.isSaving = false;
  }

  previousState() {
    window.history.back();
  }

  annulerSaisie() {
    this.display = false;
    this.init();
    this.displayDel = false;
    // this.editForm = null;
  }

  annuler(): ITypeIndicateur {
    this.display = false;
    return {
      ...new TypeIndicateur(),
      id: this.editForm.get(['id']).value,
      libelle: this.editForm.get(['libelle']).value,
      deleted: this.editForm.get(['deleted']).value
    };
  }

  isLibelleExist() {
    if (this.typeIndicateurs != null && this.typeIndicateurs.length > 0) {
      this.typeIndicateurs.forEach(typeIndicateur => {
        if (typeIndicateur.libelle === this.editForm.get(['libelle']).value) {
          this.getMessage('myKey1', 'error', "ajout d'un type d'indicateur", typeIndicateur.libelle + 'existe déjà');
          this.isSaving = true;
        }
      });
    }
  }

  selectAll() {
    this.typeIndicateurs.forEach(c => {
      c.checked = this.checkedAll;
      this.typeIndicateurSelects.push(c);
    });
    if (!this.checkedAll) {
      this.typeIndicateurSelects = [];
    }
  }

  delete() {
    this.displayDel = true;
  }

  getMessage(cle: string, severite: string, resume: string, detaille: string) {
    this.messageService.add({ key: cle, severity: severite, summary: resume, detail: detaille });
  }

  checkedOne(color: IIndicateur) {
    if (color.checked) {
      this.typeIndicateurSelects.push(color);
      if (this.typeIndicateurSelects.length === this.typeIndicateurs.length) {
        this.checkedAll = true;
      }
    } else {
      const index = this.typeIndicateurSelects.indexOf(color);
      this.typeIndicateurSelects.splice(index, 1);
      this.checkedAll = false;
    }

    window.console.log('--------------------------------->  ' + this.typeIndicateurSelects.length + '  <----------------------------');
  }

  supprimer() {
    if (this.typeIndicateurSelects.length > 0) {
      this.typeIndicateurSelects.forEach(t => {
        t.deleted = true;
      });
      this.typeIndicateurService.deleteAll(this.typeIndicateurSelects).subscribe(
        (res: HttpResponse<number>) => {
          this.loadAll();
          this.typeIndicateurSelects = [];
          this.displayDel = false;
        },
        (res: HttpErrorResponse) => res.error
      );
    }
  }

  annulerDel() {
    this.typeIndicateurSelects = [];
    this.displayDel = false;
  }

  doSearch(event: any) {
    this.typeIndicateurs = event;
  }
}
