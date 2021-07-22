import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IDomaine } from 'app/shared/model/domaine.model';

type EntityResponseType = HttpResponse<IDomaine>;
type EntityArrayResponseType = HttpResponse<IDomaine[]>;

@Injectable({ providedIn: 'root' })
export class DomaineService {
  public adminResourceUrl = SERVER_API_URL + 'api/admin/domaines';
  public portailResourceUrl = SERVER_API_URL + 'api/portail/domaines';

  constructor(protected http: HttpClient) {}

  create(domaine: IDomaine): Observable<EntityResponseType> {
    return this.http.post<IDomaine>(this.adminResourceUrl, domaine, { observe: 'response' });
  }

  update(domaine: IDomaine): Observable<EntityResponseType> {
    return this.http.put<IDomaine>(this.adminResourceUrl, domaine, { observe: 'response' });
  }

  deleteAll(domaines: IDomaine[]): Observable<HttpResponse<number>> {
    return this.http.put<number>('api/admin/domaines/update-list', domaines, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IDomaine>(`${this.portailResourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IDomaine[]>(this.adminResourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.adminResourceUrl}/${id}`, { observe: 'response' });
  }
}
