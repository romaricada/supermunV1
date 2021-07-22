import { Component, OnInit, OnDestroy, ElementRef } from '@angular/core';
import { HttpErrorResponse, HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { Observable, Subscription } from 'rxjs';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { filter, map } from 'rxjs/operators';
import { JhiEventManager, JhiParseLinks, JhiDataUtils, JhiAlertService } from 'ng-jhipster';

import { IIndicateur, Indicateur } from 'app/shared/model/indicateur.model';
import { AccountService } from 'app/core/auth/account.service';

import { ITEMS_PER_PAGE } from 'app/shared/constants/pagination.constants';
import { IndicateurService } from './indicateur.service';
import { IDomaine } from 'app/shared/model/domaine.model';
import { DomaineService } from 'app/admin/domaine/domaine.service';
import { FormBuilder } from '@angular/forms';
import { MessageService } from 'primeng/api';
import { IModalite, Modalite } from 'app/shared/model/modalite.model';

@Component({
  selector: 'jhi-indicateur',
  templateUrl: './indicateur.component.html',
  styleUrls: ['./indicateur.component.scss']
})
export class IndicateurComponent implements OnInit, OnDestroy {
  currentAccount: any;
  indicateurs: IIndicateur[];
  indicateursTMP: IIndicateur[];
  indicateur: IIndicateur;
  modalites: IModalite[];
  modalite: IModalite;
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
  domaines: IDomaine[];
  displayIMG1: boolean;
  image1: any;
  image2: any;
  displayIMG2: any;
  modaliteLength: number;
  editForm: any;
  indicateurSelects: IIndicateur[];
  displayDel = false;
  checkedAll: boolean;
  shearch: any;
  isLoading: boolean;

  constructor(
    protected indicateurService: IndicateurService,
    protected parseLinks: JhiParseLinks,
    protected accountService: AccountService,
    protected activatedRoute: ActivatedRoute,
    protected dataUtils: JhiDataUtils,
    protected router: Router,
    protected domaineService: DomaineService,
    protected eventManager: JhiEventManager,
    protected jhiAlertService: JhiAlertService,
    private fb: FormBuilder,
    protected elementRef: ElementRef,
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
    this.eventManager.broadcast({
      name: 'endpointChanged',
      content: 'User switch to portail !'
    });

    this.indicateur = new Indicateur();
    this.editForm = this.fb.group({
      id: [],
      code: [],
      libelle: [],
      borneMaximale: [],
      borneMinimale: [],
      valeur: [],
      deleted: [],
      indicateurId: []
    });
  }

  add(indicateur: Indicateur) {
    if (indicateur === null) {
      this.indicateur = new Indicateur();
    } else {
      this.indicateur = indicateur;
      this.modalites = indicateur.modalites;
    }
    this.display = true;
  }

  updateForm(modalite: IModalite) {
    this.editForm.patchValue({
      id: modalite.id,
      code: modalite.code,
      libelle: modalite.libelle,
      borneMaximale: modalite.borneMaximale,
      borneMinimale: modalite.borneMinimale,
      valeur: modalite.valeur,
      deleted: modalite.deleted,
      indicateurId: modalite.indicateurId
    });
  }

  loadAll() {
    this.isLoading = true;
    this.indicateurService
      .query({
        page: this.page - 1,
        size: this.itemsPerPage,
        sort: this.sort()
      })
      .subscribe((res: HttpResponse<IIndicateur[]>) => {
        this.isLoading = false;
        this.paginateIndicateurs(res.body, res.headers);
      });
  }

  loadPage(page: number) {
    if (page !== this.previousPage) {
      this.previousPage = page;
      this.transition();
    }
  }

  transition() {
    this.router.navigate(['/admin/indicateur'], {
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
      '/indicateur',
      {
        page: this.page,
        sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
      }
    ]);
    this.loadAll();
  }

  getMessage(cle: string, severite: string, resume: string, detaille: string) {
    this.messageService.add({ key: cle, severity: severite, summary: resume, detail: detaille });
  }

  ngOnInit() {
    this.checkedAll = false;
    this.indicateurSelects = [];
    this.init();
    this.modaliteLength = 0;
    this.isSaving = false;
    this.display = false;
    this.displayIMG1 = false;
    this.modalites = [];
    this.indicateurs = [];
    this.indicateursTMP = [];
    this.modalite = new Modalite();
    this.indicateur = new Indicateur();
    this.shearch = {};
    this.loadAll();
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

    this.registerChangeInIndicateurs();
  }

  ngOnDestroy() {
    this.eventManager.destroy(this.eventSubscriber);
  }

  trackId(index: number, item: IIndicateur) {
    return item.id;
  }

  byteSize(field) {
    return this.dataUtils.byteSize(field);
  }

  openFile(contentType, field) {
    return this.dataUtils.openFile(contentType, field);
  }

  registerChangeInIndicateurs() {
    this.eventSubscriber = this.eventManager.subscribe('indicateurListModification', response => this.loadAll());
  }

  sort() {
    const result = [this.predicate + ',' + (this.reverse ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected paginateIndicateurs(data: IIndicateur[], headers: HttpHeaders) {
    this.links = this.parseLinks.parse(headers.get('link'));
    this.totalItems = parseInt(headers.get('X-Total-Count'), 10);
    this.indicateurs = data.filter(d => d.deleted !== null && d.deleted === false);
    this.indicateursTMP = data;
  }

  indicateurIsExiste() {
    window.console.log('-------------------------existe------------------------');
    if (this.indicateurs !== undefined && this.indicateurs.length > 0) {
      this.indicateurs.forEach(indicateur => {
        if (this.indicateur.code === indicateur.code) {
          this.getMessage('myKey1', 'error', "erreur de code d'incateur", "Ce code existe pour l'indicateur " + indicateur.libelle);
          this.isSaving = true;
        } else {
          // this.isSaving = false;
        }
      });
    }
  }

  save() {
    this.isSaving = true;
    this.indicateur.modalites = this.modalites;
    if (this.indicateur.interval !== true) {
      this.isSaving = false;
    }
    window.console.log('--------------indicateur------------------> ' + JSON.stringify(this.indicateur) + '<------------------------');
    if (this.indicateur.id !== undefined) {
      this.subscribeToSaveResponse(this.indicateurService.update(this.indicateur));
    } else {
      this.subscribeToSaveResponse(this.indicateurService.create(this.indicateur));
    }
    if (this.indicateur.libelle !== null && this.indicateur.interval) {
      this.getMessage(
        'myKey1',
        'error',
        "Ajout/Modification d'un indicateur",
        "impossible d'ajouter l'indicateur" + this.indicateur.libelle + 'et dont la valeur est déterminée selon des intervalles'
      );

      window.console.log('=====================================    ajout impossible !  ==================================');
    } else {
      window.console.log('ok');
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IIndicateur>>) {
    result.subscribe(
      res => {
        this.onSaveSuccess();
        this.indicateurs.push(res.body);
      },
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess() {
    this.isSaving = false;
    // this.loadAll();
    this.display = false;
    this.init();
  }

  protected onSaveError() {
    this.isSaving = false;
  }

  protected onError(errorMessage: string) {
    this.jhiAlertService.error(errorMessage, null, null);
  }

  setFileData(event: any, field: string, isImage) {
    window.console.log('=================event================> ' + JSON.stringify(event) + '<=================================');
    window.console.log('=================field================> ' + field + '<=================================');
    window.console.log('================isImage=================> ' + JSON.stringify(isImage) + '<=================================');
    return new Promise((resolve, reject) => {
      if (event && event.target && event.target.files && event.target.files[0]) {
        const file: File = event.target.files[0];
        if (isImage && !file.type.startsWith('image/')) {
          reject(`File was expected to be an image but was found to be ${file.type}`);
        } else {
          const filedContentType: string = field + 'ContentType';
          this.dataUtils.toBase64(file, base64Data => {
            this.editForm.patchValue({
              [field]: base64Data,
              [filedContentType]: file.type
            });
          });
        }
      } else {
        reject(`Base64 data was not set as file could not be extracted from passed parameter: ${event}`);
      }
    }).then(
      // eslint-disable-next-line no-console
      () => console.log('blob added'), // success
      this.onError
    );
  }

  /* setFile(event, entity: Indicateur, field: string, isImage: boolean) {
    this.dataUtils.setFileData(event, entity, field, isImage);
  } */

  clearInputImage(field: string, fieldContentType: string, idInput: string) {
    if (this.elementRef && idInput && this.elementRef.nativeElement.querySelector('#' + idInput)) {
      this.elementRef.nativeElement.querySelector('#' + idInput).value = null;
    }
  }

  previousState() {
    window.history.back();
  }

  private createFromForm(): IIndicateur {
    this.display = true;
    return null;
  }

  annuler() {
    this.display = false;
    this.init();
    this.indicateur = new Indicateur();
    this.modalites = [];
  }

  img1(data: String) {
    this.image1 = data;
    this.displayIMG1 = true;
  }

  img2(data: String) {
    this.image1 = data;
    this.displayIMG2 = true;
  }

  img(img: IIndicateur) {
    this.indicateur = img;
    this.displayIMG1 = true;
  }

  addModalite() {
    this.modalite.indicateurId = this.indicateur.id;
    this.modalites.push(this.modalite);
    this.modalite = new Modalite();
  }

  generateIdFictif(tab: IModalite[]): number {
    if (tab.length === 0) {
      return 1;
    } else {
      return tab[tab.length - 1].idFictif + 1;
    }
  }

  supprimerModalite(modalite: IModalite) {
    const index = this.modalites.indexOf(modalite);
    this.modalites.splice(index, 1);
  }

  delIndicateur(indicateur: IIndicateur) {
    this.indicateurSelects.push(indicateur);
    this.displayDel = true;
  }

  delete() {
    this.indicateurSelects.forEach(i => {
      i.deleted = true;
    });
    this.indicateurService.updateAll(this.indicateurSelects).subscribe(
      (res: HttpResponse<IIndicateur[]>) => {
        this.loadAll();
        this.displayDel = false;
      },
      (res: HttpErrorResponse) => this.onError(res.message)
    );
  }

  annulerDel() {
    this.indicateurSelects = [];
    this.displayDel = false;
  }

  selectAll() {
    this.indicateurs.forEach(c => {
      c.checked = this.checkedAll;
      this.indicateurSelects.push(c);
    });
    if (!this.checkedAll) {
      this.indicateurSelects = [];
    }
  }

  checkedOne(color: IIndicateur) {
    if (color.checked) {
      this.indicateurSelects.push(color);
      if (this.indicateurSelects.length === this.indicateurs.length) {
        this.checkedAll = true;
      }
    } else {
      const index = this.indicateurSelects.indexOf(color);
      this.indicateurSelects.splice(index, 1);
      this.checkedAll = false;
    }

    window.console.log('--------------------------------->  ' + this.indicateurSelects.length + '  <----------------------------');
  }

  shearchItem() {
    // const indicateurTMP: IIndicateur[] = [];
    if (this.shearch !== null) {
      this.indicateurs.forEach(indicateur => {
        // if (indicateur.libelle.)
      });
    }
  }
  doSearch(event: any) {
    this.indicateurs = event;
  }
}
