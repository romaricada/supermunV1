import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ICommune } from 'app/shared/model/commune.model';
import { IExercice } from 'app/shared/model/exercice.model';
import { PortailService } from 'app/portail/portailServices/portail.service';
import { IDomaine } from 'app/shared/model/domaine.model';
import { IIndicateur, Indicateur } from 'app/shared/model/indicateur.model';
import { MessageService } from 'primeng/api';
import { ITypeIndicateur } from 'app/shared/model/type-indicateur.model';
import { saveAs } from 'file-saver';
import { Extension } from 'app/shared/model/extension.enum';
import { ActivatedRoute } from '@angular/router';
import { ExerciceService } from 'app/admin/exercice/exercice.service';
import { CommuneService } from 'app/admin/commune/commune.service';

@Component({
  selector: 'jhi-donnee',
  templateUrl: './donnee.component.html',
  styleUrls: ['./donnee.component.scss']
})
export class DonneeComponent implements OnInit {
  communes: ICommune[] = [];
  selectedCommune: ICommune;
  nombreEtoile: number[] = [1, 2, 3, 4, 5];

  minYear: number;
  maxYear: number;
  year: number;
  year1: number;
  year2: number;

  exercices: IExercice[] = [];
  selectedExerce: IExercice;
  selectedExerce1: IExercice;
  selectedExerce2: IExercice;

  indicateurs: IIndicateur[] = [];
  selectIndicate: IIndicateur;

  typeIndicateurs: ITypeIndicateur[] = [];
  selectTypeIndicateur: ITypeIndicateur;

  domaines: IDomaine[] = [];
  integer: number[] = [];
  annees: string[] = [];
  data: any;
  onVisualisation: boolean;
  onDonnee: boolean;
  isChartLine: boolean;
  isCharging: boolean;
  isCharging1: boolean;
  chartImg: any;
  isExcel: boolean;
  isCsv: boolean;

  public chartOptions = {
    responsive: false,
    maintainAspectRatio: false,
    legend: {
      display: false
    },
    scales: {
      yAxes: [
        {
          ticks: {
            stepSize: 2,
            beginAtZero: true
          }
        }
      ]
    }
  };

  constructor(
    protected portailService: PortailService,
    protected messageService: MessageService,
    protected activatedRoute: ActivatedRoute,
    protected exerciceService: ExerciceService,
    protected communeService: CommuneService
  ) {}

  loadCommunes(idExercice: number, typReturn: string) {
    this.portailService.findAllCommune(idExercice, typReturn).subscribe((res: HttpResponse<ICommune[]>) => {
      this.communes = res.body;
      if (typReturn === 'all') {
        this.selectedCommune = this.communes[0];
        if (this.exercices.length > 0) {
          this.selectedExerce1 = this.exercices[0];
          this.year1 = this.selectedExerce1.annee;
          this.selectedExerce2 = this.exercices[this.exercices.length - 1];
          this.year2 = this.selectedExerce2.annee;
          this.getIndicateurByInterval();
        }
      }
    });
  }

  loadYears() {
    this.portailService.findAllExercices().subscribe((res: HttpResponse<IExercice[]>) => {
      this.exercices = res.body;
      if (this.exercices.length > 0) {
        this.minYear = this.exercices[0].annee;
        this.maxYear = this.exercices[this.exercices.length - 1].annee;
      }
      /* if (this.exercices.length > 0) {
        this.selectedExerce = this.exercices[this.exercices.length - 1];
        this.loadTypeIndicateurByCommune();
      } */
    });
  }

  onExerciceChange() {
    if (this.year) {
      this.selectedExerce = this.exercices.find(value1 => value1.annee === this.year);
      if (this.selectedExerce) {
        this.loadCommunes(this.selectedExerce.id, 'byAnnee');
      }
    }
  }

  loadTypeIndicateurByCommune() {
    if (this.selectedExerce && this.selectedCommune) {
      this.isCharging1 = true;
      this.portailService
        .findAllGroupeDomaineByCommuneAnExercice(this.selectedCommune.id, this.selectedExerce.id)
        .subscribe((res: HttpResponse<ITypeIndicateur[]>) => {
          this.typeIndicateurs = res.body;
          if (this.typeIndicateurs.length > 0) {
            this.selectTypeIndicateur = this.typeIndicateurs[0];
            //    window.console.log(this.selectTypeIndicateur);
          }
          this.isCharging1 = false;
        });
    }
  }

