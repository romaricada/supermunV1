import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IInformation } from 'app/shared/model/information.model';

type EntityResponseType = HttpResponse<IInformation>;
type EntityArrayResponseType = HttpResponse<IInformation[]>;

@Injectable({ providedIn: 'root' })
export class InformationService {
  public adminResourceUrl = SERVER_API_URL + 'api/admin/information';
  public portailResourceUrl = SERVER_API_URL + 'api/portail/information';

  constructor(protected http: HttpClient) {}

  create(information: IInformation): Observable<EntityResponseType> {
    return this.http.post<IInformation>(this.adminResourceUrl, information, { observe: 'response' });
  }

  update(information: IInformation): Observable<EntityResponseType> {
    return this.http.put<IInformation>(this.adminResourceUrl, information, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IInformation>(`${this.portailResourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IInformation[]>(this.adminResourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.adminResourceUrl}/${id}`, { observe: 'response' });
  }
}
