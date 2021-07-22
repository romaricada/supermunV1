import { HttpResponse } from '@angular/common/http';
import { AfterViewInit, Component, OnDestroy, OnInit } from '@angular/core';
import { CommuneService } from 'app/admin/commune/commune.service';
import { DomaineService } from 'app/admin/domaine/domaine.service';
import { ExerciceService } from 'app/admin/exercice/exercice.service';
import { IndicateurService } from 'app/admin/indicateur/indicateur.service';
import { TypeIndicateurService } from 'app/admin/type-indicateur/type-indicateur.service';
import { AccountService } from 'app/core/auth/account.service';
import { LoginModalService } from 'app/core/login/login-modal.service';
import { PortailService } from 'app/portail/portailServices/portail.service';
import { ICommune } from 'app/shared/model/commune.model';
import { ICouleur } from 'app/shared/model/couleur.model';
import { IDomaine } from 'app/shared/model/domaine.model';
import { IExercice } from 'app/shared/model/exercice.model';
import { IIndicateur, Indicateur } from 'app/shared/model/indicateur.model';
import { IPerformance } from 'app/shared/model/performance.model';
import { IRegion } from 'app/shared/model/region.model';
import { ITypeIndicateur } from 'app/shared/model/type-indicateur.model';
import { saveAs } from 'file-saver';
import * as Leaflet from 'leaflet';
import { JhiEventManager } from 'ng-jhipster';
import { MenuItem } from 'primeng/api';
import { Extension } from 'app/shared/model/extension.enum';
import { CounteService } from 'app/portail/counte/counte.service';
import { Counte, ICounte } from 'app/shared/model/counte.model';

