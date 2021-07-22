import { HttpClient, HttpResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';

import { SERVER_API_URL } from 'app/app.constants';
import { ICommune } from 'app/shared/model/commune.model';
import { IEtatCommune } from 'app/shared/model/etat-commune.model';
import { createRequestOption } from 'app/shared/util/request-util';
import { Observable } from 'rxjs';

type EntityResponseType = HttpResponse<ICommune>;
type EntityArrayResponseType = HttpResponse<ICommune[]>;

@Injectable({ providedIn: 'root' })
export class CommuneService {
  public adminResourceUrl = SERVER_API_URL + 'api/admin/communes';
  public portailResourceUrl = SERVER_API_URL + 'api/portail/communes';

  constructor(protected http: HttpClient) {}

  create(commune: ICommune): Observable<EntityResponseType> {
    return this.http.post<ICommune>(this.adminResourceUrl, commune, { observe: 'response' });
  }

  update(commune: ICommune): Observable<EntityResponseType> {
    return this.http.put<ICommune>(this.adminResourceUrl, commune, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ICommune>(`${this.portailResourceUrl}/${id}`, { observe: 'response' });
  }

  findCommunesByProvince(idProvince: number): Observable<HttpResponse<ICommune[]>> {
    return this.http.get<ICommune[]>('api//admin/communes/all-by-province/?idProvince=' + idProvince, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ICommune[]>(this.adminResourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.adminResourceUrl}/${id}`, { observe: 'response' });
  }

  getAllNonPriseEnCompte(anneeId: number): Observable<EntityArrayResponseType> {
    const options = createRequestOption({ anneeId });
    return this.http.get<ICommune[]>(SERVER_API_URL + 'api/admin/' + '_not_prise_en_compte/communes', {
      params: options,
      observe: 'response'
    });
  }

  updateCommuneForCurrentYear(anneeId: number, communes: ICommune[], checked: boolean): Observable<HttpResponse<boolean>> {
    const options = createRequestOption({ anneeId, checked });
    return this.http.put<boolean>(SERVER_API_URL + 'api/admin/' + '_update_current_year/communes', communes, {
      params: options,
      observe: 'response'
    });
  }

  getAllEtaCommuneByYears(anneeId: number, req?: any): Observable<HttpResponse<IEtatCommune[]>> {
    const options = createRequestOption(req);
    return this.http.get<IEtatCommune[]>('/api/etat-communes?anneeId=' + anneeId, {
      params: options,
      observe: 'response'
    });
  }

  getAllEtaCommuneByYearAndProvince(anneeId: number, id: number, criteria: string, req?: any): Observable<HttpResponse<IEtatCommune[]>> {
    const options = createRequestOption(req);
    return this.http.get<IEtatCommune[]>('/api/etat-communes/by-province?anneeId=' + anneeId + '&id=' + id + '&criteria=' + criteria, {
      params: options,
      observe: 'response'
    });
  }

  removeAllFromYear(id: number): Observable<HttpResponse<any>> {
    return this.http.delete<any>(SERVER_API_URL + '/api/etat-communes/remove-commune-from-year?id=' + id, {
      observe: 'response'
    });
  }
}
