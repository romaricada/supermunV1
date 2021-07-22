import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpErrorResponse, HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { Observable, Subscription } from 'rxjs';
import { JhiEventManager, JhiParseLinks, JhiDataUtils, JhiAlertService } from 'ng-jhipster';
import { Domaine, IDomaine } from 'app/shared/model/domaine.model';
import { AccountService } from 'app/core/auth/account.service';
import { ITEMS_PER_PAGE } from 'app/shared/constants/pagination.constants';
import { DomaineService } from './domaine.service';
import { MessageService } from 'primeng/api';
import { FormBuilder, Validators } from '@angular/forms';
import { ITypeIndicateur, TypeIndicateur } from 'app/shared/model/type-indicateur.model';
import { TypeIndicateurService } from 'app/admin/type-indicateur/type-indicateur.service';

@Component({
  selector: 'jhi-domaine',
  templateUrl: './domaine.component.html',
  styleUrls: ['./domaine.component.scss'],
  providers: [MessageService]
})
export class DomaineComponent implements OnInit, OnDestroy {
  typeIndicateurs: ITypeIndicateur[];

  typeIndicateur: ITypeIndicateur;

  currentAccount: any;
  domaines: IDomaine[];
  domainesTMP: IDomaine[];
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
  editForm: any;
  isSaving: boolean;
  domaine: IDomaine;
  displayImg: boolean;
  blockSpecial = /^[^<>*!%£=+!/$£#@]+$/;
  displayDel: boolean;
  domaineSelects: IDomaine[];
  isLoading: boolean;

  constructor(
    protected domaineService: DomaineService,
    protected parseLinks: JhiParseLinks,
    protected accountService: AccountService,
    protected activatedRoute: ActivatedRoute,
    protected dataUtils: JhiDataUtils,
    protected router: Router,
    protected eventManager: JhiEventManager,
    private fb: FormBuilder,
    protected jhiAlertService: JhiAlertService,
    private messageService: MessageService,
    protected typeIndicateurService: TypeIndicateurService
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
      code: [],
      libelle: [null, [Validators.required]],
      description: [],
      pointTotal: [],
      image: [],
      imageContentType: [],
      deleted: [],
      typeIndicateurId: []
    });
  }

  addDomaine(domaine: IDomaine) {
    if (domaine === null) {
      this.domaine = new Domaine();
    } else {
      this.domaine = domaine;
      this.typeIndicateur = this.typeIndicateurs.find(t => t.id === domaine.typeIndicateurId);
    }
    this.display = true;
  }

  loadAll() {
    this.isLoading = true;
    this.domaineService
      .query({
        page: this.page - 1,
        size: this.itemsPerPage,
        sort: this.sort()
      })
      .subscribe((res: HttpResponse<IDomaine[]>) => {
        this.isLoading = false;
        this.paginateDomaines(res.body, res.headers);
      });
  }

  loadPage(page: number) {
    if (page !== this.previousPage) {
      this.previousPage = page;
      this.transition();
    }
  }

  transition() {
    this.router.navigate(['/admin/domaine'], {
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
      '/admin/domaine',
      {
        page: this.page,
        sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
      }
    ]);
    this.loadAll();
  }

  ngOnInit() {
    this.domaineSelects = [];
    this.displayDel = false;
    this.domainesTMP = [];
    this.eventManager.broadcast({
      name: 'endpointChanged',
      content: 'User switch to portail !'
    });

    this.displayImg = false;
    this.typeIndicateur = new TypeIndicateur();

    this.typeIndicateurs = [];
    this.typeIndicateurService.query().subscribe(
      (res: HttpResponse<ITypeIndicateur[]>) => {
        this.typeIndicateurs = res.body;
      },
      (err: HttpErrorResponse) => this.onError(err.message)
    );

    this.domaine = new Domaine();
    this.init();
    this.loadAll();
    this.accountService.identity().subscribe(account => {
      this.currentAccount = account;
    });
    this.registerChangeInDomaines();
  }

  private createFromForm(): IDomaine {
    return {
      ...new Domaine(),
      id: this.editForm.get(['id']).value,
      code: this.editForm.get(['code']).value,
      libelle: this.editForm.get(['libelle']).value,
      description: this.editForm.get(['description']).value,
      pointTotal: this.editForm.get(['pointTotal']).value,
      imageContentType: this.editForm.get(['imageContentType']).value,
      image: this.editForm.get(['image']).value,
      deleted: this.editForm.get(['deleted']).value,
      typeIndicateurId: this.editForm.get(['typeIndicateurId']).value
    };
  }

  updateForm(domaine: IDomaine) {
    this.editForm.patchValue({
      id: domaine.id,
      code: domaine.code,
      libelle: domaine.libelle,
      description: domaine.description,
      pointTotal: domaine.pointTotal,
      image: domaine.image,
      imageContentType: domaine.imageContentType,
      deleted: domaine.deleted,
      typeIndicateurId: domaine.typeIndicateurId
    });
    this.display = true;
  }

  save() {
    window.console.log(this.ifExist());
    if (this.ifExist() === false) {
      this.isSaving = true;
      const domaine = this.createFromForm();
      domaine.deleted = false;
      if (domaine.id !== null) {
        this.subscribeToSaveResponse(this.domaineService.update(domaine));
      } else {
        this.subscribeToSaveResponse(this.domaineService.create(domaine));
      }
    } else {
      this.message('warn', 'Enregistrement', 'Un domaine du même nom existe déjà dans le groupe de domaine sélectionné');
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IDomaine>>) {
    result.subscribe(() => this.onSaveSuccess(), () => this.onSaveError());
  }

  protected onSaveSuccess() {
    if (this.domaine.id !== undefined) {
      this.message('success', 'Mise à jour', 'Mise à jour effectuée avec succès');
    } else {
      this.message('success', 'Enregistrement', 'Enregistrment effectué avec succès');
    }

    this.loadAll();
    this.isSaving = false;
    this.domaine = new Domaine();
    this.display = false;
  }

  protected onSaveError() {
    this.isSaving = false;
  }

  protected onError(errorMessage: string) {
    this.jhiAlertService.error(errorMessage, null, null);
  }

  previousState() {
    window.history.back();
  }

  ngOnDestroy() {
    this.eventManager.destroy(this.eventSubscriber);
  }

  trackId(index: number, item: IDomaine) {
    return item.id;
  }

  byteSize(field) {
    return this.dataUtils.byteSize(field);
  }

  openFile(contentType, field) {
    return this.dataUtils.openFile(contentType, field);
  }

  registerChangeInDomaines() {
    this.eventSubscriber = this.eventManager.subscribe('domaineListModification', response => this.loadAll());
  }

  sort() {
    const result = [this.predicate + ',' + (this.reverse ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected paginateDomaines(data: IDomaine[], headers: HttpHeaders) {
    this.links = this.parseLinks.parse(headers.get('link'));
    this.totalItems = parseInt(headers.get('X-Total-Count'), 10);
    this.domaines = data;
    this.domainesTMP = data;
  }

  trackTypeIndicateurById(index: number, item: ITypeIndicateur) {
    return item.id;
  }

  annulerSaisie() {
    this.display = false;
    this.init();
    this.domaine = new Domaine();
  }

  setFileData(event, field: string, isImage) {
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
      () => window.console.log('blob added'), // success
      this.onError
    );
  }

  viewImg(img: IDomaine) {
    this.domaine = img;
    this.displayImg = true;
  }

  doSearch(event: any) {
    this.domaines = event;
  }

  message(typeMessage: string, entete: string, contenu: string) {
    this.messageService.add({ key: 'tc', severity: typeMessage, summary: entete, detail: contenu });
  }
  delete(domaine: IDomaine) {
    if (domaine) {
      this.displayDel = true;
      this.domaineSelects.push(domaine);
    }
  }
  validerDel() {
    if (this.domaineSelects.length > 0) {
      this.domaineSelects.forEach(d => {
        d.deleted = true;
      });

      this.domaineService.deleteAll(this.domaineSelects).subscribe(
        (res: HttpResponse<number>) => {
          this.loadAll();
          this.annulerDel();
          this.message('success', 'SUPRESSION', 'Suppression effectuée avec succès');
        },
        () => {
          this.message('error', 'SUPRESSION', 'Supression  échoué !');
        }
      );
    }
  }

  ifExist(): boolean {
    const domaine = this.createFromForm();
    window.console.log(this.domaines);
    if (domaine.id) {
      return this.domaines.some(
        value =>
          value.id !== domaine.id &&
          value.typeIndicateurId === domaine.typeIndicateurId &&
          value.libelle.toLowerCase() === domaine.code.toLowerCase()
      );
    } else {
      window.console.log(domaine);
      return this.domaines.some(
        value => value.libelle.toLowerCase() === domaine.libelle.toLowerCase() && value.typeIndicateurId === domaine.typeIndicateurId
      );
    }
  }

  annulerDel() {
    this.displayDel = false;
    this.domaineSelects = [];
  }
}