@Component({
  selector: 'jhi-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit, OnDestroy, AfterViewInit {
  mapCart: any;
  communes: any;
  years: any[];
  minYear: number;
  maxYear: number;
  year: number;
  communes5: ICommune[];
  indicateurs: any[];
  indicateurTemps: any[];
  domaines: IDomaine[];
  typeIndicateurs: ITypeIndicateur[] = [];
  typeIndic: ITypeIndicateur;
  chargerCouleur: boolean;

  performance = { value: 10 };
  geojson: any;
  prevLayerClicked = null;
  legend = new Leaflet.Control({ position: 'bottomright' });
  id1: number;
  id2: number;

  carteData: any;
  selectedYear: IExercice;
  selectedIndicateur: IIndicateur;
  selectedCommune: ICommune;
  communePerformance: IPerformance;
  legendCouleurs: ICouleur[];
  options: { floor: number; ceil: number; showTicks: boolean; getLegend: (value: number) => string };
  value: number;
  regions: IRegion[];
  isDowlodingAll: boolean;
  isDowlodingSelected: boolean;
  items: MenuItem[];
  items2: MenuItem[];
  colors: ICouleur[] = [];

  data: any;
  data1: any;
  myIndicateurs: IIndicateur[] = [];
  annees: string[] = [];

  allPerformances: IPerformance[] = [];

  counte: ICounte;
  countes: ICounte[] = [];

  public chartOptions = {
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
    private accountService: AccountService,
    private loginModalService: LoginModalService,
    private eventManager: JhiEventManager,
    protected communeService: CommuneService,
    protected portailService: PortailService,
    protected exerciceService: ExerciceService,
    protected indicateurService: IndicateurService,
    protected domaineService: DomaineService,
    protected typeIndicateurService: TypeIndicateurService,
    protected counteService: CounteService
  ) {}

  ngOnInit() {
    this.chargerCouleur = false;
    this.eventManager.broadcast({
      name: 'endpointChanged',
      content: 'User switch to portail !'
    });
    this.carteData = null;
    this.prevLayerClicked = null;
    this.selectedCommune = null;
    this.selectedIndicateur = null;
    this.selectedYear = null;
    this.year = null;
    this.counte = new Counte();
    // this.loadCommunes();
    this.loadYears();
    this.loadIndicateurs();
    this.loadTypesIndicateur();
    this.isDowlodingAll = false;
    this.isDowlodingSelected = false;
    // this.loadRegions();
    // this.loadDomaines();
    this.counteService.query().subscribe((res: HttpResponse<ICounte[]>) => {
      if (res.body.length > 0) {
        this.counte = res.body[0];
        if (sessionStorage.getItem('SessionData') == null) {
          const nbr = this.counte.nombreVisiteurs;
          this.counte.nombreVisiteurs = nbr + 1;
          this.counteService.update(this.counte).subscribe(() => {
            this.loadCounte();
          });
          sessionStorage.setItem('SessionData', 'VISITOR');
        } else {
          this.loadCounte();
        }
      } else {
        this.counte.nombreVisiteurs = 1;
        this.counteService.create(this.counte).subscribe(() => {
          this.loadCounte();
        });
      }
    });
    // this.getAllCouleur();
  }

  ngAfterViewInit() {
    this.mapCart = Leaflet.map('bf_carte', {
      crs: Leaflet.CRS.Simple,
      zoomControl: false,
      zoom: 6.3,
      minZoom: 6.3,
      maxZoom: 6.3
    }).setView([12.337372161077168, -1.5596362929761654], 6.3);
    const bounds = [[-100, -200], [500, 500]];
    // var image = Leaflet.imageOverlay('https://a.tile.openstreetmap.org/0/0/0.png', bounds, {
    //   attribution: '&copy; <a href="http://osm.org/copyright">OpenStreetMap</a> contributors'
    // }).addTo(this.mapCart);
    // this.mapCart.fitBounds(bounds);
    this.mapCart.setMaxBounds(bounds);
    this.getCarteData();

    this.portailService.findAllIndicateur().subscribe((res1: HttpResponse<IIndicateur[]>) => {
      this.indicateurTemps = res1.body;
      window.console.log(this.indicateurTemps);
      this.indicateurs = res1.body;
      if (this.indicateurs.length > 0) {
        this.selectedIndicateur = this.indicateurs[0];
        //  window.console.log(this.selectedIndicateur);

        this.portailService.getAllCouleurs().subscribe(val => {
          this.chargerCouleur = true;
          this.colors = val.body;
          this.portailService
            .findAllPerformanceForAllCommuneAndExerciceAndIndicateur(this.selectedYear.id, this.selectedIndicateur.id)
            .subscribe((perform: HttpResponse<IPerformance[]>) => {
              this.allPerformances = perform.body;
              this.changeLayerColor();
              this.chargerCouleur = false;
            });
        });
      }
    });

    // this.getDefaultLegendeCouleur();
    /*  this.legend.onAdd = function(map) {
      const div = Leaflet.DomUtil.create('div', 'info legend');
      // loop through our density intervals and generate a label with a colored square for each interval
        this.portailService.findAllRegions().subscribe((res: HttpResponse<IRegion[]>) => {
          res.body.forEach(region => {
            window.console.log(res.body);
            div.innerHTML +=
              '<i style="background:' + region.couleur + '"> </i> ' + region.libelle + '<br>';
          });
          this.legend.addTo(this.mapCart);

        });



      return div;
    };
     */

    this.items = [
      {
        label: 'Format (xls)',
        command: () => {
          this.dowloadAllData(0);
        }
      },
      {
        label: 'Format (csv)',
        command: () => {
          this.dowloadAllData(1);
        }
      }
    ];

    this.items2 = [
      {
        label: 'Format (xls)',
        command: () => {
          this.dowloadSelectedData(0);
        }
      },
      {
        label: 'Format (csv)',
        command: () => {
          this.dowloadSelectedData(1);
        }
      }
    ];
  }

  loadCounte() {
    this.counteService.query().subscribe((res: HttpResponse<ICounte[]>) => {
      this.countes = res.body;
    });
  }

  ngOnDestroy() {
    /* if (this.authSubscription) {
      this.eventManager.destroy(this.authSubscription);
    } */
  }

  onTypeChange() {
    window.console.log(this.typeIndic);
    if (this.typeIndic) {
      this.portailService.findIndicByTypeIndica(this.typeIndic.id).subscribe((res: HttpResponse<IIndicateur[]>) => {
        this.indicateurs = res.body;
      });
    } else {
      this.loadIndicateurs();
    }
  }

  loadCommunes(idAnnee: number) {
    this.portailService.findAllCommune(idAnnee, 'byAnnee').subscribe((res: HttpResponse<ICommune[]>) => (this.communes = res.body));
  }

  loadRegions() {
    this.portailService
      .findAllRegions()
      .subscribe((res: HttpResponse<IRegion[]>) => ((this.regions = res.body), window.console.log(res.body)));
  }

  loadYears() {
    this.portailService.findAllExercices().subscribe((res: HttpResponse<IExercice[]>) => {
      this.years = res.body;
      if (this.years.length > 0) {
        this.minYear = this.years[0].annee;
        this.maxYear = this.years[this.years.length - 1].annee;
        this.selectedYear = this.years[this.years.length - 1];
        this.year = this.selectedYear.annee;
        this.loadCommunes(this.selectedYear.id);
      }
    });
  }

  loadIndicateurs() {
    this.portailService.findAllIndicateur().subscribe((res: HttpResponse<IIndicateur[]>) => {
      this.indicateurTemps = res.body;
      this.indicateurs = res.body;
    });
  }

  loadTypesIndicateur() {
    this.portailService.findAllTypeIndicateur().subscribe((res: HttpResponse<ITypeIndicateur[]>) => (this.typeIndicateurs = res.body));
  }

  getCarteData() {
    this.portailService.getGeoJsonData().subscribe((res: HttpResponse<any>) => {
      this.carteData = res.body;
      this.geojson = Leaflet.geoJSON(res.body, { style: this.style, onEachFeature: this.onEachFeature });
      this.geojson.addTo(this.mapCart);
      // window.console.log(this.mapCart);
      this.portailService.findAllRegions().subscribe((reg: HttpResponse<IRegion[]>) => {
        if (reg.body) {
          for (const f in this.mapCart._layers) {
            if (Object.prototype.hasOwnProperty.call(this.mapCart._layers, f)) {
              let couleur = '#666';
              const layer = this.mapCart._layers[f];
              if (layer.feature)
                if (layer.feature.properties.region_id) {
                  const communeRegion = reg.body.filter(region => region.id === layer.feature.properties.region_id);
                  //  window.console.log(communeRegion);
                  if (communeRegion && communeRegion.length >= 1) couleur = communeRegion[0].couleur;
                  layer.setStyle({
                    fillColor: couleur,
                    color: 'white',
                    weight: 2,
                    opacity: 1,
                    dashArray: '3',
                    fillOpacity: 0.7
                  });
                }
            }
          }
          if (res.body.length > 0) this.legendCouleurs = res.body;
          this.legend.onAdd = function() {
            const div = Leaflet.DomUtil.create('div', 'info legend');
            reg.body.forEach(region => {
              div.innerHTML += '<i style="background:' + region.couleur + '"> </i> ' + region.libelle + '<br>';
            });
            //  window.console.log(this.mapCart._layers);
            //  if (mapCart)

            return div;
          };
          this.legend.addTo(this.mapCart);
        }
      });
    });
  }

  style(commune) {
    // const color= () =>{this.getCouleur(100)} ;
    return {
      // fillColor: color,
      fillColor: 'white',
      weight: 2,
      opacity: 1,
      color: 'black',
      dashArray: '3',
      fillOpacity: 0.7
    };
  }

  changeLayerColor() {
    // const selectedIndicateur: Indicateur = event.data;
    // window.console.log(selectedIndicateur);
    if (this.selectedYear) {
      if (this.selectedYear.id) {
        if (this.allPerformances) {
          if (this.allPerformances.length > 0) {
            if (this.selectedIndicateur) {
              //   window.console.log('====couleur body ========' + color.body);
              const colors: ICouleur[] = this.colors.filter(col => col.indicateurId === this.selectedIndicateur.id);
              if (colors) {
                if (colors.length > 0) {
                  for (const f in this.mapCart._layers) {
                    if (Object.prototype.hasOwnProperty.call(this.mapCart._layers, f)) {
                      let couleur = 'black';
                      const layer = this.mapCart._layers[f];
                      if (layer.feature)
                        if (layer.feature.properties.id) {
                          const score = this.allPerformances.filter(perf => perf.communeId === layer.feature.properties.id);
                          if (score) {
                            if (score.length > 0) {
                              couleur = colors.filter(col => col.maxVal >= score[0].score && col.minVal <= score[0].score)[0]
                                ? colors.filter(col => col.maxVal >= score[0].score && col.minVal <= score[0].score)[0].couleur
                                : '#e7e7e7';
                              layer.setStyle({
                                fillColor: couleur,
                                color: 'white',
                                weight: 2,
                                opacity: 1,
                                dashArray: '3',
                                fillOpacity: 0.7
                              });
                            } else {
                              layer.setStyle({
                                fillColor: '#e7e7e7',
                                color: 'white',
                                weight: 2,
                                opacity: 1,
                                dashArray: '3',
                                fillOpacity: 0.7
                              });
                            }
                          }
                        }
                    }
                  }
                } // il faut mettre  else et un alert ou toast pour dire qu'aucune couleur trouvée
              } // il faut mettre  else et un alert ou toast pour dire qu'aucune couleur trouvée
              this.getLegendeCouleur(colors);
            }
          }
        }
      }
    }
  }

  getCouleur(score) {
    let couleur = '#5ab55d';
    if (this.selectedIndicateur) {
      this.portailService.getCouleurs(this.selectedIndicateur.id).subscribe((res: HttpResponse<ICouleur[]>) => {
        // window.console.log(res.body);
        if (res.body) {
          if (res.body.length > 0) couleur = res.body.filter(color => color.maxVal >= score && color.minVal <= score)[0].couleur;
          // window.console.log(couleur);
        }
      });
    }
    return couleur;
  }

  getAllCouleur() {
    this.portailService.getAllCouleurs().subscribe(val => {
      this.colors = val.body;
      window.console.log(this.colors);
    });
  }

  getLegendeCouleur(colors: ICouleur[]) {
    /* if (this.selectedIndicateur) {
      // const indicateurUnite = this.selectedIndicateur.uniteBorne;
      if (this.selectedIndicateur.id)
        this.portailService.getCouleurs(this.selectedIndicateur.id).subscribe((res: HttpResponse<ICouleur[]>) => {
          //  window.console.log(res.body);
          if (res.body) {
            if (res.body.length > 0) this.legendCouleurs = res.body;
            this.legend.onAdd = function(map) {
              const div = Leaflet.DomUtil.create('div', 'info legend');
              div.innerHTML += '<i style="background:#e7e7e7"></i> sans performances <br>';
              // loop through our density intervals and generate a label with a colored square for each interval
              res.body.forEach(color => {
                div.innerHTML +=
                  '<i style="background:' +
                  color.couleur +
                  '"> </i> ' +
                  color.minVal +
                  (color.maxVal ? '&ndash;' + color.maxVal + '&nbsp;' + 'pts' + '<br>' : '+');
              });

              return div;
            };
            this.legend.addTo(this.mapCart);
          }
        });
    } */
    if (this.selectedIndicateur) {
      if (colors) {
        if (colors.length > 0) this.legendCouleurs = colors;
        this.legend.onAdd = function(map) {
          const div = Leaflet.DomUtil.create('div', 'info legend');
          div.innerHTML += '<i style="background:#e7e7e7"></i> sans performances <br>';
          // loop through our density intervals and generate a label with a colored square for each interval
          colors.forEach(color => {
            div.innerHTML +=
              '<i style="background:' +
              color.couleur +
              '"> </i> ' +
              color.minVal +
              (color.maxVal ? '&ndash;' + color.maxVal + '&nbsp;' + 'pts' + '<br>' : '+');
          });
          return div;
        };
        this.legend.addTo(this.mapCart);
      }
    }
  }

  onEachFeature(commue, layer) {
    layer.on({
      mouseover: () => {
        layer.bindTooltip('<h6>' + layer.feature.properties.libelle + '</h6>').openTooltip();
        layer.setStyle({
          weight: 2,
          opacity: 1,
          dashArray: '3',
          fillOpacity: 1
        });
        this.prevLayerClicked = layer;
      },
      mouseout: () => {
        if (this.prevLayerClicked !== null) {
          this.prevLayerClicked.setStyle({
            weight: 2,
            opacity: 1,
            dashArray: '3',
            fillOpacity: 0.7
          });
        }
      },
      click: () => {
        (Leaflet.DomUtil.get('communeId') as HTMLInputElement).value = layer.feature.properties.id;
        const popupButton = Leaflet.DomUtil.get('popupButton');
        popupButton.click();
        this.prevLayerClicked.closeTooltip();
      }
    });
  }

  // add by mohams
  onTableRowSelected(selectedIndicateur: IIndicateur) {
    this.selectedIndicateur = selectedIndicateur;
    if (this.selectedCommune && this.selectedIndicateur) {
      this.loadPerformances();
      this.sowPopu();
    }
  }

  loadPerformances() {
    if (this.selectedYear && this.selectedIndicateur) {
      this.portailService
        .findAllPerformanceForAllCommuneAndExerciceAndIndicateur(this.selectedYear.id, this.selectedIndicateur.id)
        .subscribe((perform: HttpResponse<IPerformance[]>) => {
          this.allPerformances = perform.body;
        });
    }
  }

  /* loadPerfByYear() {
    if (this.selectedYear) {
          this.portailService
            .findAllPerformanceForAllCommuneAndExercice(this.selectedYear.id)
            .subscribe((perform: HttpResponse<IPerformance[]>) => {
              this.allPerformances = perform.body;
              window.console.log(this.allPerformances);
            });
    }
  } */

  onFilterChange() {
    if (this.years) {
      this.selectedYear = this.years.find(value1 => value1.annee === this.year);
      if (this.selectedYear) {
        this.loadCommunes(this.selectedYear.id);
        if (this.selectedIndicateur) {
          if (this.selectedYear && this.selectedIndicateur) {
            this.chargerCouleur = true;
            this.portailService
              .findAllPerformanceForAllCommuneAndExerciceAndIndicateur(this.selectedYear.id, this.selectedIndicateur.id)
              .subscribe((perform: HttpResponse<IPerformance[]>) => {
                this.allPerformances = perform.body;
                this.changeLayerColor();
                this.chargerCouleur = false;
              });
          }
        }
        if (this.selectedCommune && this.selectedIndicateur) {
          // window.console.log(this.selectedCommune);
          this.sowPopu();
        }
      }
    }
  }

  onCommuneChange(commune: ICommune) {
    if (commune) {
      (Leaflet.DomUtil.get('communeId') as HTMLInputElement).value = '' + commune.id;
      this.selectedCommune = commune;
      // window.console.log(this.selectedCommune);
      this.mapCart.setView([commune.positionLabelLat, commune.positionLabelLon]);

      // this.mapCart.setView([commune.positionLabelLat, commune.positionLabelLon], 9);
      // ==================================== //
      this.id1 = this.selectedYear.id;
      this.id2 = this.selectedCommune.id;
      //  window.console.log('id 1 = ' + this.id1 + ' id 2 = ' + this.id2);
      this.sowPopu();
      // this.getIndicateurByInterval();
    }
  }

  sowPopu() {
    let selectedLayer = null;
    const communeId = parseInt((Leaflet.DomUtil.get('communeId') as HTMLInputElement).value, 0);
    for (const f in this.mapCart._layers) {
      if (Object.prototype.hasOwnProperty.call(this.mapCart._layers, f)) {
        const layer = this.mapCart._layers[f];
        if (layer.feature)
          if (layer.feature.properties.id === communeId) {
            selectedLayer = layer;
            this.selectedCommune = this.communes.filter(comm => comm.id === communeId)[0];
            // this.selectedCommune = this.communes.find(comm => comm.id.toString().indexOf(communeId.toString()) === 0);
            // window.console.log(this.selectedCommune);
            // this.getIndicateurByInterval();
          }
      }
    }

    if (this.selectedYear && this.selectedIndicateur) {
      let popupContent = 'Aucune donnée';
      if (this.selectedYear.id && this.selectedIndicateur.id) {
        if (this.selectedCommune) {
          this.portailService
            .findAllPerformanceByCommuneAndExerciceAndIndicateur(this.selectedCommune.id, this.selectedYear.id, this.selectedIndicateur.id)
            .subscribe((res: HttpResponse<any>) => {
              if (res.body) {
                const nbCommuneByRegion = this.communes.filter(comm => comm.libelleRegion.indexOf(this.selectedCommune.libelleRegion) === 0)
                  .length;
                this.communePerformance = res.body;
                // this.selectedCommune = res.body.commune;
                // window.console.log(this.selectedCommune);
                popupContent =
                  '<div id="popupDiv" class="font-weight-bold">' +
                  '<div class="text-center"><span class="font-weight-bold text-center text-warning">Commune de ' +
                  this.selectedCommune.libelle +
                  '</span><br>' +
                  '<span class="font-weight-bold text-center text-warning">Région: ' +
                  this.selectedCommune.libelleRegion +
                  '</span></div>' +
                  '<hr>' +
                  '<span>' +
                  this.selectedIndicateur.libelle +
                  ' en ' +
                  this.selectedYear.annee +
                  ': ' +
                  this.truncateNumber(this.communePerformance.score) +
                  ' pts' +
                  '</span><br>' +
                  '<span> Ecart par rapport' +
                  ' à ' +
                  (this.selectedYear.annee - 1) +
                  ': ' +
                  this.communePerformance.scoreAnneePrec +
                  ' (pts)' +
                  '</span><br>' +
                  'Rang Nationnal: ' +
                  this.communePerformance.commune.rangNational +
                  'ème' +
                  ' sur ' +
                  this.communes.length +
                  ' Communes' +
                  '</span><br>' +
                  '<span>' +
                  'Rang Régional: ' +
                  this.communePerformance.commune.rangRegional +
                  'ème' +
                  ' sur ' +
                  nbCommuneByRegion +
                  ' Communes.' +
                  '</span><br>' +
                  '<div class="font-weight-bold mt-3"><span class="mr-3"><a href=/portail/poster?idY=' +
                  this.selectedYear.id +
                  '&idC=' +
                  this.selectedCommune.id +
                  '><i class="fa fa-book mr-2"></i><span>Aller aux posters</span></a></span>' +
                  '<span class="ml-3"><a href=/portail/donnee?idY=' +
                  this.selectedYear.id +
                  '&idC=' +
                  this.selectedCommune.id +
                  '><i class="fa fa-database mr-2"></i><span>Aller aux données</span></a>' +
                  '</span></div>' +
                  '</div>';
              } else {
                popupContent =
                  '<div><span>Commune de ' +
                  this.selectedCommune.libelle +
                  '</span><hr><span> ' +
                  this.selectedIndicateur.libelle +
                  ' (' +
                  this.selectedYear.annee +
                  ')</span><hr><span>Aucune performance trouvée </span></div>';
              }
              selectedLayer.bindPopup(popupContent).openPopup();
            });
        } else {
          popupContent = "<div><span>Commune non prise en compte pour l'année " + this.selectedYear.annee + ' </span></div>';
          selectedLayer.bindPopup(popupContent).openPopup();
        }
      } else {
        selectedLayer
          .bindPopup(
            '<div> Veuillez selectionner une année et un indicateur pour voir la perfomance de la commune de ' +
              this.selectedCommune.libelle +
              '</div>'
          )
          .openPopup();
      }
    } else {
      selectedLayer
        .bindPopup(
          '<div> Veuillez selectionner une année et un indicateur pour voir la perfomance de la commune de ' +
            this.selectedCommune.libelle +
            '</div>'
        )
        .openPopup();
    }
  }

  dowloadAllData(num: number) {
    if (num === 0) {
      this.isDowlodingAll = true;
      this.portailService.exportAllData(Extension.XLSX).subscribe((res: HttpResponse<any>) => {
        const filename = res.headers.get('filename');
        this.saveFile(res.body, filename, 'application/vnd.ms.excel');
        this.isDowlodingAll = false;
      });
    }
    if (num === 1) {
      this.isDowlodingAll = true;
      this.portailService.exportAllData(Extension.CSV).subscribe((res: HttpResponse<any>) => {
        const filename = res.headers.get('filename');
        this.saveFile(res.body, filename, 'application/vnd.ms.excel');
        this.isDowlodingAll = false;
      });
    }
  }

  dowloadSelectedData(num: number) {
    if (num === 0) {
      this.isDowlodingSelected = true;
      this.portailService
        .exportExcelScore(this.selectedCommune.id, this.selectedYear.id, this.selectedYear.id, Extension.XLSX)
        .subscribe(value => {
          const filename = value.headers.get('filename');
          this.portailService.saveFile(value.body, filename, 'application/vnd.ms.excel');
          this.isDowlodingSelected = false;
        });
      /* this.portailService.exportSelectedData(this.selectedIndicateur.id, Extension.XLSX).subscribe(value => {
        const filename = value.headers.get('filename');
        this.saveFile(value.body, filename, 'application/vnd.ms.excel');
        this.isDowlodingSelected = false;
      }); */
    }
    if (num === 1) {
      this.isDowlodingSelected = true;
      this.portailService
        .exportExcelScore(this.selectedCommune.id, this.selectedYear.id, this.selectedYear.id, Extension.CSV)
        .subscribe(value => {
          const filename = value.headers.get('filename');
          this.portailService.saveFile(value.body, filename, 'application/vnd.ms.excel');
          this.isDowlodingSelected = false;
        });
      /* this.portailService.exportSelectedData(this.selectedIndicateur.id, Extension.CSV).subscribe(value => {
        const filename = value.headers.get('filename');
        this.saveFile(value.body, filename, 'application/vnd.ms.excel');
        this.isDowlodingSelected = false;
      }); */
    }
  }

  saveFile(data: any, filename?: string, type?: string) {
    const blob = new Blob([data], { type: `${type}; charset=utf-8` });
    saveAs(blob, filename);
  }

  downloadCarte(catreData) {
    const carte = catreData.getBase64Image();
    this.saveFile([carte], 'carte');
  }

  loadLayerDefaultColor() {}

  gotoPoster() {
    alert('ok');
  }

  grapheForTop5(comm: any[], val: any[]) {
    this.data = {
      labels: comm,
      datasets: [
        {
          label: this.selectedIndicateur.libelle,
          data: val,
          fill: false,
          borderColor: '#4bc0c0',
          backgroundColor: '#9CCC65'
        }
      ]
    };
  }

  grapheOfByIndicateur(indicateur: IIndicateur) {
    const indicat: Indicateur = this.myIndicateurs.filter(indic => indic.id === indicateur.id)[0];
    if (indicat !== null) {
      const indicaScore: any[] = [];

      indicat.scores.forEach(score => {
        indicaScore.push(score.score);
      });
      if (indicaScore.length > 0) {
        this.data1 = {
          labels: this.annees,
          datasets: [
            {
              label: this.selectedIndicateur.libelle,
              data: indicaScore,
              fill: false,
              borderColor: '#4bc0c0',
              backgroundColor: '#9CCC65'
            }
          ]
        };
      }
    }
  }

  classerCommune(dataFilter: string, id: number) {
    this.portailService.classementCommuneTop5(this.selectedYear.id, dataFilter, id).subscribe((res: HttpResponse<ICommune[]>) => {
      this.communes5 = res.body;
      if (this.communes5.length > 0) {
        const comms: any[] = [];
        const values: number[] = [];

        this.communes5.forEach(value1 => {
          comms.push(value1.libelle);
          values.push(value1.scoreCommune);
        });

        if (comms.length > 0 && values.length > 0) {
          this.grapheForTop5(comms, values);
        }
      }
    });
  }

  getIndicateurByInterval() {
    this.portailService.scoreCommuneAndByIntervalAnnee(this.selectedCommune.id, 0, 0).subscribe((res: HttpResponse<IIndicateur[]>) => {
      this.myIndicateurs = res.body;
      if (this.myIndicateurs.length > 0) {
        this.annees = [];
        this.myIndicateurs[0].scores.forEach(value => this.annees.push(value.annee));
        this.annees.sort();
        if (this.selectedIndicateur) {
          this.grapheOfByIndicateur(this.selectedIndicateur);
        }
      }
    });
  }

  printChart1(chart) {
    const chartImg = chart.getBase64Image();
    saveAs(chartImg, 'Top_cinq_des_communes');
  }

  printChart2(chart1) {
    const chartImg = chart1.getBase64Image();
    saveAs(chartImg, 'graphe');
  }

  truncateNumber(a: number) {
    return Math.floor(a * 1000) / 1000;
  }
}
