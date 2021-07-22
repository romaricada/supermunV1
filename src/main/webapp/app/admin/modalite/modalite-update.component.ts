import { Component, OnInit } from '@angular/core';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { IndicateurService } from 'app/admin/indicateur/indicateur.service';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiAlertService, JhiEventManager } from 'ng-jhipster';
import { Formule, IModalite, Modalite } from 'app/shared/model/modalite.model';
import { ModaliteService } from './modalite.service';
import { IIndicateur } from 'app/shared/model/indicateur.model';

@Component({
  selector: 'jhi-modalite-update',
  templateUrl: './modalite-update.component.html'
})
export class ModaliteUpdateComponent implements OnInit {
  isSaving: boolean;

  indicateurs: IIndicateur[];

  editForm = this.fb.group({
    id: [],
    code: [],
    libelle: [],
    borneMaximale: [],
    borneMinimale: [],
    valeur: [],
    deleted: [],
    indicateurId: [],
    formule: Formule
  });

  constructor(
    protected jhiAlertService: JhiAlertService,
    protected modaliteService: ModaliteService,
    protected indicateurService: IndicateurService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder,
    protected eventManager: JhiEventManager
  ) {}

  ngOnInit() {
    this.eventManager.broadcast({
      name: 'endpointChanged',
      content: 'User switch to portail !'
    });
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ modalite }) => {
      this.updateForm(modalite);
    });
    this.indicateurService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<IIndicateur[]>) => mayBeOk.ok),
        map((response: HttpResponse<IIndicateur[]>) => response.body)
      )
      .subscribe((res: IIndicateur[]) => (this.indicateurs = res), (res: HttpErrorResponse) => this.onError(res.message));
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
      // formule: modalite.formule
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const modalite = this.createFromForm();
    if (modalite.id !== undefined) {
      this.subscribeToSaveResponse(this.modaliteService.update(modalite));
    } else {
      this.subscribeToSaveResponse(this.modaliteService.create(modalite));
    }
  }

  private createFromForm(): IModalite {
    return {
      ...new Modalite(),
      id: this.editForm.get(['id']).value,
      code: this.editForm.get(['code']).value,
      libelle: this.editForm.get(['libelle']).value,
      borneMaximale: this.editForm.get(['borneMaximale']).value,
      borneMinimale: this.editForm.get(['borneMinimale']).value,
      valeur: this.editForm.get(['valeur']).value,
      deleted: this.editForm.get(['deleted']).value,
      indicateurId: this.editForm.get(['indicateurId']).value
      // formule: this.editForm.get(['formule']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IModalite>>) {
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

  trackIndicateurById(index: number, item: IIndicateur) {
    return item.id;
  }
}
