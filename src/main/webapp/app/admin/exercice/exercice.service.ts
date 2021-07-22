import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IExercice } from 'app/shared/model/exercice.model';

type EntityResponseType = HttpResponse<IExercice>;
type EntityArrayResponseType = HttpResponse<IExercice[]>;

@Injectable({ providedIn: 'root' })
export class ExerciceService {
  public adminResourceUrl = SERVER_API_URL + 'api/admin/exercices';
  public portailResourceUrl = SERVER_API_URL + 'api/portail/exercices';

  constructor(protected http: HttpClient) {}

  create(exercice: IExercice): Observable<EntityResponseType> {
    return this.http.post<IExercice>(this.adminResourceUrl, exercice, { observe: 'response' });
  }

  update(exercice: IExercice): Observable<EntityResponseType> {
    return this.http.put<IExercice>(this.adminResourceUrl, exercice, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IExercice>(`${this.portailResourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IExercice[]>(this.adminResourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.adminResourceUrl}/${id}`, { observe: 'response' });
  }
}
