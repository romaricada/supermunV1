import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IIndicateur } from 'app/shared/model/indicateur.model';

type EntityResponseType = HttpResponse<IIndicateur>;
type EntityArrayResponseType = HttpResponse<IIndicateur[]>;

@Injectable({ providedIn: 'root' })
export class IndicateurService {
  public adminResourceUrl = SERVER_API_URL + 'api/admin/indicateurs';
  public portailResourceUrl = SERVER_API_URL + 'api/portail/indicateurs';

  constructor(protected http: HttpClient) {}

  create(indicateur: IIndicateur): Observable<EntityResponseType> {
    return this.http.post<IIndicateur>(this.adminResourceUrl, indicateur, { observe: 'response' });
  }

  update(indicateur: IIndicateur): Observable<EntityResponseType> {
    return this.http.put<IIndicateur>(this.adminResourceUrl, indicateur, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IIndicateur>(`${this.portailResourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IIndicateur[]>(this.adminResourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.adminResourceUrl}/${id}`, { observe: 'response' });
  }

  updateAll(indicateurs: IIndicateur[]): Observable<HttpResponse<IIndicateur[]>> {
    return this.http.put<IIndicateur[]>('api/admin/indicateurs/update-list', indicateurs, { observe: 'response' });
  }
}
