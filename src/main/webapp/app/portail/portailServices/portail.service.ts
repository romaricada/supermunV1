import { IRegion } from 'app/shared/model/region.model';
import { Injectable } from '@angular/core';
import { SERVER_API_URL } from 'app/app.constants';
import { HttpClient, HttpHeaders, HttpResponse } from '@angular/common/http';
import { ICommune } from 'app/shared/model/commune.model';
import { createRequestOption } from 'app/shared/util/request-util';
import { Observable } from 'rxjs';
import { saveAs } from 'file-saver';
import { IExercice } from 'app/shared/model/exercice.model';
import { IIndicateur, Indicateur } from 'app/shared/model/indicateur.model';
import { IInformation } from 'app/shared/model/information.model';
import { IPublication } from 'app/shared/model/publication.model';
import { IDomaine } from 'app/shared/model/domaine.model';
import { IPerformance } from 'app/shared/model/performance.model';
import { ITypeIndicateur } from 'app/shared/model/type-indicateur.model';
import { ICouleur } from 'app/shared/model/couleur.model';
import { ITypePublication } from 'app/shared/model/type-publication.model';
import { Extension } from 'app/shared/model/extension.enum';
import { IDictionaires } from 'app/shared/model/dictionaires.model';

type EntityResponseType = HttpResponse<IPerformance>;

@Injectable({ providedIn: 'root' })
export class PortailService {
  public portailResourceUrl = SERVER_API_URL + 'api/portail';

  constructor(protected http: HttpClient) {}

  findAllCommune(anneeId: number, typReturn: string, req?: any): Observable<HttpResponse<ICommune[]>> {
    const options = createRequestOption({ anneeId, typReturn, req });
    return this.http.get<ICommune[]>(this.portailResourceUrl + '/communes', { params: options, observe: 'response' });
  }
  findAllRegions(req?: any): Observable<HttpResponse<IRegion[]>> {
    const options = createRequestOption(req);
    return this.http.get<IRegion[]>(this.portailResourceUrl + '/regions/all-region', { params: options, observe: 'response' });
  }

  findAllExercices(req?: any): Observable<HttpResponse<IExercice[]>> {
    const options = createRequestOption(req);
    return this.http.get<IExercice[]>(this.portailResourceUrl + '/exercices', { params: options, observe: 'response' });
  }

  findAllInformation(req?: any): Observable<HttpResponse<IInformation[]>> {
    const options = createRequestOption(req);
    return this.http.get<IInformation[]>(this.portailResourceUrl + '/informations', {
      params: options,
      observe: 'response'
    });
  }

  findAlDomaines(req?: any): Observable<HttpResponse<IDomaine[]>> {
    const options = createRequestOption(req);
    return this.http.get<IDomaine[]>(this.portailResourceUrl + '/domaines', {
      params: options,
      observe: 'response'
    });
  }

  findAllTypeIndicateur(req?: any): Observable<HttpResponse<ITypeIndicateur[]>> {
    const options = createRequestOption(req);
    return this.http.get<ITypeIndicateur[]>(this.portailResourceUrl + '/type-indicateurs', {
      params: options,
      observe: 'response'
    });
  }

  findAllIndicateur(req?: any): Observable<HttpResponse<IIndicateur[]>> {
    const options = createRequestOption(req);
    return this.http.get<IIndicateur[]>(this.portailResourceUrl + '/indicateurs', {
      params: options,
      observe: 'response'
    });
  }

  findAllPerformanceByCommuneAndExerciceAndIndicateur(idCommune, idExercice, idIndicateur): Observable<HttpResponse<IPerformance>> {
    const options = createRequestOption({ idCommune, idExercice, idIndicateur });
    return this.http.get<IPerformance>(this.portailResourceUrl + '/performances/all-by-commune-exercice-indicateur', {
      params: options,
      observe: 'response'
    });
  }

  findAllPerformanceForAllCommuneAndExerciceAndIndicateur(idExercice, idIndicateur): Observable<HttpResponse<IPerformance[]>> {
    const options = createRequestOption({ idExercice, idIndicateur });
    return this.http.get<IPerformance[]>(this.portailResourceUrl + '/performances/all-by-exercice-indicateur', {
      params: options,
      observe: 'response'
    });
  }

  findAllPerformanceForAllCommuneAndExercice(idExercice): Observable<HttpResponse<IPerformance[]>> {
    const options = createRequestOption({ idExercice });
    return this.http.get<IPerformance[]>(this.portailResourceUrl + '/performances/all-by-exercice', {
      params: options,
      observe: 'response'
    });
  }

  getCouleurs(idIndicateur): Observable<HttpResponse<ICouleur[]>> {
    const options = createRequestOption({ idIndicateur });
    return this.http.get<ICouleur[]>(this.portailResourceUrl + '/couleurs/all-by-indicateur', {
      params: options,
      observe: 'response'
    });
  }

  getAllCouleurs(): Observable<HttpResponse<ICouleur[]>> {
    return this.http.get<ICouleur[]>(this.portailResourceUrl + '/couleurs/find-all', {
      observe: 'response'
    });
  }

  getGeoJsonData(): Observable<HttpResponse<any>> {
    return this.http.get<any>(`${this.portailResourceUrl}/geojson`, { observe: 'response' });
  }

  findAlPublication(req?: any): Observable<HttpResponse<IPublication[]>> {
    const options = createRequestOption(req);
    return this.http.get<IPublication[]>(this.portailResourceUrl + '/publications', {
      params: options,
      observe: 'response'
    });
  }

  findAllTypePublication(req?: any): Observable<HttpResponse<ITypePublication[]>> {
    const options = createRequestOption(req);
    return this.http.get<ITypePublication[]>(this.portailResourceUrl + '/typePublications/all-type', {
      params: options,
      observe: 'response'
    });
  }

