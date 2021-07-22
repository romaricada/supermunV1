import { Component, ElementRef, OnInit } from '@angular/core';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormArray, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { DomaineService } from 'app/admin/domaine/domaine.service';
import { Observable } from 'rxjs';
import { JhiAlertService, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { IIndicateur, Indicateur } from 'app/shared/model/indicateur.model';
import { IndicateurService } from './indicateur.service';
import { IDomaine } from 'app/shared/model/domaine.model';
import { ITypeIndicateur, TypeIndicateur } from 'app/shared/model/type-indicateur.model';
import { TypeIndicateurService } from 'app/admin/type-indicateur/type-indicateur.service';

import { MessageService } from 'primeng/api';
import { IModalite, Modalite } from 'app/shared/model/modalite.model';

@Component({
  selector: 'jhi-indicateur-update',
  templateUrl: './indicateur-update.component.html',
  styleUrls: ['./indicateur-update.component.scss'],
  providers: [MessageService]
})
export class IndicateurUpdateComponent implements OnInit {
  isSaving: boolean;

  indicateurs: IIndicateur[];
  domaines: IDomaine[];
  typeIndicateurs: TypeIndicateur[];
  typeIndicateur: TypeIndicateur;
  modalites: IModalite[];
  modalite: IModalite;
  editForm: FormGroup;
  editFormModalite: any;
  indicateur: IIndicateur;
  blockSpecial = /^[^<>*!%£=+!/$£#@]+$/;
  unites: any[] = [
    { libelle: '', value: null },
    { libelle: 'pourcentage', value: '%' },
    { libelle: 'point', value: 'point' },
    { libelle: 'F CFA', value: 'FCFA' },
    { libelle: 'jour', value: 'jour' }
  ];

  constructor(
    protected dataUtils: JhiDataUtils,
    protected jhiAlertService: JhiAlertService,
    protected indicateurService: IndicateurService,
    protected domaineService: DomaineService,
    protected elementRef: ElementRef,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder,
    private messageService: MessageService,
    protected typeIndicateurService: TypeIndicateurService,
    protected eventManager: JhiEventManager
  ) {}

  loadAll() {
    this.indicateurService
      .query()
      .subscribe(
        (res: HttpResponse<IIndicateur[]>) => (this.indicateurs = res.body),
        (res: HttpErrorResponse) => this.onError(res.message)
      );
  }

  ngOnInit() {
    this.eventManager.broadcast({
      name: 'endpointChanged',
      content: 'User switch to portail !'
    });

    this.domaines = [];
    this.modalites = [];
    this.init();
    this.typeIndicateur = new TypeIndicateur();
    this.typeIndicateurs = [];
    this.isSaving = false;
    this.modalite = new Modalite();
    this.indicateur = new Indicateur();
    this.typeIndicateurService.query().subscribe(
      (res: HttpResponse<ITypeIndicateur[]>) => {
        this.typeIndicateurs = res.body;
        window.console.log('**********************1*****************' + this.typeIndicateurs.length + '**********************************');
      },
      (res: HttpErrorResponse) => this.onError(res.message)
    );
    this.loadService();
    // this.loadAll();
    this.activatedRoute.data.subscribe(({ indicateur }) => {
      this.modalites = indicateur.modalites;
      if (indicateur.id !== undefined) {
        this.updateForm(indicateur);
        // this.typeIndicateur = this.typeIndicateurs.find(t => t.id === indicateur.domaine.typeIndicateurId);
        window.console.log('==================>');
        window.console.log(indicateur);
        window.console.log('<=================');
        /* indicateur.modalites.forEach(i => {
          this.modalitesArray.push(i);
        }); */
      } else {
        this.modalites = [];
      }
    });
  }

  loadService() {
    this.domaineService
      .query()
      .subscribe((res: HttpResponse<IDomaine[]>) => (this.domaines = res.body), (res: HttpErrorResponse) => this.onError(res.message));
  }

  init() {
    this.editForm = this.fb.group({
      id: [],
      code: [null, Validators.required],
      libelle: [null, Validators.required],
      description: [null, Validators.required],
      image1: [],
      image1ContentType: [],
      image2: [],
      image2ContentType: [],
      imageModaliteOK: [],
      imageModaliteOKContentType: [],
      imageModaliteNOK: [],
      imageModaliteNOKContentType: [],
      totalPoint: [],
      interval: [],
      deleted: [],
      domaineId: [],
      typeIndicateur: [],
      modalites: this.fb.array([this.addModalite()]),
      sousIndicateur: [],
      addSousIndicateur: [],
      uniteBorne: [null, Validators.required],
      borneMin: [null, Validators.required],
      borneMax: [null, Validators.required]
    });
  }

  typeIndicateurChange() {
    window.console.log('.........................................................................................................');
    window.console.log(this.editForm.get('typeIndicateur').value);
    if (this.editForm.get('typeIndicateur').value !== null && this.editForm.get('typeIndicateur').value.id !== undefined) {
      this.domaines = this.domaines.filter(d => d.typeIndicateurId === this.editForm.get('typeIndicateur').value.id);
    }
  }

  findIndicateur(indicateur: IIndicateur): string {
    this.loadService();
    // window.console.log('***************************************'+ this.typeIndicateurs.length + '**********************************');
    // indicateur.domaine.libelle;
    return indicateur.domaine.typeIndicateur.libelle;
  }

  updateForm(indicateur: IIndicateur) {
    window.console.log(indicateur.modalites);
    this.editForm.patchValue({
      id: indicateur.id,
      code: indicateur.code,
      libelle: indicateur.libelle,
      description: indicateur.description,
      image1: indicateur.image1,
      image1ContentType: indicateur.image1ContentType,
      image2: indicateur.image2,
      image2ContentType: indicateur.image2ContentType,
      totalPoint: indicateur.totalPoint,
      interval: indicateur.interval,
      deleted: indicateur.deleted,
      domaineId: indicateur.domaineId,
      typeIndicateur: indicateur.domaine.typeIndicateur.libelle,
      sousIndicateur: indicateur.sousIndicateur,
      addSousIndicateur: this.modalites.length > 0 ? true : false,
      uniteBorne: indicateur.uniteBorne,
      borneMin: indicateur.borneMin,
      borneMax: indicateur.borneMax,
      imageModaliteOKContentType: indicateur.imageModaliteOKContentType,
      imageModaliteOK: indicateur.imageModaliteOK,
      imageModaliteNOKContentType: indicateur.imageModaliteNOKContentType,
      imageModaliteNOK: indicateur.imageModaliteNOK
    });
    window.console.log(
      '***************************************' + this.editForm.get('typeIndicateur').value + '**********************************'
    );
    this.editForm.patchValue({
      typeIndicateur: indicateur.domaine.typeIndicateur.libelle
    });
    // this.findIndicateur(indicateur);
  }

  updateModaliteArray(modalites: any[]) {
    if (modalites.length > 0) {
      modalites.forEach(m => {
        this.modalitesArray.push(m);
      });
    }
  }

  /* updateFormModalite(modalite: IModalite) {
    this.editFormModalite.patchValue({
      id: modalite.id,
      code: modalite.code,
      libelle: modalite.libelle,
      borneMaximale: modalite.borneMaximale,
      borneMinimale: modalite.borneMinimale,
      valeur: modalite.valeur,
      deleted: modalite.deleted,
      indicateurId: modalite.indicateurId
    });
  } */

  byteSize(field) {
    return this.dataUtils.byteSize(field);
  }

  openFile(contentType, field) {
    this.dataUtils.openFile(contentType, field);
  }

  get modalitesArray() {
    return this.editForm.get('modalites') as FormArray;
  }

  ajouter() {
    this.modalites.push(this.modalite);
    this.modalite = new Modalite();
    // window.console.log('=======================< ' + this.modalitesArray.length + ' >==============================');
    // this.modalitesArray.push(this.addModalite());
  }

  supprimer(index: number) {
    this.modalitesArray.removeAt(index);
  }

  setFileData(event, field: string, isImage) {
    window.console.log('-----------------event------------------> ' + JSON.stringify(event) + '<-----------------------------------');
    window.console.log('----------------field-------------------> ' + field + '<-----------------------------------');
    window.console.log('------------------isImage-----------------> ' + JSON.stringify(isImage) + '<-----------------------------------');
    return new Promise((resolve, reject) => {
      if (event && event.target && event.target.files && event.target.files[0]) {
        const file: File = event.target.files[0];
        if (isImage && !file.type.startsWith('image/')) {
          reject(`File was expected to be an image but was found to be ${file.type}`);
        } else {
          const filedContentType: string = field + 'ContentType';
          this.dataUtils.toBase64(file, base64Data => {
            // this.indicateur
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

  getMessage(cle: string, severite: string, resume: string, detaille: string) {
    this.messageService.add({ key: cle, severity: severite, summary: resume, detail: detaille });
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
    const indicateur = this.createFrom();
    // indicateur.modalites = this.modalitesArray.value;

    indicateur.deleted = false;

    if (indicateur.interval !== true) {
      indicateur.interval = false;
    }
    if (indicateur.sousIndicateur !== true) {
      indicateur.sousIndicateur = false;
    }
    if (indicateur.addSousIndicateur !== true && indicateur.id === null) {
      indicateur.modalites = [];
    }
    indicateur.modalites = this.modalites;
    window.console.log('--------------indicateur------------------------------------------');
    window.console.log(indicateur);
    if (indicateur.id !== null) {
      this.subscribeToSaveResponse(this.indicateurService.update(indicateur));
    } else {
      this.subscribeToSaveResponse(this.indicateurService.create(indicateur));
    }
  }

  private createFrom(): IIndicateur {
    return {
      ...new Indicateur(),
      id: this.editForm.get(['id']).value,
      code: this.editForm.get(['code']).value,
      libelle: this.editForm.get(['libelle']).value,
      description: this.editForm.get(['description']).value,
      image1ContentType: this.editForm.get(['image1ContentType']).value,
      image1: this.editForm.get(['image1']).value,
      image2ContentType: this.editForm.get(['image2ContentType']).value,
      image2: this.editForm.get(['image2']).value,
      imageModaliteNOK: this.editForm.get('imageModaliteNOK').value,
      imageModaliteOKContentType: this.editForm.get('imageModaliteOKContentType').value,
      imageModaliteOK: this.editForm.get('imageModaliteOK').value,
      imageModaliteNOKContentType: this.editForm.get('imageModaliteNOKContentType').value,
      totalPoint: this.editForm.get(['totalPoint']).value,
      interval: this.editForm.get(['interval']).value,
      deleted: this.editForm.get(['deleted']).value,
      domaineId: this.editForm.get(['domaineId']).value,
      typeIndicateur: this.editForm.get(['typeIndicateur']).value,
      modalites: this.fb.array([this.addModalite()]).value,
      sousIndicateur: this.editForm.get(['sousIndicateur']).value,
      addSousIndicateur: this.editForm.get(['addSousIndicateur']).value,
      uniteBorne: this.editForm.get(['uniteBorne']).value,
      borneMin: this.editForm.get(['borneMin']).value,
      borneMax: this.editForm.get(['borneMax']).value
    };
  }

  private addModalite() {
    return this.fb.group({
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

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IIndicateur>>) {
    result.subscribe(
      () => {
        this.onSaveSuccess();
      },
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess() {
    this.isSaving = false;
    const indicateur = this.createFrom();
    if (indicateur.id !== null) {
      this.showMessage('success', 'MODIFICATION', 'Mise à jour effectuée avec succès !');
      this.previousState();
    } else {
      this.showMessage('success', 'ENREGISTREMENT', 'Indicateur enregistré avec succès !');
      this.init();
    }
    // this.previousState();
  }

  protected onSaveError() {
    this.isSaving = false;
  }
  protected onError(errorMessage: string) {
    this.jhiAlertService.error(errorMessage, null, null);
  }

  supprimerModalite(modalite) {
    const index = this.modalites.indexOf(modalite);
    this.modalites.splice(index, 1);
  }

  trackDomaineById(index: number, item: IDomaine) {
    return item.id;
  }

  intervalChecked() {
    if (this.modalitesArray !== null && this.modalitesArray.getRawValue().length > 0) {
      this.modalitesArray.getRawValue().forEach(m => {
        m.libelle = null;
      });
    } else {
      this.modalitesArray.getRawValue().forEach(m => {
        m.borneMaximale = null;
        m.borneMinimale = null;
      });
    }
  }
  updateIntervalle(intervalle: any) {
    this.editFormModalite.patchValue({
      interval: false
    });
    window.console.log('------------------------------> ' + this.editForm.get('interval').value + '<------------------------------------');
  }

  okChecked() {
    this.editForm.setValue({
      interval: !this.editForm.get('interval').value
    });

    // this.intervalChecked();
    window.console.log('................................> ' + this.editForm.get('interval').value + '<...........................');
  }

  modaliteIsExiste(modalite: IModalite) {
    if (
      this.modalitesArray !== undefined &&
      this.modalitesArray.getRawValue() !== undefined &&
      this.modalitesArray.getRawValue().length > 0
    ) {
      this.modalitesArray.getRawValue().forEach(mod => {
        if (modalite.code === mod.code) {
          window.console.log('-------------------------existe-----------modalite-------------');
          this.getMessage('myKey2', 'error', "erreur d'ajout", 'Ce code existe déjà pour la modalité ' + mod.libelle);
          this.isSaving = true;
        }
      });
    }
  }

  indicateurIsExiste() {
    if (this.indicateurs !== undefined && this.indicateurs !== null) {
      this.indicateurs.forEach(indicateur => {
        if (this.editForm.get('code').value === indicateur.code) {
          window.console.log('-------------------------existe----------indicateur--------------');
          this.getMessage('myKey1', 'error', "erreur d'ajout", "Ce code existe déjà pour l'indicateur " + indicateur.libelle);
          this.isSaving = true;
        }
      });
    }
  }

  sousIndicateurChange() {
    window.console.log('--------------------------------------------->');
    window.console.log(this.editForm.get('sousIndicateur').value);
    window.console.log('<---------------------------------------------');
  }

  add() {
    if (this.editForm.get('addSousIndicateur').value) {
      this.ajouter();
    } else {
      this.modalites = [];
    }
  }

  // message(typeMessage: string, entete: string, contenu: string) {
  //   this.messageService.add({ key: 'tc', severity: typeMessage, summary: entete, detail: contenu });
  // }

  showMessage(sever: string, sum: string, det: string) {
    this.messageService.add({
      severity: sever,
      summary: sum,
      detail: det
    });
  }
}
