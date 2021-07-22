import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { filter, map } from 'rxjs/operators';
import { JhiEventManager } from 'ng-jhipster';

import { Dictionaires, IDictionaires } from 'app/shared/model/dictionaires.model';
import { AccountService } from 'app/core/auth/account.service';
import { DictionairesService } from './dictionaires.service';
import { ConfirmationService, MessageService } from 'primeng/api';

@Component({
  selector: 'jhi-dictionaires',
  templateUrl: './dictionaires.component.html',
  styleUrls: ['./dictionaires.component.scss']
})
export class DictionairesComponent implements OnInit, OnDestroy {
  dictionaires: IDictionaires[] = [];
  dictionaire: IDictionaires;
  currentAccount: any;
  eventSubscriber: Subscription;
  display: boolean;

  constructor(
    protected dictionairesService: DictionairesService,
    protected eventManager: JhiEventManager,
    protected accountService: AccountService,
    protected confirmationService: ConfirmationService,
    protected messageService: MessageService
  ) {}

  loadAll() {
    this.dictionairesService
      .query()
      .pipe(
        filter((res: HttpResponse<IDictionaires[]>) => res.ok),
        map((res: HttpResponse<IDictionaires[]>) => res.body)
      )
      .subscribe((res: IDictionaires[]) => {
        this.dictionaires = res;
      });
  }

  ngOnInit() {
    this.dictionaire = new Dictionaires();
    this.loadAll();
    this.accountService.identity().subscribe(account => {
      this.currentAccount = account;
    });
    this.registerChangeInDictionaires();
  }

  creeEntite() {
    if (this.display) {
      this.dictionaire = new Dictionaires();
      this.display = false;
    } else {
      this.dictionaire = new Dictionaires();
      this.display = true;
    }
  }

  iniElement() {
    this.dictionaire = new Dictionaires();
  }

  updateEntite(dictionaire: IDictionaires) {
    if (this.display) {
      this.dictionaire = new Dictionaires();
      this.display = false;
    } else {
      if (dictionaire) {
        this.dictionaire = dictionaire;
        this.display = true;
      }
    }
  }

  save() {
    if (this.dictionaire.id !== undefined) {
      this.dictionairesService.update(this.dictionaire).subscribe(() => {
        this.loadAll();
        this.iniElement();
        this.display = false;
        this.showMessage('success', 'Modification', 'modification effectué avec succès !');
      });
    } else {
      this.dictionairesService.create(this.dictionaire).subscribe(() => {
        this.loadAll();
        this.iniElement();
        this.display = false;
        this.showMessage('success', 'Enregitrement', 'Enregistrement effectué avec succès !');
      });
    }
  }

  ngOnDestroy() {
    this.eventManager.destroy(this.eventSubscriber);
  }

  deleteElement(dictionaire: IDictionaires) {
    this.confirmationService.confirm({
      header: 'Confirmation',
      message: 'Etes-vous sûr de vouloir supprimer ?',
      accept: () => {
        if (dictionaire === null) {
          return;
        } else {
          this.dictionairesService.delete(dictionaire.id).subscribe(() => {
            this.loadAll();
            this.showMessage('success', 'SUPPRESSION', 'Suppression effectuée avec succès !');
          });
        }
      }
    });
  }

  showMessage(sever: string, sum: string, det: string) {
    this.messageService.add({
      severity: sever,
      summary: sum,
      detail: det
    });
  }

  trackId(index: number, item: IDictionaires) {
    return item.id;
  }

  registerChangeInDictionaires() {
    this.eventSubscriber = this.eventManager.subscribe('dictionairesListModification', response => this.loadAll());
  }
}
