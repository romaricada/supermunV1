import { HttpResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { CommuneService } from 'app/admin/commune/commune.service';
import { DomaineService } from 'app/admin/domaine/domaine.service';
import { ExerciceService } from 'app/admin/exercice/exercice.service';
import { IndicateurService } from 'app/admin/indicateur/indicateur.service';
import { TypeIndicateurService } from 'app/admin/type-indicateur/type-indicateur.service';
import { ICommune } from 'app/shared/model/commune.model';
import { IExercice } from 'app/shared/model/exercice.model';
import { IIndicateur } from 'app/shared/model/indicateur.model';
import { ITypeIndicateur } from 'app/shared/model/type-indicateur.model';
import { PortailService } from '../portailServices/portail.service';

@Component({
  selector: 'jhi-poster',
  templateUrl: './poster.component.html',
  styleUrls: ['./poster.component.scss']
})
export class PosterComponent implements OnInit {
  domaines: ITypeIndicateur[];
  selectedYear;
  selectedCommune;
  selectedTypeIndicateur;
  performances: ITypeIndicateur;
  communes: ICommune[];
  years: IExercice[];
  indicateurs: IIndicateur[];
  typeIndicateurs: ITypeIndicateur[];
  value: number;
  options: { floor: number; ceil: number; showTicks: boolean; getLegend: (value: number) => string };
  posters: { id: number; libelle: string }[];
  pourt = 76;
  isCharging: boolean;
  minYear: number;
  maxYear: number;
  year: number;

  constructor(
    protected communeService: CommuneService,
    protected portailService: PortailService,
    protected exerciceService: ExerciceService,
    protected indicateurService: IndicateurService,
    protected domaineService: DomaineService,
    protected typeIndicateurService: TypeIndicateurService,
    protected activatedRoute: ActivatedRoute
  ) {}

  ngOnInit() {
    this.isCharging = false;
    this.pourt = 50;
    this.posters = [{ id: 1, libelle: 'Poster N°1' }, { id: 2, libelle: 'Poster N°2' }];
    this.value = 5;
    this.options = {
      floor: 0,
      ceil: 10,
      showTicks: true,
      getLegend(val: number) {
        return '<b>T</b>' + val;
      }
    };
    this.year = null;

    /* this.activatedRoute.data.subscribe(({ids}) => {
      const anneId = null;
      const communeId = null;
      if (anneId !== undefined && communeId !== undefined) {
        this.exerciceService.find(anneId).subscribe((res: HttpResponse<IExercice>) => {
          this.selectedYear = res.body;
        });
        this.communeService.find(communeId).subscribe((res: HttpResponse<ICommune>) => {
          this.selectedCommune = res.body;
        });
      }
    }); */

    this.loadTypeIndicateurs();
    const idExercice = this.activatedRoute.snapshot.queryParams.idY;
    const idCommune = this.activatedRoute.snapshot.queryParams.idC;
    this.loadYears(idExercice, idCommune);
    // this.loadCommunes(idCommune);
  }

  onAnneeChange() {
    if (this.year) {
      this.selectedYear = this.years.find(value1 => value1.annee === this.year);
      if (this.selectedYear) {
        this.loadCommunes(undefined, this.selectedYear.id, 'byAnnee');
      }
    }
  }

  loadPerformance() {
    this.performances = null;
    if (this.selectedYear && this.selectedTypeIndicateur && this.selectedCommune) {
      this.isCharging = true;
    }
    if (this.selectedCommune && this.selectedTypeIndicateur && this.selectedYear)
      this.portailService
        .findAllGroupeDomaineByCommuneAnExercice(this.selectedCommune.id, this.selectedYear.id)
        .subscribe((res: HttpResponse<ITypeIndicateur[]>) => {
          if (res.body) this.performances = res.body.filter(perform => perform.id === this.selectedTypeIndicateur.id)[0];
          this.isCharging = false;
          // window.console.log(this.performances);
        });
  }

  selectACommune(idCommune, communes: ICommune[]) {
    this.selectedCommune =
      communes.filter(comm => comm.id === parseInt(idCommune, 10)) &&
      communes.filter(comm => comm.id === parseInt(idCommune, 10)).length > 0
        ? communes.filter(comm => comm.id === parseInt(idCommune, 10))[0]
        : null;
  }

  loadCommunes(idCommune, anneeId: number, typeReturn: string) {
    this.portailService.findAllCommune(anneeId, typeReturn).subscribe((res: HttpResponse<ICommune[]>) => {
      this.communes = res.body;
      if (idCommune) {
        this.selectACommune(idCommune, this.communes);
      }
    });
  }

  loadYears(idExercice, idCommune) {
    this.portailService.findAllExercices().subscribe((res: HttpResponse<IExercice[]>) => {
      this.years = res.body;
      this.minYear = this.years[0].annee;
      this.maxYear = this.years[this.years.length - 1].annee;
      this.selectedYear =
        res.body.filter(year => year.id === parseInt(idExercice, 10)) &&
        res.body.filter(year => year.id === parseInt(idExercice, 10)).length > 0
          ? res.body.filter(year => year.id === parseInt(idExercice, 10))[0]
          : null;
      if (this.selectedYear) {
        this.year = this.selectedYear.annee;
        this.loadCommunes(idCommune, this.selectedYear.id, 'byAnnee');
      }
    });
  }

  loadTypeIndicateurs() {
    this.portailService.findAllTypeIndicateur().subscribe((res: HttpResponse<ITypeIndicateur[]>) => (this.typeIndicateurs = res.body));
  }

  valeurEnPourcentage(valeur: any, maxVal: any) {
    return valeur && maxVal ? Math.round((valeur * 100) / maxVal) : 0;
  }

  print(): void {
    window.print();
  }
}
