import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IModalite } from 'app/shared/model/modalite.model';

type EntityResponseType = HttpResponse<IModalite>;
type EntityArrayResponseType = HttpResponse<IModalite[]>;

@Injectable({ providedIn: 'root' })
export class ModaliteService {
  public adminResourceUrl = SERVER_API_URL + 'api/admin/modalites';
  public portailResourceUrl = SERVER_API_URL + 'api/portail/modalites';

  constructor(protected http: HttpClient) {}

  create(modalite: IModalite): Observable<EntityResponseType> {
    return this.http.post<IModalite>(this.adminResourceUrl, modalite, { observe: 'response' });
  }

  update(modalite: IModalite): Observable<EntityResponseType> {
    return this.http.put<IModalite>(this.adminResourceUrl, modalite, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IModalite>(`${this.portailResourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IModalite[]>(this.adminResourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.adminResourceUrl}/${id}`, { observe: 'response' });
  }
}
