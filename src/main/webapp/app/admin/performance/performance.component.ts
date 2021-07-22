import { HttpErrorResponse, HttpHeaders, HttpResponse } from '@angular/common/http';
import { Component, OnDestroy, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { CommuneService } from 'app/admin/commune/commune.service';
import { DomaineService } from 'app/admin/domaine/domaine.service';
import { ExerciceService } from 'app/admin/exercice/exercice.service';
import { IndicateurService } from 'app/admin/indicateur/indicateur.service';
import { ImportExportService } from 'app/admin/performance/import-export.service';
import { RegionService } from 'app/admin/region/region.service';
import { TypeIndicateurService } from 'app/admin/type-indicateur/type-indicateur.service';
import { AccountService } from 'app/core/auth/account.service';
// import { ITEMS_PER_PAGE } from 'app/shared/constants/pagination.constants';
import { CommuneCopy } from 'app/shared/model/commune.copy';
import { Commune, ICommune } from 'app/shared/model/commune.model';
import { IDomaine } from 'app/shared/model/domaine.model';
import { IExercice } from 'app/shared/model/exercice.model';
import { Extension } from 'app/shared/model/extension.enum';
import { FileType } from 'app/shared/model/file-type.enum';
import { IIndicateur } from 'app/shared/model/indicateur.model';
import { IPerformance, TypePrformance } from 'app/shared/model/performance.model';
import { IProvince, Province } from 'app/shared/model/province.model';
import { IRegion, Region } from 'app/shared/model/region.model';
import { ITypeIndicateur } from 'app/shared/model/type-indicateur.model';
import { saveAs } from 'file-saver';
import { JhiEventManager, JhiParseLinks } from 'ng-jhipster';
import { ConfirmationService, MenuItem, MessageService } from 'primeng/api';
import { Subscription } from 'rxjs';
import { PerformanceService } from './performance.service';
import { ValeurModaliteService } from 'app/admin/valeur-modalite/valeur-modalite.service';

@Component({
  selector: 'jhi-performance',
  templateUrl: './performance.component.html',
  styleUrls: ['./performance.component.scss']
})
export class PerformanceComponent implements OnInit, OnDestroy {
  currentAccount: any;
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
  regions: IRegion[];
  region: IRegion;
  provinces: IProvince[];
  province: IProvince;
  communes: ICommune[];
  commune: ICommune;
  exercices: IExercice[];
  exercicesImport: IExercice[];
  exercice: IExercice;
  score: number;
  typeIndicateurs: ITypeIndicateur[];
  typeIndicateur: ITypeIndicateur;

  fichier: File;
  indicateurs: CommuneCopy[];
  indics: IIndicateur[];
  entetes: String[];
  domaines: IDomaine[];
  perfers: any[];
  boolValue: String[];
  items: MenuItem[];
  ext: Extension;
  fileType: FileType;
  checkIfValidated: boolean;
  isCharging: boolean;
  deleting: boolean;

  constructor(
    protected performanceService: PerformanceService,
    protected parseLinks: JhiParseLinks,
    protected accountService: AccountService,
    protected activatedRoute: ActivatedRoute,
    protected router: Router,
    protected eventManager: JhiEventManager,
    protected regionService: RegionService,
    protected communeService: CommuneService,
    protected exerciceService: ExerciceService,
    protected typeIndicateurService: TypeIndicateurService,
    protected messageService: MessageService,
    protected indicateurService: IndicateurService,
    protected domaineService: DomaineService,
    protected importExportService: ImportExportService,
    protected confirmationService: ConfirmationService,
    protected valeurModaliteService: ValeurModaliteService
  ) {
    this.itemsPerPage = 15;
    this.routeData = this.activatedRoute.data.subscribe(data => {
      this.page = data.pagingParams.page;
      this.previousPage = data.pagingParams.page;
      this.reverse = data.pagingParams.ascending;
      this.predicate = data.pagingParams.predicate;
    });
  }

  loadPage(page: number) {
    if (page !== this.previousPage) {
      this.previousPage = page;
      this.transition();
    }
  }

  transition() {
    this.router.navigate(['/admin/performance'], {
      queryParams: {
        page: this.page,
        size: this.itemsPerPage,
        sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
      }
    });
    this.findAllDataOfCommune(this.exercice.id, this.typeIndicateur.id);
  }

  clear() {
    this.page = 0;
    this.router.navigate([
      '/admin/performance',
      {
        page: this.page,
        sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
      }
    ]);
    this.findAllDataOfCommune(this.exercice.id, this.typeIndicateur.id);
  }

  init() {
    this.eventManager.broadcast({
      name: 'endpointChanged',
      content: 'User switch to portail !'
    });
    this.region = new Region();
    this.regions = [];
    this.province = new Province();
    this.provinces = [];
    this.communes = [];
    this.commune = new Commune();
    this.score = 0;
    this.exercices = null;
    this.exercices = [];
    this.exercicesImport = [];
    this.typeIndicateur = null;
    this.indicateurs = [];
    this.entetes = [];
    this.boolValue = ['oui', 'non'];
    this.isCharging = false;
    this.deleting = false;

    this.items = [
      {
        label: 'Exporter les données',
        icon: 'pi pi-fw pi-file',
        items: [
          {
            label: 'En excel(.xlsx)',
            icon: 'pi pi-fw pi-file-excel',
            command: () => {
              this.fileType = FileType.DATA;
              this.ext = Extension.XLSX;
              this.exportData();
            }
          },
          { separator: true },
          {
            label: 'En CSV(.csv)',
            icon: 'pi pi-fw pi-file-o',
            command: () => {
              this.fileType = FileType.DATA;
              this.ext = Extension.CSV;
              this.exportData();
            }
          }
        ]
      }
    ];
  }

  ngOnInit() {
    this.init();
    this.perfers = [];
    this.accountService.identity().subscribe(account => {
      this.currentAccount = account;
    });
    this.regionService.query().subscribe(
      (res: HttpResponse<IRegion[]>) => {
        this.regions = res.body;
      },
      (resError: HttpErrorResponse) => this.error(resError.message)
    );
    this.exerciceService.query().subscribe(
      (res: HttpResponse<IExercice[]>) => {
        this.exercices = res.body;
      },
      (err: HttpErrorResponse) => this.error(err.message)
    );

    this.typeIndicateurService.query().subscribe(
      (res: HttpResponse<ITypeIndicateur[]>) => {
        this.typeIndicateurs = res.body;
      },
      (err: HttpErrorResponse) => this.error(err.message)
    );

    this.indicateurService.query().subscribe(
      (res: HttpResponse<IIndicateur[]>) => {
        this.indics = res.body;
      },
      (err: HttpErrorResponse) => this.error(err.message)
    );

    this.domaineService.query().subscribe(
      (res: HttpResponse<IDomaine[]>) => {
        this.domaines = res.body;
      },
      (err: HttpErrorResponse) => this.error(err.message)
    );
    this.registerChangeInPerformances();
  }

  ngOnDestroy() {
    this.eventManager.destroy(this.eventSubscriber);
  }

  trackId(index: number, item: IPerformance) {
    return item.id;
  }

  registerChangeInPerformances() {
    this.eventSubscriber = this.eventManager.subscribe('performanceListModification', () =>
      this.findAllDataOfCommune(this.exercice.id, this.typeIndicateur.id)
    );
  }

  sort() {
    const result = [this.predicate + ',' + (this.reverse ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  setFileData(event) {
    if (event.target.files.length > 0) {
      this.fichier = event.target.files[0];
    }
  }

  findAllDataOfCommune(idAnnee: number, idtype: number) {
    if (idAnnee !== null && idtype !== null) {
      this.isCharging = true;
      this.perfers = [];
      this.performanceService
        .findAllDataOfCommune(idAnnee, idtype, this.page - 1, this.itemsPerPage, this.sort())
        .subscribe((res: HttpResponse<CommuneCopy[]>) => {
          this.paginatePerformance(res.body, res.headers);
          this.performanceService.checkValidateAllCommuneData(idAnnee, idtype).subscribe(res1 => {
            this.checkIfValidated = res1.body;
            this.isCharging = false;
          });
        });
    }
  }

  protected paginatePerformance(data: CommuneCopy[], headers: HttpHeaders) {
    this.links = this.parseLinks.parse(headers.get('link'));
    this.totalItems = parseInt(headers.get('X-Total-Count'), 10);
    if (data.length > 0) {
      this.indicateurs = data;
      this.createHeader(data[0]);
      this.createDynamicCol();
    } else {
      this.showMessage('warn', 'Avertissement', 'Aucune donnée trouvée !');
    }
  }

  createHeader(data: CommuneCopy) {
    this.entetes = [];
    data.valeurModalites.forEach(value => {
      if (!value.indicateurId) {
        this.entetes.push(value.modalite.libelle);
      } else {
        const ind = this.indics.find(value1 => value1.id === value.indicateurId);
        this.entetes.push(ind.libelle);
      }
    });
    data.performances.forEach(value => {
      if (value !== null && value.typePerformance === TypePrformance.INDICATEUR) {
        this.entetes.push(`Points-${value.indicateurLibelle}`);
      } else if (value !== null && value.typePerformance === TypePrformance.DOMAINE) {
        const domaine = this.domaines.find(value1 => value1.id === value.domaineId);
        this.entetes.push(`Points-${domaine.libelle}`);
        this.entetes.push(`Nombre d'étoiles-${domaine.libelle}`);
      } else if (value !== null && value.typePerformance === TypePrformance.COMMUNE) {
        this.entetes.push('Performance globale');
        this.entetes.push("Nombre d'étoiles globale");
      }
    });
  }

  createDynamicCol() {
    this.perfers = [];
    this.indicateurs.forEach(value => {
      const cols = [];
      value.valeurModalites.forEach(value1 => {
        if (value1.indicateurId) {
          cols.push({
            value: this.computeValue(value1.valeur),
            id: value1.id,
            isModalite: false,
            typeObjet: 'valeurModalite',
            unite: undefined,
            communeId: value.id
          });
        } else {
          cols.push({
            value: value1.valeur,
            id: value1.id,
            isModalite: true,
            typeObjet: 'valeurModalite',
            unite: undefined,
            comId: value.id
          });
        }
      });
      value.performances.forEach(value1 => {
        if (value1.typePerformance === TypePrformance.DOMAINE || value1.typePerformance === TypePrformance.COMMUNE) {
          cols.push({
            value: this.computeValue(value1.score),
            id: value1.id,
            isModalite: undefined,
            typeObjet: 'performance',
            unite: 'points',
            comId: value.id
          });
          cols.push({
            value: value1.nbEtoile,
            id: value1.id,
            isModalite: undefined,
            typeObjet: 'performance',
            unite: 'etoiles',
            comId: value.id
          });
        } else {
          cols.push({
            value: this.computeValue(value1.score),
            id: value1.id,
            isModalite: undefined,
            typeObjet: 'performance',
            unite: 'points',
            comId: value.id
          });
        }
      });
      let row = {};
      if (value.validationCommune) {
        row = { libelle: value.libelle, id: value.id, validated: value.validationCommune.validated, cols };
      } else {
        row = { libelle: value.libelle, id: value.id, validated: false, cols };
      }
      this.perfers.push(row);
    });
  }

  saveFile(data: any, filename?: string, type?: string) {
    const blob = new Blob([data], { type: `${type}; charset=utf-8` });
    saveAs(blob, filename);
  }

  exportData() {
    this.importExportService.exportData(this.typeIndicateur.id, this.ext, this.fileType, this.exercice.id).subscribe(
      value => {
        const filename = value.headers.get('filename');
        this.saveFile(value.body, filename, value.headers.get('content-type'));
      },
      (error1: HttpErrorResponse) => {
        if (error1.status === 404) {
          this.showMessage('warn', 'Avertissement', 'Aucune donnée trouvée à exporter !');
        }
      }
    );
  }

  showMessage(severity: string, summary: string, detail: string) {
    this.messageService.add({ severity, summary, detail });
  }

  computeValue(score: any): number {
    let supp = 0;
    if (score) {
      const val = score.toString().split('.');
      if (val.length > 1) {
        if (val[1].length > 2) {
          const value = val[0] + '.' + val[1].substr(0, 2);
          supp = parseFloat(value.toString());
        } else {
          supp = parseFloat(val[0]);
        }
      } else {
        supp = parseFloat(val[0]);
      }
    }
    return supp;
  }

  saveCommuneData(perf: any) {
    this.performanceService.validateCommuneData(perf, this.exercice.id, this.typeIndicateur.id).subscribe(
      (res: HttpResponse<boolean>) => {
        if (res.body) {
          perf.validated = res.body;
          this.findAllDataOfCommune(this.exercice.id, this.typeIndicateur.id);
          this.showMessage('success', 'Information', 'Validation et publication effectuées avec succès !');
        } else {
          this.showMessage('warn', 'Avertissement', "Erreur lors de l'importation des données !");
        }
      },
      () => this.showMessage('error', 'Avertissement', 'Une erreur a survenue !')
    );
  }

  saveAllCommuneData() {
    this.confirmationService.confirm({
      header: 'Confirmation',
      message: 'Cette action va publier les données sur le portail, Voulez-vous poursuivre ?',
      accept: () => {
        this.performanceService.validateAllCommuneData(this.exercice.id, this.typeIndicateur.id).subscribe(
          (res: HttpResponse<boolean>) => {
            if (res.body) {
              this.findAllDataOfCommune(this.exercice.id, this.typeIndicateur.id);
              this.showMessage('success', 'Information', 'Validation efectuée avec succès !');
            } else {
              this.showMessage('warn', 'Avertissement', "Erreur lors de l'importation des données !");
            }
          },
          () => this.showMessage('error', 'Avertissement', 'Une erreur a survenue !')
        );
      },
      reject: () => this.showMessage('warn', 'Information', 'Merci de bien vouloir vous rassurez !')
    });
  }

  deleteElement(anneeID: number, type: ITypeIndicateur) {
    this.confirmationService.confirm({
      header: 'Confirmation',
      message: 'Etes-vous sûr de vouloir vider les données ?',
      accept: () => {
        if (anneeID === undefined || type === undefined) {
          return;
        } else {
          this.deleting = true;
          this.performanceService.viderPerformanceByAnneeAndTypIndic(anneeID, type.id).subscribe(
            () => {
              this.deleting = false;
              this.showMessage('success', 'SUPPRESSION', 'Suppression effectuée avec succès !');
              this.findAllDataOfCommune(anneeID, type.id);
            },
            () => {
              this.deleting = false;
              this.showMessage('error', 'SUPPRESSION', 'Echec de la suppression !');
            }
          );
        }
      }
    });
  }

  deValiderDonnee(anneeID: number, type: ITypeIndicateur) {
    this.confirmationService.confirm({
      header: 'Confirmation',
      message: 'Etes-vous sûr de vouloir devalider les données ?',
      accept: () => {
        if (anneeID === undefined || type === undefined) {
          return;
        } else {
          this.deleting = true;
          this.performanceService.devaliderDonnees(anneeID, type.id).subscribe(
            () => {
              this.deleting = false;
              this.showMessage('success', 'DEVALIDATION', 'Opération effectué avec succès !');
              this.findAllDataOfCommune(anneeID, type.id);
            },
            () => {
              this.deleting = false;
              this.showMessage('error', 'DEVALIDATION', "Echec de l'opération !");
            }
          );
        }
      }
    });
  }
}
