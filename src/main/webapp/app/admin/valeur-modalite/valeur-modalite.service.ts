import { HttpClient, HttpHeaders, HttpResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';

import { SERVER_API_URL } from 'app/app.constants';
import { IValeurModalite } from 'app/shared/model/valeur-modalite.model';
import { createRequestOption } from 'app/shared/util/request-util';
import { Observable } from 'rxjs';

type EntityResponseType = HttpResponse<IValeurModalite>;
type EntityArrayResponseType = HttpResponse<IValeurModalite[]>;

@Injectable({ providedIn: 'root' })
export class ValeurModaliteService {
  public adminResourceUrl = SERVER_API_URL + 'api/admin/valeur-modalites';
  public portailResourceUrl = SERVER_API_URL + 'api/portail/valeur-modalites';
  public importationResourceUrl = SERVER_API_URL + 'api/admin/importations';

  constructor(protected http: HttpClient) {}

  create(valeurModalite: IValeurModalite): Observable<EntityResponseType> {
    return this.http.post<IValeurModalite>(this.adminResourceUrl, valeurModalite, { observe: 'response' });
  }

  update(valeurModalite: IValeurModalite): Observable<EntityResponseType> {
    return this.http.put<IValeurModalite>(this.adminResourceUrl, valeurModalite, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IValeurModalite>(`${this.portailResourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IValeurModalite[]>(this.adminResourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.adminResourceUrl}/${id}`, { observe: 'response' });
  }

  getValeurModalitebyCommunebyExercice(idExercice: number, idCommune: number, req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IValeurModalite[]>(
      this.adminResourceUrl + '/all-by-exercice-commune?idExercice=' + idExercice + '&idCommune=' + idCommune,
      { params: options, observe: 'response' }
    );
  }

  exportationCSV(typeIndicateurId: number): Observable<HttpResponse<string>> {
    let header = new HttpHeaders();
    header = header.append('Accept', 'text/csv; charset=utf-8');
    return this.http.get(this.importationResourceUrl + '/export-csv?typeId=' + typeIndicateurId, {
      headers: header,
      observe: 'response',
      responseType: 'text'
    });
  }

  exportationExcel(typeIndicateurId: number): Observable<HttpResponse<ArrayBuffer>> {
    let header = new HttpHeaders();
    header = header.append('Accept', 'application/vnd.ms.excel; charset=utf-8');
    return this.http.get(this.importationResourceUrl + '/export-excel?typeId=' + typeIndicateurId, {
      headers: header,
      observe: 'response',
      responseType: 'arraybuffer'
    });
  }

  exportExcel(typeIndicateurId: number): Observable<HttpResponse<ArrayBuffer>> {
    let header = new HttpHeaders();
    header = header.append('Accept', 'application/vnd.ms.excel; charset=utf-8');
    return this.http.get('api/admin/export-excel-template?typeId=' + typeIndicateurId, {
      headers: header,
      observe: 'response',
      responseType: 'arraybuffer'
    });
  }

  exportExcelPerformance(typeId: number, anneeId: number, regionId?: number, provinceId?: number): Observable<HttpResponse<ArrayBuffer>> {
    let header = new HttpHeaders();
    header = header.append('Accept', 'application/vnd.ms.excel; charset=utf-8');
    let options: any = createRequestOption({ typeId, anneeId });
    if (regionId && provinceId) {
      options = createRequestOption({ typeId, anneeId, regionId, provinceId });
    } else if (regionId) {
      options = createRequestOption({ typeId, anneeId, regionId });
    } else if (provinceId) {
      options = createRequestOption({ typeId, anneeId, provinceId });
    }

    return this.http.get('api/admin/export-excel-performance', {
      params: options,
      headers: header,
      observe: 'response',
      responseType: 'arraybuffer'
    });
  }

  exportExcelScore(idCommune: number, idAnnee1: number, idAnnee2: number): Observable<HttpResponse<ArrayBuffer>> {
    const options = createRequestOption({ idCommune, idAnnee1, idAnnee2 });
    let header = new HttpHeaders();
    header = header.append('Accept', 'application/vnd.ms.excel; charset=utf-8');
    return this.http.get('api/portail/download-data-between-year', {
      headers: header,
      params: options,
      observe: 'response',
      responseType: 'arraybuffer'
    });
  }

  updloadCSVFile(exerciciceId: number, typeIndicateurId: number, file: File): Observable<HttpResponse<boolean>> {
    const options = createRequestOption({ exerciciceId, typeIndicateurId, file });
    return this.http.get<boolean>(this.importationResourceUrl + '/import-csv', {
      params: options,
      reportProgress: true,
      observe: 'response'
    });
  }

  updloadExcelFile(anneeId: number, typeId: number, file: File): Observable<HttpResponse<boolean>> {
    const formData = new FormData();
    formData.append('file', file, file.name);
    let header = new HttpHeaders();
    header = header.append('Accept', 'application/vnd.ms.excel; charset=utf-8').append('uploadType', 'multipart');
    const options = createRequestOption({ anneeId, typeId });
    return this.http.post<boolean>('api/admin/import-excel-template', formData, {
      headers: header,
      params: options,
      observe: 'response'
    });
  }
}