  ngOnInit() {
    this.selectedCommune = null;
    this.selectedExerce = null;
    this.onVisualisation = true;
    this.onDonnee = false;
    this.isChartLine = true;
    this.selectedExerce1 = null;
    this.selectedExerce2 = null;
    this.selectTypeIndicateur = null;
    this.selectIndicate = null;
    this.isCharging = false;
    this.isCharging1 = false;
    this.isExcel = false;
    this.isCsv = false;
    this.year1 = null;
    this.year2 = null;
    this.year = null;
    this.loadYears();
    // this.loadCommunes();

    const idExercice = this.activatedRoute.snapshot.queryParams.idY;
    const idCommune = this.activatedRoute.snapshot.queryParams.idC;
    if (idExercice !== undefined && idCommune !== undefined) {
      this.exerciceService.find(parseInt(idExercice, 10)).subscribe((res: HttpResponse<IExercice>) => {
        this.selectedExerce = res.body;
        this.year = this.selectedExerce.annee;
        this.portailService.findAllCommune(this.selectedExerce.id, 'byAnnee').subscribe((res1: HttpResponse<ICommune[]>) => {
          this.communes = res1.body;
          this.selectedCommune = this.communes.find(value => value.id === parseInt(idCommune, 10));
          // this.selectedCommune = this.communes[0];
          this.loadTypeIndicateurByCommune();
        });
      });
    }
  }

  // methode pour tracer le graphe
  grapgOfByIndicateur(event, indicateur: IIndicateur) {
    let indicat: Indicateur = event.data;
    if (indicateur !== null) {
      indicat = indicateur;
    }
    this.selectIndicate = indicat;
    if (indicat !== null) {
      const indicaScore: any[] = [];

      this.selectIndicate.scores.forEach(score => {
        if (score.score) {
          indicaScore.push(this.truncateNumber(score.score));
        } else {
          indicaScore.push(score.score);
        }
      });
      this.data = {
        labels: this.annees,
        datasets: [
          {
            label: '',
            data: indicaScore,
            fill: false,
            borderColor: '#4bc0c0',
            backgroundColor: '#9CCC65'
          }
        ]
      };
    }
  }

  /* loadScoreByDomaineBetweenAnnee() {
    if (this.selectedCommune !== null && this.selectedExerce !== null) {
      this.portailService
        .scoreByAnneeAndDomaine(this.selectedCommune.id, this.selectedExerce.id)
        .subscribe((res: HttpResponse<IDomaine[]>) => {
          this.domaines = res.body;
          this.getIndicateurByInterval();
        });
    }
  } */

  changeToVisuel() {
    if (this.onDonnee) {
      this.onDonnee = false;
      this.onVisualisation = true;
      this.onExerciceChange();
    }
  }

  changeToDonnee() {
    if (this.onVisualisation) {
      this.onVisualisation = false;
      this.onDonnee = true;
      this.loadCommunes(0, 'all');
    }
  }

  getAnnee1(): number {
    if (this.year1) {
      this.selectedExerce1 = this.exercices.find(value1 => value1.annee === this.year1);
      if (this.selectedExerce1 !== null) {
        return this.selectedExerce1.id;
      } else {
        return 0;
      }
    }
  }

  getAnnee2(): number {
    if (this.year2) {
      this.selectedExerce2 = this.exercices.find(value1 => value1.annee === this.year2);
      if (this.selectedExerce2 !== null) {
        return this.selectedExerce2.id;
      } else {
        return 0;
      }
    }
  }

  changerGraphe(chartType: string) {
    switch (chartType) {
      case 'LINE':
        this.isChartLine = true;
        break;
      case 'BAR':
        this.isChartLine = false;
        break;
    }
  }

  exportExcelScore(val: boolean) {
    let extension: Extension;
    if (val === true) {
      extension = Extension.XLSX;
      this.isExcel = true;
    } else {
      extension = Extension.CSV;
      this.isCsv = true;
    }
    if (this.selectedCommune && this.selectedExerce1 && this.selectedExerce2) {
      this.portailService
        .exportExcelScore(this.selectedCommune.id, this.selectedExerce1.id, this.selectedExerce2.id, extension)
        .subscribe(value => {
          const filename = value.headers.get('filename');
          this.isExcel = false;
          this.isCsv = false;
          this.portailService.saveFile(value.body, filename, 'application/vnd.ms.excel');
        });
    }
  }

  getIndicateurByInterval() {
    this.isCharging = true;
    this.portailService
      .scoreCommuneAndByIntervalAnnee(this.selectedCommune.id, this.getAnnee1(), this.getAnnee2())
      .subscribe((res: HttpResponse<IIndicateur[]>) => {
        this.indicateurs = res.body;
        if (this.indicateurs.length > 0) {
          this.annees = [];
          this.indicateurs[0].scores.forEach(value => this.annees.push(value.annee));
          this.annees.sort();
        }
        this.isCharging = false;
      });
  }

  print(chart) {
    this.chartImg = chart.getBase64Image();
    saveAs(this.chartImg, 'graphe');
  }

  showPopup(tooltip, greeting: string) {
    if (tooltip.isOpen()) {
      tooltip.close();
    } else {
      tooltip.open({ greeting });
    }
  }

  truncateNumber(a: number) {
    return Math.floor(a * 1000) / 1000;
  }
}
