import { HttpErrorResponse, HttpHeaders, HttpResponse } from '@angular/common/http';
import { Component, OnDestroy, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { DomaineService } from 'app/admin/domaine/domaine.service';
import { IndicateurService } from 'app/admin/indicateur/indicateur.service';
import { AccountService } from 'app/core/auth/account.service';

import { ITEMS_PER_PAGE } from 'app/shared/constants/pagination.constants';

import { Couleur, ICouleur } from 'app/shared/model/couleur.model';
import { IDomaine } from 'app/shared/model/domaine.model';
import { IIndicateur } from 'app/shared/model/indicateur.model';
import { JhiEventManager, JhiParseLinks } from 'ng-jhipster';
import { MessageService } from 'primeng/api';
import { Subscription } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { CouleurService } from './couleur.service';

@Component({
  selector: 'jhi-couleur',
  templateUrl: './couleur.component.html',
  styleUrls: ['./couleur.component.scss']
})
export class CouleurComponent implements OnInit, OnDestroy {
  currentAccount: any;
  couleurs: ICouleur[];
  couleur: ICouleur;
  couleursTMP: ICouleur[];
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
  domaines: IDomaine[];
  domaine: IDomaine;
  indicateurs: IIndicateur[];
  indicateursTMP: IIndicateur[];
  isSaving: boolean;
  couleurTab: ICouleur[];
  checkedAll: boolean;
  colorSellecteds: ICouleur[];
  displayDel: boolean;
  isLoading: boolean;

  constructor(
    protected couleurService: CouleurService,
    protected parseLinks: JhiParseLinks,
    protected accountService: AccountService,
    protected activatedRoute: ActivatedRoute,
    protected router: Router,
    protected eventManager: JhiEventManager,
    private messageService: MessageService,
    protected domaineService: DomaineService,
    protected indicateurService: IndicateurService
  ) {
    this.itemsPerPage = ITEMS_PER_PAGE;
    this.routeData = this.activatedRoute.data.subscribe(data => {
      this.page = data.pagingParams.page;
      this.previousPage = data.pagingParams.page;
      this.reverse = data.pagingParams.ascending;
      this.predicate = data.pagingParams.predicate;
    });
  }

  domaineChange() {
    if (this.domaine !== null && this.domaine.id !== undefined) {
      this.indicateurs = this.indicateursTMP.filter(i => i.domaineId === this.domaine.id);
    } else {
      this.indicateurs = [];
    }
  }

  loadAll() {
    this.isLoading = true;
    this.couleurService
      .query({
        page: this.page - 1,
        size: this.itemsPerPage,
        sort: this.sort()
      })
      .subscribe((res: HttpResponse<ICouleur[]>) => {
        this.isLoading = false;
        this.paginateCouleurs(res.body, res.headers);
      });
  }

  loadPage(page: number) {
    if (page !== this.previousPage) {
      this.previousPage = page;
      this.transition();
    }
  }

  transition() {
    this.router.navigate(['/admin/couleur'], {
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
      '/admin/couleur',
      {
        page: this.page,
        sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
      }
    ]);
    this.loadAll();
  }

  loadIndicateur() {
    this.indicateurService.query().subscribe(
      (res: HttpResponse<IIndicateur[]>) => {
        this.indicateurs = res.body;
        this.indicateursTMP = res.body;
      },
      (res: HttpErrorResponse) => this.onError(res.message)
    );
  }

  ngOnInit() {
    this.displayDel = false;
    this.eventManager.broadcast({
      name: 'endpointChanged',
      content: 'User switch to portail !'
    });
    this.colorSellecteds = [];
    this.checkedAll = false;
    this.display = false;
    this.isSaving = false;
    this.loadAll();
    this.couleur = new Couleur();
    this.couleur.deleted = false;
    this.couleurTab = [];
    this.accountService.identity().subscribe(account => {
      this.currentAccount = account;
    });
    this.domaineService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<IDomaine[]>) => mayBeOk.ok),
        map((response: HttpResponse<IDomaine[]>) => response.body)
      )
      .subscribe((res: IDomaine[]) => (this.domaines = res), (res: HttpErrorResponse) => this.onError(res.message));
    this.loadIndicateur();
    this.registerChangeInCouleurs();
    this.indicateurs = [];
  }

  ngOnDestroy() {
    this.eventManager.destroy(this.eventSubscriber);
  }

  trackId(index: number, item: ICouleur) {
    return item.id;
  }

  registerChangeInCouleurs() {
    this.eventSubscriber = this.eventManager.subscribe('couleurListModification', response => this.loadAll());
  }

  sort() {
    const result = [this.predicate + ',' + (this.reverse ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected paginateCouleurs(data: ICouleur[], headers: HttpHeaders) {
    this.links = this.parseLinks.parse(headers.get('link'));
    this.totalItems = parseInt(headers.get('X-Total-Count'), 10);
    this.couleurs = data;
    this.couleursTMP = data;
  }

  protected onSaveSuccess() {
    this.isSaving = false;
    // this.previousState();
  }

  protected onSaveError() {
    this.isSaving = false;
  }

  protected onError(errorMessage: string) {
    // this.jhiAlertService.error(errorMessage, null, null);
  }

  getMessage(cle: string, severite: string, resume: string, detaille: string) {
    this.messageService.add({ key: cle, severity: severite, summary: resume, detail: detaille });
  }

  add() {
    window.console.log('..........................................' + JSON.stringify(this.couleur) + '...................................');
    if (this.couleur !== null && this.domaine.id !== undefined) {
      if (this.couleurTab !== null && this.couleurTab.length > 0) {
        this.couleurTab.forEach(c => {
          if (c.indicateurId === this.couleur.indicateurId && c.maxVal === this.couleur.maxVal && c.minVal === this.couleur.minVal) {
            this.getMessage('myKey1', 'error', 'Ajout de couleur', 'Cette couleur existe déjà');
          } else {
            this.couleur.idFictif = this.generateId(this.couleurTab);
            this.couleur.deleted = false;
            this.couleurTab.push(this.couleur);
            this.couleur = new Couleur();
          }
        });
      } else {
        this.couleur.idFictif = this.generateId(this.couleurTab);
        this.couleur.deleted = false;
        this.couleurTab.push(this.couleur);
        this.couleur = new Couleur();
        this.couleur.deleted = false;
      }
    }
    window.console.log('..........................................' + this.couleurTab.length + '...................................');
  }

  addColors() {
    if (this.couleur.id === undefined) {
      if (this.couleurTab !== null && this.couleurTab.length > 0) {
        this.couleurService.createAll(this.couleurTab).subscribe((res: HttpResponse<number>) => {
          this.loadAll();
          this.display = false;
          this.couleurTab = [];
        });
      }
    } else {
      this.couleurTab.push(this.couleur);
      this.couleurService.update(this.couleur).subscribe((res: HttpResponse<ICouleur>) => {
        this.loadAll();
        this.display = false;
        this.couleurTab = [];
      });
    }
  }

  annuler() {
    this.couleur = new Couleur();
    this.couleur.deleted = false;
    this.display = false;
    this.loadAll();
  }

  selectAll() {
    this.couleurs.forEach(c => {
      c.checked = this.checkedAll;
      this.colorSellecteds.push(c);
    });
    if (!this.checkedAll) {
      this.colorSellecteds = [];
    }
  }

  checkedOne(color: ICouleur) {
    if (color.checked) {
      this.colorSellecteds.push(color);
      if (this.colorSellecteds.length === this.couleurs.length) {
        this.checkedAll = true;
      }
    } else {
      const index = this.colorSellecteds.indexOf(color);
      this.colorSellecteds.splice(index, 1);
      this.checkedAll = false;
    }
  }

  supprimer() {
    if (this.colorSellecteds !== null && this.colorSellecteds.length > 0) {
      this.colorSellecteds.forEach(col => {
        col.deleted = true;
      });
      this.couleurService.updateAll(this.colorSellecteds).subscribe(
        (res: HttpResponse<number>) => {
          this.loadAll();
          this.displayDel = false;
        },
        (res: HttpErrorResponse) => this.onError(res.message)
      );
    }
  }

  del(color: ICouleur) {
    this.couleurTab = this.couleurTab.filter(c => c.idFictif !== color.idFictif);
    // this.displayDel = true;
  }

  delAll() {
    this.displayDel = true;
  }

  generateId(couleurTab: ICouleur[]): number {
    if (couleurTab.length === 0) {
      return 1;
    } else {
      return couleurTab[couleurTab.length - 1].idFictif + 1;
    }
  }

  annulerDel() {
    this.colorSellecteds = [];
    this.couleurTab = [];
    this.displayDel = false;
  }

  doSearch(event: any) {
    this.couleurs = event;
  }

  update(couleur: ICouleur) {
    this.couleur = couleur;
    this.display = true;
    this.domaine = this.domaines.find(d => d.id === this.indicateurs.find(i => i.id === couleur.indicateurId).domaineId);
  }

  delOne(couleur) {
    this.colorSellecteds.push(couleur);
    this.displayDel = true;
  }

  ajout() {
    this.display = true;
  }

  checValue() {
    if (this.couleur.maxVal && this.couleur.minVal) {
      if (this.couleur.minVal >= this.couleur.maxVal) {
        this.couleur.minVal = undefined;
        this.showMessage('warn', 'Avertissement', 'La valeur minamale doit être inférieure au valeur maximale !');
      }
    }
  }

  showMessage(severity: string, summary: string, detail: string) {
    this.messageService.add({ severity, summary, detail });
  }
}
