import { Component, OnInit } from '@angular/core';
import { IPublication, Publication } from 'app/shared/model/publication.model';
import { PortailService } from 'app/portail/portailServices/portail.service';
import { JhiDataUtils, JhiParseLinks } from 'ng-jhipster';
import { AccountService } from 'app/core/auth/account.service';
import { ActivatedRoute, Router } from '@angular/router';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { saveAs } from 'file-saver';
import { ITypePublication } from 'app/shared/model/type-publication.model';

@Component({
  selector: 'jhi-publication',
  templateUrl: './publication.component.html',
  styleUrls: ['./publication.component.scss']
})
export class PublicationComponent implements OnInit {
  publications: IPublication[] = [];
  publication: IPublication;
  publicationTemps: IPublication[] = [];

  typepublications: ITypePublication[] = [];
  selectedTypePublication: ITypePublication;

  routeData: any;
  links: any;
  totalItems: any;
  itemsPerPage: any;
  page: any;
  predicate: any;
  previousPage: any;
  reverse: any;

  afficheDialog: boolean;

  constructor(
    protected portailService: PortailService,
    protected parseLinks: JhiParseLinks,
    protected accountService: AccountService,
    protected activatedRoute: ActivatedRoute,
    protected dataUtils: JhiDataUtils,
    protected router: Router
  ) {
    this.itemsPerPage = 9;
    this.routeData = this.activatedRoute.data.subscribe(data => {
      this.page = data.pagingParams.page;
      this.previousPage = data.pagingParams.page;
      this.reverse = data.pagingParams.ascending;
      this.predicate = data.pagingParams.predicate;
    });
  }

  loadAll() {
    this.portailService
      .findAlPublication({
        page: this.page - 1,
        size: this.itemsPerPage,
        sort: this.sort()
      })
      .subscribe((res: HttpResponse<IPublication[]>) => {
        this.publicationTemps = res.body.filter(value => value.published);
        this.paginatePublications(this.publicationTemps, res.headers);
      });
  }

  loadAllTypeCategorie() {
    this.portailService.findAllTypePublication().subscribe((res: HttpResponse<ITypePublication[]>) => {
      this.typepublications = res.body;
    });
  }

  loadPage(page: number) {
    if (page !== this.previousPage) {
      this.previousPage = page;
      this.transition();
    }
  }

  transition() {
    this.router.navigate(['/portail/publicationDoc'], {
      queryParams: {
        page: this.page,
        size: this.itemsPerPage,
        sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
      }
    });
    this.loadAll();
  }

  ngOnInit() {
    this.publication = new Publication();
    this.selectedTypePublication = null;
    this.loadAll();
    this.loadAllTypeCategorie();
  }

  onTypePublicationChange() {
    if (this.selectedTypePublication !== null) {
      this.publications = this.publicationTemps.filter(value => value.typePublicationId === this.selectedTypePublication.id);
    } else {
      this.publications = this.publicationTemps;
    }
  }

  trackId(index: number, item: IPublication) {
    return item.id;
  }

  byteSize(field) {
    return this.dataUtils.byteSize(field);
  }

  openFile(contentType, field) {
    return this.dataUtils.openFile(contentType, field);
  }

  protected paginatePublications(data: IPublication[], headers: HttpHeaders) {
    this.links = this.parseLinks.parse(headers.get('link'));
    this.totalItems = parseInt(headers.get('X-Total-Count'), 10);
    this.publications = data;
  }

  sort() {
    const result = [this.predicate + ',' + (this.reverse ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  VoirFile(publication: IPublication) {
    if (publication.contenuContentType !== null) {
      this.portailService.telechargerPublication(publication.id).subscribe(response => {
        const doctType = publication.contenuContentType;
        window.open(URL.createObjectURL(new Blob([response], { type: doctType })), '_blank');
      });
    }
  }

  downloadFile(publication: IPublication) {
    if (publication.contenuContentType !== null) {
      this.portailService.telechargerPublication(publication.id).subscribe(response => {
        const doctType = publication.contenuContentType;
        const blob = new Blob([response], { type: doctType });
        saveAs(blob, publication.libelle);
      });
    }
  }

  displayDialog(publication: IPublication) {
    if (this.afficheDialog) {
      this.publication = new Publication();
      this.afficheDialog = false;
    } else {
      this.publication = publication;
      this.afficheDialog = true;
    }
  }
}
