import { HttpClient, HttpResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';

import { SERVER_API_URL } from 'app/app.constants';
import { CommuneCopy } from 'app/shared/model/commune.copy';
import { IPerformance } from 'app/shared/model/performance.model';
import { createRequestOption } from 'app/shared/util/request-util';
import { Observable } from 'rxjs';

type EntityResponseType = HttpResponse<IPerformance>;
type EntityArrayResponseType = HttpResponse<IPerformance[]>;

@Injectable({ providedIn: 'root' })
export class PerformanceService {
  public adminResourceUrl = SERVER_API_URL + 'api/admin/performances';
  public portailResourceUrl = SERVER_API_URL + 'api/portail/performances';

  constructor(protected http: HttpClient) {}

  create(performance: IPerformance): Observable<EntityResponseType> {
    return this.http.post<IPerformance>(this.adminResourceUrl, performance, { observe: 'response' });
  }

  update(id: number, valeur: number, score: boolean): Observable<EntityResponseType> {
    return this.http.put<any>(this.adminResourceUrl + '/update?id=' + id + '&valeur=' + valeur + '&score=' + score, {
      observe: 'response'
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IPerformance>(`${this.portailResourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IPerformance[]>(this.adminResourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.adminResourceUrl}/${id}`, { observe: 'response' });
  }

  findAllDataOfCommune(idAnnee, idType, page, size, sort): Observable<HttpResponse<CommuneCopy[]>> {
    const options = createRequestOption({ page, size, sort, idAnnee, idType });
    return this.http.get<CommuneCopy[]>(this.adminResourceUrl + '/all-list', { params: options, observe: 'response' });
  }

  validateCommuneData(data: any, anneeId: number, typeId: number): Observable<HttpResponse<boolean>> {
    const options = createRequestOption({ anneeId, typeId });
    return this.http.put<boolean>('api/admin/commune-data/validate', data, {
      params: options,
      observe: 'response'
    });
  }

  validateAllCommuneData(anneeId: number, typeId: number): Observable<HttpResponse<boolean>> {
    const options = createRequestOption({ anneeId, typeId });
    return this.http.get<boolean>('api/admin/commune-data/validate-all', {
      params: options,
      observe: 'response'
    });
  }

  checkValidateAllCommuneData(anneeId: number, typeId: number): Observable<HttpResponse<boolean>> {
    const options = createRequestOption({ anneeId, typeId });
    return this.http.get<boolean>('api/portail/commune-data/check-validate-all', {
      params: options,
      observe: 'response'
    });
  }

  viderPerformanceByAnneeAndTypIndic(anneId: number, typeId: number): Observable<HttpResponse<void>> {
    const options = createRequestOption({ anneId, typeId });
    return this.http.get<void>('api/admin/vider_performance', {
      params: options,
      observe: 'response'
    });
  }

  devaliderDonnees(anneId: number, typeId: number): Observable<HttpResponse<void>> {
    const options = createRequestOption({ anneId, typeId });
    return this.http.get<void>('api/admin/devalider-donnee', {
      params: options,
      observe: 'response'
    });
  }
}
