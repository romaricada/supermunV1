import { Component, OnInit } from '@angular/core';
import { PortailService } from 'app/portail/portailServices/portail.service';
import { IInformation, Information } from 'app/shared/model/information.model';
import { HttpResponse } from '@angular/common/http';

@Component({
  selector: 'jhi-donnee',
  templateUrl: './apropos.component.html',
  styleUrls: ['./apropos.component.scss']
})
export class AproposComponent implements OnInit {
  informations: IInformation[] = [];
  information: IInformation;
  constructor(protected portailService: PortailService) {}

  loadAllInformation() {
    this.portailService.findAllInformation().subscribe((res: HttpResponse<IInformation[]>) => {
      this.informations = res.body;
      this.information = this.informations[0];
    });
  }
  ngOnInit() {
    this.information = new Information();
    this.loadAllInformation();
  }
}
