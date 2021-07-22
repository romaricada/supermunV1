import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { ITypeIndicateur } from 'app/shared/model/type-indicateur.model';

type EntityResponseType = HttpResponse<ITypeIndicateur>;
type EntityArrayResponseType = HttpResponse<ITypeIndicateur[]>;

@Injectable({ providedIn: 'root' })
export class TypeIndicateurService {
  public adminResourceUrl = SERVER_API_URL + 'api/admin/type-indicateurs';
  public portailResourceUrl = SERVER_API_URL + 'api/portail/type-indicateurs';

  constructor(protected http: HttpClient) {}

  create(typeIndicateur: ITypeIndicateur): Observable<EntityResponseType> {
    return this.http.post<ITypeIndicateur>(this.adminResourceUrl, typeIndicateur, { observe: 'response' });
  }

  update(typeIndicateur: ITypeIndicateur): Observable<EntityResponseType> {
    return this.http.put<ITypeIndicateur>(this.adminResourceUrl, typeIndicateur, { observe: 'response' });
  }

  deleteAll(typeIndicateurs: ITypeIndicateur[]): Observable<HttpResponse<number>> {
    return this.http.put<number>('api/admin/type-indicateurs/update-list', typeIndicateurs, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ITypeIndicateur>(`${this.portailResourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ITypeIndicateur[]>(this.adminResourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.adminResourceUrl}/${id}`, { observe: 'response' });
  }
}
