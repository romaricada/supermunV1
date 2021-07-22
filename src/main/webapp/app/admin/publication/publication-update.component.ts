import { Component, OnInit } from '@angular/core';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { JhiAlertService, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { IPublication, Publication } from 'app/shared/model/publication.model';
import { PublicationService } from './publication.service';

@Component({
  selector: 'jhi-publication-update',
  templateUrl: './publication-update.component.html'
})
export class PublicationUpdateComponent implements OnInit {
  isSaving: boolean;

  editForm = this.fb.group({
    id: [],
    libelle: [],
    description: [],
    contenu: [],
    contenuContentType: [],
    published: []
  });

  constructor(
    protected dataUtils: JhiDataUtils,
    protected jhiAlertService: JhiAlertService,
    protected publicationService: PublicationService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder,
    private eventManager: JhiEventManager
  ) {}

  ngOnInit() {
    this.eventManager.broadcast({
      name: 'endpointChanged',
      content: 'User switch to portail !'
    });
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ publication }) => {
      this.updateForm(publication);
    });
  }

  updateForm(publication: IPublication) {
    this.editForm.patchValue({
      id: publication.id,
      libelle: publication.libelle,
      description: publication.description,
      contenu: publication.contenu,
      contenuContentType: publication.contenuContentType,
      published: publication.published
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

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const publication = this.createFromForm();
    if (publication.id !== undefined) {
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
      published: this.editForm.get(['published']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPublication>>) {
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
}
