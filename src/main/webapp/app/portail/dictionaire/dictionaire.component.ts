import { Component, OnInit } from '@angular/core';
import { IDictionaires } from 'app/shared/model/dictionaires.model';
import { PortailService } from 'app/portail/portailServices/portail.service';
import { HttpResponse } from '@angular/common/http';

@Component({
  selector: 'jhi-dictionaire',
  templateUrl: './dictionaire.component.html',
  styleUrls: ['./dictionaire.component.scss']
})
export class DictionaireComponent implements OnInit {
  dictionaires: IDictionaires[] = [];
  dictionairesTemp: IDictionaires[] = [];
  isLoading: boolean;
  constructor(protected portailSercie: PortailService) {}

  ngOnInit() {
    this.loadAllDictionaire();
  }

  loadAllDictionaire() {
    this.isLoading = true;
    this.portailSercie.findAllDictionaire().subscribe((res: HttpResponse<IDictionaires[]>) => {
      this.isLoading = false;
      this.dictionairesTemp = res.body;
      this.dictionaires = res.body;
    });
  }
  doSearch(event: any) {
    this.dictionaires = event;
  }
}
