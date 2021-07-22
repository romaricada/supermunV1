import { Component, OnInit, ElementRef } from '@angular/core';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { TypeIndicateurService } from 'app/admin/type-indicateur/type-indicateur.service';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiAlertService, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { IDomaine, Domaine } from 'app/shared/model/domaine.model';
import { DomaineService } from './domaine.service';
import { ITypeIndicateur } from 'app/shared/model/type-indicateur.model';

@Component({
  selector: 'jhi-domaine-update',
  templateUrl: './domaine-update.component.html'
})
export class DomaineUpdateComponent implements OnInit {
  isSaving: boolean;
  ok = false;
  nonOk = false;

  typeindicateurs: ITypeIndicateur[];

  editForm = this.fb.group({
    id: [],
    code: [],
    libelle: [null, [Validators.required]],
    image: [],
    imageContentType: [],
    deleted: [],
    typeIndicateurId: []
  });

  constructor(
    protected dataUtils: JhiDataUtils,
    protected jhiAlertService: JhiAlertService,
    protected domaineService: DomaineService,
    protected typeIndicateurService: TypeIndicateurService,
    protected elementRef: ElementRef,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder,
    protected eventManager: JhiEventManager
  ) {}

  ngOnInit() {
    this.eventManager.broadcast({
      name: 'endpointChanged',
      content: 'User switch to portail !'
    });

    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ domaine }) => {
      this.updateForm(domaine);
    });
    this.typeIndicateurService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<ITypeIndicateur[]>) => mayBeOk.ok),
        map((response: HttpResponse<ITypeIndicateur[]>) => response.body)
      )
      .subscribe((res: ITypeIndicateur[]) => (this.typeindicateurs = res), (res: HttpErrorResponse) => this.onError(res.message));
  }

  updateForm(domaine: IDomaine) {
    this.editForm.patchValue({
      id: domaine.id,
      code: domaine.code,
      libelle: domaine.libelle,
      description: domaine.description,
      image: domaine.image,
      imageContentType: domaine.imageContentType,
      deleted: domaine.deleted,
      typeIndicateurId: domaine.typeIndicateurId
    });
  }

  byteSize(field) {
    return this.dataUtils.byteSize(field);
  }

  openFile(contentType, field) {
    return this.dataUtils.openFile(contentType, field);
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
      () => console.log('blob added'), // success
      this.onError
    );
  }

  clearInputImage(field: string, fieldContentType: string, idInput: string) {
    this.editForm.patchValue({
      [field]: null,
      [fieldContentType]: null
    });
    if (this.elementRef && idInput && this.elementRef.nativeElement.querySelector('#' + idInput)) {
      this.elementRef.nativeElement.querySelector('#' + idInput).value = null;
    }
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const domaine = this.createFromForm();
    if (domaine.id !== undefined) {
      this.subscribeToSaveResponse(this.domaineService.update(domaine));
    } else {
      this.subscribeToSaveResponse(this.domaineService.create(domaine));
    }
  }

  private createFromForm(): IDomaine {
    return {
      ...new Domaine(),
      id: this.editForm.get(['id']).value,
      code: this.editForm.get(['code']).value,
      libelle: this.editForm.get(['libelle']).value,
      description: this.editForm.get(['description']).value,
      imageContentType: this.editForm.get(['imageContentType']).value,
      image: this.editForm.get(['image']).value,
      deleted: this.editForm.get(['deleted']).value,
      typeIndicateurId: this.editForm.get(['typeIndicateurId']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IDomaine>>) {
    result.subscribe(() => this.onSaveSuccess(), () => this.onSaveError());
  }

  protected onSaveSuccess() {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError() {
    this.isSaving = false;
  }
  protected onError(errorMessage: string) {
    this.jhiAlertService.error(errorMessage, null, null);
  }

  trackTypeIndicateurById(index: number, item: ITypeIndicateur) {
    return item.id;
  }
}
