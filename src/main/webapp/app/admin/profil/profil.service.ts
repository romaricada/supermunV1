import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IProfil } from 'app/shared/model/profil.model';

type EntityResponseType = HttpResponse<IProfil>;
type EntityArrayResponseType = HttpResponse<IProfil[]>;

@Injectable({ providedIn: 'root' })
export class ProfilService {
  public resourceUrl = SERVER_API_URL + 'api/profils';

  constructor(protected http: HttpClient) {}

  create(profil: IProfil): Observable<EntityResponseType> {
    return this.http.post<IProfil>(this.resourceUrl, profil, { observe: 'response' });
  }

  update(profil: IProfil): Observable<EntityResponseType> {
    return this.http.put<IProfil>(this.resourceUrl, profil, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IProfil>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IProfil[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}
