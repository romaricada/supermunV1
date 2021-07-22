import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpErrorResponse, HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { Observable, Subscription } from 'rxjs';
import { JhiEventManager, JhiParseLinks, JhiDataUtils, JhiAlertService } from 'ng-jhipster';
import { IPublication, Publication } from 'app/shared/model/publication.model';
import { AccountService } from 'app/core/auth/account.service';
import { ITEMS_PER_PAGE } from 'app/shared/constants/pagination.constants';
import { PublicationService } from './publication.service';
import { FormBuilder } from '@angular/forms';
import { TypePublicationService } from 'app/admin/type-publication/type-publication.service';
import { ITypePublication } from 'app/shared/model/type-publication.model';
import { ConfirmationService, MessageService } from 'primeng/api';

@Component({
  selector: 'jhi-publication',
  templateUrl: './publication.component.html',
  styleUrls: ['./publication.component.scss']
})
export class PublicationComponent implements OnInit, OnDestroy {
  currentAccount: any;
  publications: IPublication[] = [];

  typePublications: IPublication[] = [];
  typePublication: IPublication;

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
  publication: IPublication;
  displayImg: boolean;

  constructor(
    protected publicationService: PublicationService,
    protected parseLinks: JhiParseLinks,
    protected accountService: AccountService,
    protected activatedRoute: ActivatedRoute,
    protected dataUtils: JhiDataUtils,
    protected router: Router,
    protected eventManager: JhiEventManager,
    protected typePublicationService: TypePublicationService,
    private fb: FormBuilder,
    protected jhiAlertService: JhiAlertService,
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

  init() {
    this.editForm = this.fb.group({
      id: [],
      libelle: [],
      description: [],
      contenu: [],
      contenuContentType: [],
      published: [],
      typePublicationId: [],
      image: [],
      imageContentType: []
    });
  }

  loadTypePublication() {
    this.typePublicationService.query().subscribe((res: HttpResponse<ITypePublication[]>) => {
      this.typePublications = res.body;
    });
  }

  loadAll() {
    this.publicationService
      .query({
        page: this.page - 1,
        size: this.itemsPerPage,
        sort: this.sort()
      })
      .subscribe((res: HttpResponse<IPublication[]>) => this.paginatePublications(res.body, res.headers));
  }

  loadPage(page: number) {
    if (page !== this.previousPage) {
      this.previousPage = page;
      this.transition();
    }
  }

  transition() {
    this.router.navigate(['/publication'], {
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
      '/admin/publication',
      {
        page: this.page,
        sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
      }
    ]);
    this.loadAll();
  }

  ngOnInit() {
    this.init();
    this.loadAll();
    this.loadTypePublication();
    this.typePublication = null;
    this.displayImg = false;
    this.publication = new Publication();
    this.eventManager.broadcast({
      name: 'endpointChanged',
      content: 'User switch to portail !'
    });
    this.typePublicationService
      .query()
      .subscribe((res: HttpResponse<ITypePublication[]>) => (this.typePublications = res.body), (res: HttpErrorResponse) => res.message);
    this.accountService.identity().subscribe(account => {
      this.currentAccount = account;
    });

    this.registerChangeInPublications();
  }

  save() {
    this.isSaving = true;
    const publication = this.createFromForm();
    window.console.log('=========================' + JSON.stringify(publication) + '================================');
    if (publication.id !== null) {
      this.subscribeToSaveResponse(this.publicationService.update(publication));
    } else {
      this.subscribeToSaveResponse(this.publicationService.create(publication));
    }
  }

  private createFromForm(): IPublication {
    return {
      ...new Publication(),
      id: this.editForm.get(['id']).value,
      libelle: this.editForm.get(['libelle']).value,
      description: this.editForm.get(['description']).value,
      contenuContentType: this.editForm.get(['contenuContentType']).value,
      contenu: this.editForm.get(['contenu']).value,
      published: this.editForm.get(['published']).value,
      typePublicationId: this.editForm.get(['typePublicationId']).value,
      image: this.editForm.get(['image']).value,
      imageContentType: this.editForm.get(['imageContentType']).value
    };
  }

  updateForm(publication: IPublication) {
    this.editForm.patchValue({
      id: publication.id,
      libelle: publication.libelle,
      description: publication.description,
      contenu: publication.contenu,
      contenuContentType: publication.contenuContentType,
      published: publication.published,
      typePublicationId: publication.typePublicationId,
      image: publication.image,
      imageContentType: publication.imageContentType
    });
    this.display = true;
  }

  setFileData(event, field: string, isImage) {
    return new Promise((resolve, reject) => {
      if (!(event && event.target && event.target.files && event.target.files[0])) {
        reject(`Base64 data was not set as file could not be extracted from passed parameter: ${event}`);
      } else {
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
      }
    }).then(
      // eslint-disable-next-line no-console
      () => console.log('blob added'), // success
      this.onError
    );
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPublication>>) {
    result.subscribe(() => this.onSaveSuccess(), () => this.onSaveError());
  }

  previousState() {
    window.history.back();
  }

  protected onSaveSuccess() {
    this.loadAll();
    this.isSaving = false;
    this.publication = new Publication();
    this.display = false;
  }

  protected onSaveError() {
    this.isSaving = false;
  }

  ngOnDestroy() {
    this.eventManager.destroy(this.eventSubscriber);
  }

  trackId(index: number, item: IPublication) {
    return item.id;
  }

  byteSize(field) {
    return this.dataUtils.byteSize(field);
  }

  openFile(contentType, field) {
    return this.dataUtils.openFile(contentType, field);
  }

  registerChangeInPublications() {
    this.eventSubscriber = this.eventManager.subscribe('publicationListModification', response => this.loadAll());
  }

  sort() {
    const result = [this.predicate + ',' + (this.reverse ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected paginatePublications(data: IPublication[], headers: HttpHeaders) {
    this.links = this.parseLinks.parse(headers.get('link'));
    this.totalItems = parseInt(headers.get('X-Total-Count'), 10);
    this.publications = data;
  }

  protected onError(errorMessage: string) {
    this.jhiAlertService.error(errorMessage, null, null);
  }

  handleChange(publication: IPublication) {
    this.publicationService.update(publication).subscribe((res: HttpResponse<IPublication>) => {
      this.loadAll();
    });
  }

  annuler() {
    this.display = false;
    this.publication = new Publication();
    this.init();
    this.isSaving = false;
  }

  viewImg(img: IPublication) {
    this.publication = img;
    this.displayImg = true;
  }

  deleteElement(publication: IPublication) {
    this.confirmationService.confirm({
      header: 'Confirmation',
      message: 'Etes-vous sûr de vouloir supprimer ?',
      accept: () => {
        if (publication === null) {
          return;
        } else {
          publication.deleted = true;
          this.publicationService.update(publication).subscribe(() => {
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
}
