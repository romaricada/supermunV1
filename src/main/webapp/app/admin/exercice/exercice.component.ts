import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { Observable, Subscription } from 'rxjs';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { JhiEventManager, JhiParseLinks } from 'ng-jhipster';

import { Exercice, IExercice } from 'app/shared/model/exercice.model';
import { AccountService } from 'app/core/auth/account.service';

import { ITEMS_PER_PAGE } from 'app/shared/constants/pagination.constants';
import { ExerciceService } from './exercice.service';
import { ConfirmationService, MessageService } from 'primeng/api';

@Component({
  selector: 'jhi-exercice',
  templateUrl: './exercice.component.html',
  styleUrls: ['./exercice.component.scss']
})
export class ExerciceComponent implements OnInit, OnDestroy {
  currentAccount: any;
  exercices: IExercice[];
  exercice: IExercice;
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
    protected exerciceService: ExerciceService,
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
    this.exerciceService
      .query({
        page: this.page - 1,
        size: this.itemsPerPage,
        sort: this.sort()
      })
      .subscribe((res: HttpResponse<IExercice[]>) => this.paginateExercices(res.body, res.headers));
  }

  loadPage(page: number) {
    if (page !== this.previousPage) {
      this.previousPage = page;
      this.transition();
    }
  }

  transition() {
    this.router.navigate(['/exercice'], {
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
      '/exercice',
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
    this.display = false;
    this.loadAll();
    this.isSaving = false;
    this.exercice = new Exercice();
    this.accountService.identity().subscribe(account => {
      this.currentAccount = account;
    });
    this.registerChangeInExercices();
  }

  ngOnDestroy() {
    this.eventManager.destroy(this.eventSubscriber);
  }

  trackId(index: number, item: IExercice) {
    return item.id;
  }

  registerChangeInExercices() {
    this.eventSubscriber = this.eventManager.subscribe('exerciceListModification', response => this.loadAll());
  }

  sort() {
    const result = [this.predicate + ',' + (this.reverse ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected paginateExercices(data: IExercice[], headers: HttpHeaders) {
    this.links = this.parseLinks.parse(headers.get('link'));
    this.totalItems = parseInt(headers.get('X-Total-Count'), 10);
    this.exercices = data;
  }

  add(exercice: IExercice) {
    if (exercice === null) {
      this.exercice = new Exercice();
    } else {
      this.exercice = exercice;
    }
    this.display = true;
  }

  save() {
    if (!this.ifExist()) {
      this.isSaving = true;
      if (this.exercice.id !== undefined) {
        this.subscribeToSaveResponse(this.exerciceService.update(this.exercice));
      } else {
        this.exercice.validated = false;
        this.subscribeToSaveResponse(this.exerciceService.create(this.exercice));
      }
    } else {
      this.showMessage('warn', 'Enregistrement', 'un exercice avec la même année existe dèjà!');
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IExercice>>) {
    result.subscribe(() => this.onSaveSuccess(), () => this.onSaveError());
  }

  protected onSaveSuccess() {
    this.showMessage('success', 'Enregistrement', 'Enregistrement effectué avec succès');
    this.annuler();
    this.loadAll();
  }

  annuler() {
    this.display = false;
    this.exercice = new Exercice();
    this.isSaving = false;
  }

  deleteElement(exercice: IExercice) {
    this.confirmationService.confirm({
      header: 'Confirmation',
      message: 'Etes-vous sûr de vouloir supprimer ?',
      accept: () => {
        if (exercice === null) {
          return;
        } else {
          exercice.deleted = true;
          this.exerciceService.delete(exercice.id).subscribe(
            () => {
              this.loadAll();
              this.showMessage('success', 'SUPPRESSION', 'Suppression effectuée avec succès !');
            },
            () => this.showMessage('error', 'SUPPRESSION', 'Echec de la suppression !')
          );
        }
      }
    });
  }

  ifExist(): boolean {
    if (this.exercice.id) {
      return this.exercices.some(value => value.id !== this.exercice.id && value.annee === this.exercice.annee);
    } else {
      return this.exercices.some(value => value.annee === this.exercice.annee);
    }
  }

  protected onSaveError() {
    this.isSaving = false;
  }
  showMessage(sever: string, sum: string, det: string) {
    this.messageService.add({
      severity: sever,
      summary: sum,
      detail: det
    });
  }
}
