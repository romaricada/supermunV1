import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IPublication } from 'app/shared/model/publication.model';

type EntityResponseType = HttpResponse<IPublication>;
type EntityArrayResponseType = HttpResponse<IPublication[]>;

@Injectable({ providedIn: 'root' })
export class PublicationService {
  public adminrResourceUrl = SERVER_API_URL + 'api/admin/publications';
  public portailResourceUrl = SERVER_API_URL + 'api/portail/publications';

  constructor(protected http: HttpClient) {}

  create(publication: IPublication): Observable<EntityResponseType> {
    return this.http.post<IPublication>(this.adminrResourceUrl, publication, { observe: 'response' });
  }

  update(publication: IPublication): Observable<EntityResponseType> {
    return this.http.put<IPublication>(this.adminrResourceUrl, publication, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IPublication>(`${this.portailResourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IPublication[]>(this.adminrResourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.adminrResourceUrl}/${id}`, { observe: 'response' });
  }
}