  telechargerPublication(id: number): Observable<ArrayBuffer> {
    return this.http.get(`${this.portailResourceUrl}/downloadPub`, {
      params: createRequestOption({ id }),
      responseType: 'arraybuffer'
    });
  }

  scoreByAnneeAndDomaine(idCommune: number, idExercice: number): Observable<HttpResponse<IDomaine[]>> {
    const options = createRequestOption({ idCommune, idExercice });
    return this.http.get<IDomaine[]>(`${this.portailResourceUrl}/domaines/score-by-domaine`, {
      params: options,
      observe: 'response'
    });
  }

  scoreCommuneAndByIntervalAnnee(idCommune: number, idAnnee1: number, idAnnee2: number): Observable<HttpResponse<IIndicateur[]>> {
    const options = createRequestOption({ idCommune, idAnnee1, idAnnee2 });
    return this.http.get<IIndicateur[]>(`${this.portailResourceUrl}/indicateurs/score-indicateur-between-year`, {
      params: options,
      observe: 'response'
    });
  }

  classementCommune(idExercice: number, dataFilter: string, id: number): Observable<HttpResponse<ICommune[]>> {
    const options = createRequestOption({ idExercice, dataFilter, id });
    return this.http.get<ICommune[]>(`${this.portailResourceUrl}/communes/classement`, {
      params: options,
      observe: 'response'
    });
  }

  classementCommuneTop5(idExercice: number, dataFilter: string, id: number): Observable<HttpResponse<ICommune[]>> {
    const options = createRequestOption({ idExercice, dataFilter, id });
    return this.http.get<ICommune[]>(`${this.portailResourceUrl}/communes/classement-top-5`, {
      params: options,
      observe: 'response'
    });
  }

  findAllGroupeDomaineByCommuneAnExercice(idCommune: number, idExercice: number): Observable<HttpResponse<ITypeIndicateur[]>> {
    const options = createRequestOption({ idCommune, idExercice });
    return this.http.get<ITypeIndicateur[]>(`${this.portailResourceUrl}/domaines/score-by-groupe-domaine-indicateur`, {
      params: options,
      observe: 'response'
    });
  }

  findAllIndicatorByCommune(idAnnee: number, idCommune: number, dataFilter: string, id: number): Observable<HttpResponse<IIndicateur[]>> {
    const options = createRequestOption({ idAnnee, idCommune, dataFilter, id });
    return this.http.get<IIndicateur[]>(`${this.portailResourceUrl}/indicateurs/all-to-compare`, {
      params: options,
      observe: 'response'
    });
  }

  imprimeListeClassement(idExercice: number, dataFilter: string, id: number): Observable<ArrayBuffer> {
    return this.http.get(`${this.portailResourceUrl}/communes/classement-print`, {
      params: createRequestOption({ idExercice, dataFilter, id }),
      responseType: 'arraybuffer'
    });
  }

  exportDataClasement(idExercice: number, dataFilter, id: number, extension: Extension): Observable<HttpResponse<ArrayBuffer>> {
    const options = createRequestOption({ idExercice, dataFilter, id, extension });
    let header = new HttpHeaders();
    header = header.append('Accept', 'application/vnd.ms.excel; charset=utf-8');
    return this.http.get(`${this.portailResourceUrl}/communes/classement-exportation`, {
      headers: header,
      params: options,
      observe: 'response',
      responseType: 'arraybuffer'
    });
  }

  exportExcelScore(idCommune: number, idAnnee1: number, idAnnee2: number, extension: Extension): Observable<HttpResponse<ArrayBuffer>> {
    const options = createRequestOption({ idCommune, idAnnee1, idAnnee2, extension });
    let header = new HttpHeaders();
    header = header.append('Accept', 'application/vnd.ms.excel; charset=utf-8');
    return this.http.get(`${this.portailResourceUrl}/download-data-between-year`, {
      headers: header,
      params: options,
      observe: 'response',
      responseType: 'arraybuffer'
    });
  }

  exportAllData(extension: Extension) {
    let header = new HttpHeaders();
    const options = createRequestOption({ extension });
    header = header.append('Accept', 'application/vnd.ms.excel; charset=utf-8');
    return this.http.get(this.portailResourceUrl + '/download-all-data', {
      headers: header,
      params: options,
      observe: 'response',
      responseType: 'arraybuffer'
    });
  }

  exportSelectedData(idIndicateur: number, extension: Extension) {
    const options = createRequestOption({ idIndicateur, extension });
    let header = new HttpHeaders();
    header = header.append('Accept', 'application/vnd.ms.excel; charset=utf-8');
    return this.http.get(this.portailResourceUrl + '/download-seleted-data', {
      headers: header,
      params: options,
      observe: 'response',
      responseType: 'arraybuffer'
    });
  }

  saveFile(data: any, filename?: string, type?: string) {
    const blob = new Blob([data], { type: `${type}; charset=utf-8` });
    saveAs(blob, filename);
  }

  findAllDictionaire(): Observable<HttpResponse<IDictionaires[]>> {
    return this.http.get<IDictionaires[]>(this.portailResourceUrl + '/finAllDictionaire', { observe: 'response' });
  }

  findIndicByTypeIndica(id: number): Observable<HttpResponse<Indicateur[]>> {
    return this.http.get<IIndicateur[]>(this.portailResourceUrl + '/find-indicateur-by-typeIndic', {
      params: createRequestOption({ id }),
      observe: 'response'
    });
  }

  /* incrementPageCount(){
    const pageCount = this.af.object('/pageCount/').$ref
      .ref.transaction(count => {
        return count + 1;
      }).then((data) => {return data.snapshot.node_.value_;});

    return pageCount;
  }*/
}
