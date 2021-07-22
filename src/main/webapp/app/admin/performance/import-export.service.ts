import { HttpClient, HttpHeaders, HttpResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { SERVER_API_URL } from 'app/app.constants';
import { Extension } from 'app/shared/model/extension.enum';
import { FileType } from 'app/shared/model/file-type.enum';
import { createRequestOption } from 'app/shared/util/request-util';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ImportExportService {
  public exportResourceUrl = SERVER_API_URL + 'api/admin/export';
  public importResourceUrl = SERVER_API_URL + 'api/admin/import';

  constructor(protected http: HttpClient) {}

  exportData(
    typeId: number,
    ext: Extension,
    fileType: FileType,
    anneeId?: number,
    regionId?: number,
    provinceId?: number
  ): Observable<HttpResponse<ArrayBuffer>> {
    let header = new HttpHeaders();

    if (ext === Extension.CSV) {
      header = header.append('Accept', 'text/csv; charset=utf-8');
    } else {
      header = header.append('Accept', 'application/vnd.ms.excel; charset=utf-8');
    }

    let options: any = createRequestOption({ typeId, fileType, ext });
    if (anneeId) {
      options = createRequestOption({ typeId, anneeId, fileType, ext });
      if (regionId && provinceId) {
        options = createRequestOption({ typeId, anneeId, regionId, provinceId, fileType, ext });
      } else if (regionId) {
        options = createRequestOption({ typeId, anneeId, regionId, fileType, ext });
      } else if (provinceId) {
        options = createRequestOption({ typeId, anneeId, provinceId, fileType, ext });
      }
    } else {
      options = createRequestOption({ typeId, fileType, ext });
      if (regionId && provinceId) {
        options = createRequestOption({ typeId, regionId, provinceId, fileType, ext });
      } else if (regionId) {
        options = createRequestOption({ typeId, regionId, fileType, ext });
      } else if (provinceId) {
        options = createRequestOption({ typeId, provinceId, fileType, ext });
      }
    }

    return this.http.get(this.exportResourceUrl, {
      params: options,
      headers: header,
      observe: 'response',

      responseType: 'arraybuffer'
    });
  }

  importData(anneeId: number, typeId: number, file: File, ext: Extension, update: boolean): Observable<HttpResponse<any>> {
    const formData = new FormData();
    formData.append('file', file, file.name);
    let header = new HttpHeaders();
    if (ext === Extension.CSV) {
      header = header.append('Accept', 'text/csv; charset=utf-8');
    } else {
      header = header.append('Accept', 'application/vnd.ms.excel; charset=utf-8');
    }
    const options = createRequestOption({ anneeId, typeId, ext, update });
    return this.http.post<string>(this.importResourceUrl, formData, {
      headers: header,
      params: options,
      observe: 'response'
    });
  }
}
