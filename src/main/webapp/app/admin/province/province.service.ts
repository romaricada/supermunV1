import { HttpClient, HttpResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';

import { SERVER_API_URL } from 'app/app.constants';
import { IProvince } from 'app/shared/model/province.model';
import { createRequestOption } from 'app/shared/util/request-util';
import { Observable } from 'rxjs';

type EntityResponseType = HttpResponse<IProvince>;
type EntityArrayResponseType = HttpResponse<IProvince[]>;

@Injectable({ providedIn: 'root' })
export class ProvinceService {
  public adminResourceUrl = SERVER_API_URL + 'api/admin/provinces';
  public portailResourceUrl = SERVER_API_URL + 'api/portail/provinces';

  constructor(protected http: HttpClient) {}

  create(province: IProvince): Observable<EntityResponseType> {
    return this.http.post<IProvince>(this.adminResourceUrl, province, { observe: 'response' });
  }

  update(province: IProvince): Observable<EntityResponseType> {
    return this.http.put<IProvince>(this.adminResourceUrl, province, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IProvince>(`${this.portailResourceUrl}/${id}`, { observe: 'response' });
  }

  findProvinceByRegion(idRegion: number): Observable<EntityArrayResponseType> {
    return this.http.get<IProvince[]>('api/admin/provinces/all-by-region/?idRegion=' + idRegion, { observe: 'response' });
  }

  findAllCommuesByProvince(): Observable<EntityArrayResponseType> {
    return this.http.get<IProvince[]>('api/admin/provinces/all-communes-by-province', { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IProvince[]>(this.adminResourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.adminResourceUrl}/${id}`, { observe: 'response' });
  }
}
