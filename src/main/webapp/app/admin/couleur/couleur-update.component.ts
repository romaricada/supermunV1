import { Component, OnInit } from '@angular/core';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { CommuneService } from 'app/admin/commune/commune.service';
import { ExerciceService } from 'app/admin/exercice/exercice.service';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiAlertService, JhiEventManager } from 'ng-jhipster';
import { ICouleur, Couleur } from 'app/shared/model/couleur.model';
import { CouleurService } from './couleur.service';
import { ICommune } from 'app/shared/model/commune.model';
import { IExercice } from 'app/shared/model/exercice.model';
import { Domaine, IDomaine } from 'app/shared/model/domaine.model';
import { DomaineService } from 'app/admin/domaine/domaine.service';
import { IIndicateur } from 'app/shared/model/indicateur.model';
import { IndicateurService } from 'app/admin/indicateur/indicateur.service';
import { MessageService } from 'primeng/api';

@Component({
  selector: 'jhi-couleur-update',
  templateUrl: './couleur-update.component.html',
  providers: [MessageService]
})
export class CouleurUpdateComponent implements OnInit {
  isSaving: boolean;

  communes: ICommune[];
  couleur: ICouleur;
  couleurs: ICouleur[];
  domaines: IDomaine[];
  domaine: IDomaine;
  indicateurs: IIndicateur[];
  indicateursTMP: IIndicateur[];
  couleurTab: ICouleur[];

  exercices: IExercice[];

  editForm = this.fb.group({
    id: [],
    couleur: [null, [Validators.required]],
    idPerformance: [],
    communeId: [],
    exerciceId: []
  });

  constructor(
    protected jhiAlertService: JhiAlertService,
    protected couleurService: CouleurService,
    protected communeService: CommuneService,
    protected exerciceService: ExerciceService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder,
    protected domaineService: DomaineService,
    protected indicateurService: IndicateurService,
    private messageService: MessageService,
    private eventManager: JhiEventManager
  ) {}

  ngOnInit() {
    this.eventManager.broadcast({
      name: 'endpointChanged',
      content: 'User switch to portail !'
    });

    this.couleur = new Couleur();
    this.couleur.couleur = ' #127327';
    this.couleurs = [];
    this.isSaving = false;
    this.domaine = new Domaine();
    this.domaines = [];
    this.indicateurs = [];
    this.couleurTab = [];
    this.activatedRoute.data.subscribe(({ couleur }) => {
      this.updateForm(couleur);
    });
    this.loadIndicateur();
    this.domaineService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<IDomaine[]>) => mayBeOk.ok),
        map((response: HttpResponse<IDomaine[]>) => response.body)
      )
      .subscribe((res: IDomaine[]) => (this.domaines = res), (res: HttpErrorResponse) => this.onError(res.message));

    this.communeService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<ICommune[]>) => mayBeOk.ok),
        map((response: HttpResponse<ICommune[]>) => response.body)
      )
      .subscribe((res: ICommune[]) => (this.communes = res), (res: HttpErrorResponse) => this.onError(res.message));
    this.exerciceService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<IExercice[]>) => mayBeOk.ok),
        map((response: HttpResponse<IExercice[]>) => response.body)
      )
      .subscribe((res: IExercice[]) => (this.exercices = res), (res: HttpErrorResponse) => this.onError(res.message));
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

  updateForm(couleur: ICouleur) {
    this.editForm.patchValue({
      id: couleur.id,
      couleur: couleur.couleur,
      idPerformance: couleur.idPerformance,
      communeId: couleur.communeId,
      exerciceId: couleur.exerciceId
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const couleur = this.createFromForm();
    if (couleur.id !== undefined) {
      this.subscribeToSaveResponse(this.couleurService.update(couleur));
    } else {
      this.subscribeToSaveResponse(this.couleurService.create(couleur));
    }
  }

  private createFromForm(): ICouleur {
    return {
      ...new Couleur(),
      id: this.editForm.get(['id']).value,
      couleur: this.editForm.get(['couleur']).value,
      idPerformance: this.editForm.get(['idPerformance']).value,
      communeId: this.editForm.get(['communeId']).value,
      exerciceId: this.editForm.get(['exerciceId']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICouleur>>) {
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

  trackCommuneById(index: number, item: ICommune) {
    return item.id;
  }

  trackExerciceById(index: number, item: IExercice) {
    return item.id;
  }

  domaineChange() {
    if (this.domaine !== null && this.domaine.id !== undefined) {
      this.indicateursTMP.filter(i => i.domaineId === this.domaine.id);
    } else {
      this.indicateurs = [];
    }
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
            this.couleurTab.push(this.couleur);
            this.couleur = new Couleur();
          }
        });
      } else {
        this.couleurTab.push(this.couleur);
        this.couleur = new Couleur();
      }
    }
    window.console.log('..........................................' + this.couleurTab.length + '...................................');
  }

  couleurChange() {
    window.console.log('..........................................' + this.couleur.couleur + '...................................');
  }
}
