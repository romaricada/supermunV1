import { Component, OnInit } from '@angular/core';
import { IExercice } from 'app/shared/model/exercice.model';
import { ITypeIndicateur } from 'app/shared/model/type-indicateur.model';
import { IDomaine } from 'app/shared/model/domaine.model';
import { IIndicateur } from 'app/shared/model/indicateur.model';
import { PortailService } from 'app/portail/portailServices/portail.service';
import { HttpResponse } from '@angular/common/http';
import { TypeIndicateurService } from 'app/admin/type-indicateur/type-indicateur.service';
import { ICommune } from 'app/shared/model/commune.model';
import { Extension } from 'app/shared/model/extension.enum';
import { saveAs } from 'file-saver';

@Component({
  selector: 'jhi-classement',
  templateUrl: './classement.component.html',
  styleUrls: ['./classement.component.scss']
})
export class ClassementComponent implements OnInit {
  communes: ICommune[] = [];
  communes1: ICommune[] = [];
  exercies: IExercice[] = [];
  seletedExercice: IExercice;

  total1: number;
  total2: number;

  typeIndicateurs: ITypeIndicateur[] = [];
  selectedTypeIndicateur: ITypeIndicateur;

  domaines: IDomaine[] = [];
  selectedDomaine: IDomaine;

  indicateurs: IIndicateur[] = [];
  indicateurs1: IIndicateur[] = [];
  indicateurs2: IIndicateur[] = [];

  commune1: ICommune;
  commune2: ICommune;

  minYear: number;
  maxYear: number;
  year: number;

  selectedIndicateur: IIndicateur;
  dataFilter: string;
  id: number;
  isCharging: boolean;
  isCharging1: boolean;
  onClassement: boolean;
  onCompare: boolean;
  data: any;

  public chartOptions = {
    responsive: false,
    maintainAspectRatio: false,
    scales: {
      yAxes: [
        {
          ticks: {
            stepSize: 2,
            beginAtZero: true
          }
        }
      ]
    },
    legend: {
      position: 'left'
    },
    plugins: {
      datalabels: {
        align: 'end',
        anchor: 'end',
        borderRadius: 4,
        backgroundColor: 'teal',
        color: 'white',
        font: {
          weight: 'bold'
        }
      }
    }
  };

  constructor(protected portailService: PortailService, protected typeIndicateurService: TypeIndicateurService) {}

  loadExercice() {
    this.portailService.findAllExercices().subscribe((res: HttpResponse<IExercice[]>) => {
      this.exercies = res.body;
      this.minYear = this.exercies[0].annee;
      this.maxYear = this.exercies[this.exercies.length - 1].annee;
      if (this.exercies.length > 0) {
        this.seletedExercice = this.exercies[this.exercies.length - 1];
        this.year = this.seletedExercice.annee;
        this.dataFilter = 'GENERAL';
        this.id = 0;
        this.classerCommune(this.dataFilter, this.id);
        if (this.seletedExercice) {
          this.loadCommune(this.seletedExercice.id, 'byAnnee');
        }
      }
    });
  }

  loadTypeIndicateur() {
    this.portailService.findAllTypeIndicateur().subscribe((res: HttpResponse<ITypeIndicateur[]>) => {
      this.typeIndicateurs = res.body;
    });
  }

  loadDomaines() {
    this.portailService.findAlDomaines().subscribe((res: HttpResponse<IDomaine[]>) => {
      if (this.selectedTypeIndicateur) {
        this.domaines = res.body.filter(value => value.typeIndicateurId === this.selectedTypeIndicateur.id);
        this.domaines.sort();
      }
    });
  }

  loadCommune(idAnnee: number, typReturn: string) {
    this.portailService.findAllCommune(idAnnee, typReturn).subscribe((res: HttpResponse<ICommune[]>) => {
      this.communes1 = res.body;
    });
  }

  loadIndicateur() {
    this.portailService.findAllIndicateur().subscribe((res: HttpResponse<IIndicateur[]>) => {
      if (this.selectedDomaine) this.indicateurs = res.body.filter(value => value.domaineId === this.selectedDomaine.id);
      this.indicateurs.sort();
    });
  }

  ngOnInit() {
    this.loadExercice();
    this.loadTypeIndicateur();
    this.selectedTypeIndicateur = null;
    this.selectedDomaine = null;
    this.selectedIndicateur = null;
    this.seletedExercice = null;
    this.total1 = 0;
    this.total2 = 0;
    this.isCharging = false;
    this.isCharging1 = false;
    this.onClassement = true;
    this.onCompare = false;
  }

  initCompareElment() {
    this.commune1 = null;
    this.commune2 = null;
    this.indicateurs2 = [];
    this.indicateurs1 = [];
    this.total1 = 0;
    this.total2 = 0;
  }

  onTypIndicateurChange() {
    this.selectedIndicateur = null;
    this.selectedDomaine = null;
    this.initCompareElment();
    if (this.selectedTypeIndicateur) {
      this.dataFilter = 'TYPEINDICATEUR';
      this.id = this.selectedTypeIndicateur.id;
      this.loadDomaines();
      this.classerCommune(this.dataFilter, this.id);
    } else {
      this.dataFilter = 'GENERAL';
      this.id = 0;
      this.classerCommune(this.dataFilter, this.id);
    }
  }

