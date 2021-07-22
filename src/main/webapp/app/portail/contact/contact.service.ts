import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Contact } from 'app/shared/model/contact';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ContactService {
  constructor(private http: HttpClient) {}

  sendMessage(contact: Contact): Observable<any> {
    return this.http.post('/api/portail/contact/informations', contact, { observe: 'response' });
  }
}
