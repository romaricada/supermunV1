import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Component, OnDestroy, OnInit } from '@angular/core';
import { ExerciceService } from 'app/admin/exercice/exercice.service';
import { ImportExportService } from 'app/admin/performance/import-export.service';
import { PerformanceService } from 'app/admin/performance/performance.service';
import { TypeIndicateurService } from 'app/admin/type-indicateur/type-indicateur.service';
import { IExercice } from 'app/shared/model/exercice.model';
import { Extension } from 'app/shared/model/extension.enum';
import { FileType } from 'app/shared/model/file-type.enum';
import { ITypeIndicateur } from 'app/shared/model/type-indicateur.model';
import { saveAs } from 'file-saver';
import { ConfirmationService, MenuItem, MessageService } from 'primeng/api';
import { Subscription } from 'rxjs';

@Component({
  selector: 'jhi-import-export',
  templateUrl: './import-export.component.html',
  styleUrls: ['./import-export.component.scss']
})
export class ImportExportComponent implements OnInit, OnDestroy {
  items: MenuItem[];
  exercices: IExercice[];
  exercice: IExercice;
  typeIndicateurs: ITypeIndicateur[];
  typeIndicateur: ITypeIndicateur;
  fichier: File;
  ext: Extension;
  fileType: FileType;
  display: boolean;
  action: string;
  subscription: Subscription;
  request: boolean;
  checkIfValidated: boolean;

  constructor(
    protected importExportService: ImportExportService,
    protected exerciceService: ExerciceService,
    protected typeIndicateurService: TypeIndicateurService,
    protected messageService: MessageService,
    protected performanceService: PerformanceService,
    protected confirmationService: ConfirmationService
  ) {}

  ngOnInit() {
    this.exerciceService.query().subscribe((res: HttpResponse<IExercice[]>) => {
      this.exercices = res.body;
    });

    this.typeIndicateurService.query().subscribe((res: HttpResponse<ITypeIndicateur[]>) => {
      this.typeIndicateurs = res.body;
    });

    this.items = [
      {
        label: 'Exporter un modèle',
        icon: 'pi pi-fw pi-file',
        items: [
          {
            label: 'En excel(.xlsx)',
            icon: 'pi pi-fw pi-file-excel',
            command: () => {
              this.fileType = FileType.MODEL;
              this.ext = Extension.XLSX;
              this.action = 'export';
              this.displayDialog();
            }
          },
          { separator: true },
          {
            label: 'En CSV(.csv)',
            icon: 'pi pi-fw pi-file-o',
            command: () => {
              this.fileType = FileType.MODEL;
              this.ext = Extension.CSV;
              this.action = 'export';
              this.displayDialog();
            }
          }
        ]
      },
      {
        label: 'Importer les données',
        icon: 'pi pi-fw pi-file',
        items: [
          {
            label: "D'un fichier excel(.xlsx)",
            icon: 'pi pi-fw pi-file-excel',
            command: () => {
              this.ext = Extension.XLSX;
              this.action = 'import';
              this.displayDialog();
            }
          },
          { separator: true },
          {
            label: "D'un fichierCSV(.csv)",
            icon: 'pi pi-fw pi-file-o',
            command: () => {
              this.ext = Extension.CSV;
              this.action = 'import';
              this.displayDialog();
            }
          }
        ]
      },
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
              this.action = 'export-data';
              this.displayDialog();
            }
          },
          { separator: true },
          {
            label: 'En CSV(.csv)',
            icon: 'pi pi-fw pi-file-o',
            command: () => {
              this.fileType = FileType.DATA;
              this.ext = Extension.CSV;
              this.action = 'export-data';
              this.displayDialog();
            }
          }
        ]
      }
    ];
  }

  ngOnDestroy() {
    if (this.subscription) {
      this.subscription.unsubscribe();
    }
  }

  displayDialog() {
    if (this.display) {
      this.display = false;
      this.ext = null;
      this.fileType = null;
      this.action = null;
      this.fichier = null;
      this.exercice = null;
      this.typeIndicateur = null;
    } else {
      this.display = true;
    }
  }

  saveFile(data: any, filename?: string, type?: string) {
    const blob = new Blob([data], { type: `${type}; charset=utf-8` });
    saveAs(blob, filename);
  }

  showMessage(severity: string, summary: string, detail: string) {
    this.messageService.add({ severity, summary, detail });
  }

  setFileData(event) {
    if (event.target.files.length > 0) {
      this.fichier = event.target.files[0];
    }
  }

  exportModel() {
    this.importExportService.exportData(this.typeIndicateur.id, this.ext, this.fileType, this.exercice.id).subscribe(
      value => {
        const filename = value.headers.get('filename');
        this.saveFile(value.body, filename, value.headers.get('content-type'));
      },
      (error1: HttpErrorResponse) => {
        if (error1.status === 404) {
          this.showMessage('warn', 'Avertissement', "Aucune commune sélectionnée pour l'année choisie !");
        }
      }
    );
  }

  importData(update: boolean) {
    this.request = true;
    this.importExportService.importData(this.exercice.id, this.typeIndicateur.id, this.fichier, this.ext, update).subscribe(
      () => {
        this.request = false;
        this.showMessage('success', 'Information', 'Importation effectée avec succès !');
      },
      (error1: HttpErrorResponse) => {
        this.request = false;
        if (error1.status === 403) {
          this.showMessage(
            'warn',
            'Avertissement',
            "Les performances du groupe de domaine pour l'année sélectionnée ont déjà été importées!"
          );
        } else if (error1.status === 404) {
          this.showMessage('warn', 'Avertissement', "Aucune commune sélectionnée pour l'année choisie !");
        } else {
          this.showMessage('error', 'Avertissement', "Une erreur a survenue lors de l'importation des données !");
        }
      }
    );
  }

  doRequest() {
    if (this.action === 'import') {
      if (this.checkIfValidated) {
        this.confirmationService.confirm({
          header: 'Confirmation',
          message: 'Les données correspondant à la sélection sont déjà publiées, voulez-vous faire un mise à jour ?',
          accept: () => {
            this.importData(true);
          }
        });
      } else {
        this.importData(false);
      }
    } else if (this.action === 'export') {
      this.exportModel();
    } else if (this.action === 'export-data') {
      this.exportData();
    }
  }

  onChange() {
    if (this.typeIndicateur && this.exercice) {
      this.performanceService.checkValidateAllCommuneData(this.exercice.id, this.typeIndicateur.id).subscribe(res1 => {
        this.checkIfValidated = res1.body;
      });
    }
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
}
