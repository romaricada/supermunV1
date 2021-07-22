import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { ITypePublication } from 'app/shared/model/type-publication.model';

type EntityResponseType = HttpResponse<ITypePublication>;
type EntityArrayResponseType = HttpResponse<ITypePublication[]>;

@Injectable({ providedIn: 'root' })
export class TypePublicationService {
  public resourceUrl = SERVER_API_URL + 'api/admin/type-publications';

  constructor(protected http: HttpClient) {}

  create(typePublication: ITypePublication): Observable<EntityResponseType> {
    return this.http.post<ITypePublication>(this.resourceUrl, typePublication, { observe: 'response' });
  }

  update(typePublication: ITypePublication): Observable<EntityResponseType> {
    return this.http.put<ITypePublication>(this.resourceUrl, typePublication, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ITypePublication>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ITypePublication[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}
