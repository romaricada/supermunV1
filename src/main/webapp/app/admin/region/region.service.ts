import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IRegion } from 'app/shared/model/region.model';

type EntityResponseType = HttpResponse<IRegion>;
type EntityArrayResponseType = HttpResponse<IRegion[]>;

@Injectable({ providedIn: 'root' })
export class RegionService {
  public adminResourceUrl = SERVER_API_URL + 'api/admin/regions';
  public portailResourceUrl = SERVER_API_URL + 'api/portail/regions';

  constructor(protected http: HttpClient) {}

  create(region: IRegion): Observable<EntityResponseType> {
    return this.http.post<IRegion>(this.adminResourceUrl, region, { observe: 'response' });
  }

  update(region: IRegion): Observable<EntityResponseType> {
    return this.http.put<IRegion>(this.adminResourceUrl, region, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IRegion>(`${this.portailResourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IRegion[]>(this.adminResourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.adminResourceUrl}/${id}`, { observe: 'response' });
  }
}
