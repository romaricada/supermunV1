import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { ICounte } from 'app/shared/model/counte.model';

type EntityResponseType = HttpResponse<ICounte>;
type EntityArrayResponseType = HttpResponse<ICounte[]>;

@Injectable({ providedIn: 'root' })
export class CounteService {
  public resourceUrl = SERVER_API_URL + 'api/countes';

  constructor(protected http: HttpClient) {}

  create(counte: ICounte): Observable<EntityResponseType> {
    return this.http.post<ICounte>(this.resourceUrl, counte, { observe: 'response' });
  }

  update(counte: ICounte): Observable<EntityResponseType> {
    return this.http.put<ICounte>(this.resourceUrl, counte, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ICounte>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ICounte[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}