  classerCommune(dataFilter: string, id: number) {
    if (this.onClassement) {
      this.isCharging = true;
      this.communes = [];
      this.portailService.classementCommune(this.seletedExercice.id, dataFilter, id).subscribe((res: HttpResponse<ICommune[]>) => {
        this.communes = res.body;
        this.isCharging = false;
      });
    } else {
      this.communes = [];
    }
  }

  changeToClassement() {
    if (this.onCompare) {
      this.onCompare = false;
      this.onClassement = true;
      if (this.communes.length === 0) {
        this.classerCommune(this.dataFilter, this.id);
      }
    }
  }

  changeToCompare() {
    if (this.onClassement) {
      this.onClassement = false;
      this.onCompare = true;
    }
  }

  onDomaineChange() {
    this.selectedIndicateur = null;
    this.initCompareElment();
    if (this.selectedDomaine) {
      this.dataFilter = 'DOMAINE';
      this.id = this.selectedDomaine.id;
      this.loadIndicateur();
      this.classerCommune(this.dataFilter, this.id);
    }
  }

  onYearChange() {
    this.communes = [];
    this.initCompareElment();
    this.selectedTypeIndicateur = null;
    this.selectedDomaine = null;
    this.selectedIndicateur = null;
    if (this.year) {
      this.seletedExercice = this.exercies.find(value1 => value1.annee === this.year);
      if (this.seletedExercice) {
        this.dataFilter = 'GENERAL';
        this.id = 0;
        this.loadCommune(this.seletedExercice.id, 'byAnnee');
        this.classerCommune(this.dataFilter, this.id);
      }
    }
  }

  onIndicateurChange() {
    this.initCompareElment();
    if (this.selectedIndicateur) {
      this.dataFilter = 'INDICATEUR';
      this.id = this.selectedIndicateur.id;
      this.classerCommune(this.dataFilter, this.id);
    }
  }

  getIndicateurByInterval(typeCom: string) {
    switch (typeCom) {
      case 'COM1': {
        this.portailService
          .findAllIndicatorByCommune(this.seletedExercice.id, this.commune1.id, this.dataFilter, this.id)
          .subscribe((res: HttpResponse<IIndicateur[]>) => {
            this.indicateurs1 = res.body;
            this.chowCahrt();
            this.total1 = 0;
            this.indicateurs1.forEach(value => {
              this.total1 = this.total1 + value.sroreIndicateur;
            });
          });
        break;
      }
      case 'COM2': {
        this.portailService
          .findAllIndicatorByCommune(this.seletedExercice.id, this.commune2.id, this.dataFilter, this.id)
          .subscribe((res: HttpResponse<IIndicateur[]>) => {
            this.indicateurs2 = res.body;
            this.chowCahrt();
            this.total2 = 0;
            this.indicateurs2.forEach(value => {
              this.total2 = this.total1 + value.sroreIndicateur;
            });
          });
        break;
      }
    }
  }

  imprimerClassement(type: number) {
    if (this.seletedExercice !== null) {
      switch (type) {
        case 0:
          this.isCharging1 = true;
          this.portailService.imprimeListeClassement(this.seletedExercice.id, this.dataFilter, this.id).subscribe(response => {
            this.isCharging1 = false;
            window.open(URL.createObjectURL(new Blob([response], { type: 'application/pdf' })), '_blank');
          });
          break;
        case 1:
          this.exporDataClassemnt(this.seletedExercice.id, this.dataFilter, this.id, Extension.XLSX);
          break;
        case 2:
          this.exporDataClassemnt(this.seletedExercice.id, this.dataFilter, this.id, Extension.CSV);
      }
    }
  }

  chowCahrt() {
    if (this.indicateurs1.length > 0 && this.indicateurs2.length > 0) {
      this.buildChart(this.indicateurs1, this.indicateurs2);
    }
  }
  buildChart(indicateur1: IIndicateur[], indicateur2: IIndicateur[]) {
    if (indicateur1.length > 0 && indicateur2.length > 0) {
      const indicaScores1: any[] = [];
      const indicaScores2: any[] = [];
      const indiLibelle: any[] = [];

      indicateur1.forEach(indic => {
        indicaScores1.push(this.truncateNumber(indic.sroreIndicateur));
        indiLibelle.push(indic.libelle);
      });
      indicateur2.forEach(indic => {
        indicaScores2.push(this.truncateNumber(indic.sroreIndicateur));
      });
      this.data = {
        labels: indiLibelle,
        datasets: [
          {
            label: this.commune1.libelle,
            data: indicaScores1,
            fill: false,
            borderColor: '#4bc0c0',
            backgroundColor: '#9CCC65'
          },
          {
            label: this.commune2.libelle,
            data: indicaScores2,
            fill: false,
            borderColor: '#4bc0c0',
            backgroundColor: '#cf7c60'
          }
        ]
      };
    }
  }

  exporDataClassemnt(idExercice: number, dataFilter, id: number, extension: Extension) {
    this.isCharging1 = true;
    this.portailService.exportDataClasement(idExercice, dataFilter, id, extension).subscribe(value => {
      const filename = value.headers.get('filename');
      this.isCharging1 = false;
      this.saveFile(value.body, filename, 'application/vnd.ms.excel');
    });
  }

  saveFile(data: any, filename?: string, type?: string) {
    const blob = new Blob([data], { type: `${type}; charset=utf-8` });
    saveAs(blob, filename);
  }

  printChart(chart) {
    const chartImg = chart.getBase64Image();
    saveAs(chartImg, 'graphe_comparaison');
  }

  truncateNumber(a: number) {
    return Math.floor(a * 1000) / 1000;
  }
}
