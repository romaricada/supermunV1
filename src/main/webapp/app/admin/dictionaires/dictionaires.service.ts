import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IDictionaires } from 'app/shared/model/dictionaires.model';

type EntityResponseType = HttpResponse<IDictionaires>;
type EntityArrayResponseType = HttpResponse<IDictionaires[]>;

@Injectable({ providedIn: 'root' })
export class DictionairesService {
  public resourceUrl = SERVER_API_URL + 'api/dictionaires';

  constructor(protected http: HttpClient) {}

  create(dictionaires: IDictionaires): Observable<EntityResponseType> {
    return this.http.post<IDictionaires>(this.resourceUrl, dictionaires, { observe: 'response' });
  }

  update(dictionaires: IDictionaires): Observable<EntityResponseType> {
    return this.http.put<IDictionaires>(this.resourceUrl, dictionaires, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IDictionaires>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IDictionaires[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}
