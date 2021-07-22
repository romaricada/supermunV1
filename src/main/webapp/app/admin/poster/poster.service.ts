import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IPoster } from 'app/shared/model/poster.model';

type EntityResponseType = HttpResponse<IPoster>;
type EntityArrayResponseType = HttpResponse<IPoster[]>;

@Injectable({ providedIn: 'root' })
export class PosterService {
  public adminResourceUrl = SERVER_API_URL + 'api/admin/posters';
  public portailResourceUrl = SERVER_API_URL + 'api/portail/posters';

  constructor(protected http: HttpClient) {}

  create(poster: IPoster): Observable<EntityResponseType> {
    return this.http.post<IPoster>(this.adminResourceUrl, poster, { observe: 'response' });
  }

  update(poster: IPoster): Observable<EntityResponseType> {
    return this.http.put<IPoster>(this.adminResourceUrl, poster, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IPoster>(`${this.portailResourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IPoster[]>(this.adminResourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.adminResourceUrl}/${id}`, { observe: 'response' });
  }
}
