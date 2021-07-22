import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { ICouleur } from 'app/shared/model/couleur.model';

type EntityResponseType = HttpResponse<ICouleur>;
type EntityArrayResponseType = HttpResponse<ICouleur[]>; // /admin/couleurs/update-list

@Injectable({ providedIn: 'root' })
export class CouleurService {
  public adminResourceUrl = SERVER_API_URL + 'api/admin/couleurs';
  public portailResourceUrl = SERVER_API_URL + 'api/portail/couleurs';

  constructor(protected http: HttpClient) {}

  create(couleur: ICouleur): Observable<EntityResponseType> {
    return this.http.post<ICouleur>(this.adminResourceUrl, couleur, { observe: 'response' });
  }

  update(couleur: ICouleur): Observable<EntityResponseType> {
    return this.http.put<ICouleur>(this.adminResourceUrl, couleur, { observe: 'response' });
  }

  createAll(couleurs: ICouleur[]): Observable<HttpResponse<number>> {
    return this.http.put<number>(this.adminResourceUrl + '/update-list', couleurs, { observe: 'response' });
  }

  updateAll(couleurs: ICouleur[]): Observable<HttpResponse<number>> {
    return this.http.put<number>('api/admin/couleurs/update-list', couleurs, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ICouleur>(`${this.portailResourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ICouleur[]>(this.adminResourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.adminResourceUrl}/${id}`, { observe: 'response' });
  }
}
