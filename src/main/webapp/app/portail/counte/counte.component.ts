import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { filter, map } from 'rxjs/operators';
import { JhiEventManager } from 'ng-jhipster';

import { ICounte } from 'app/shared/model/counte.model';
import { AccountService } from 'app/core/auth/account.service';
import { CounteService } from './counte.service';

@Component({
  selector: 'jhi-counte',
  templateUrl: './counte.component.html'
})
export class CounteComponent implements OnInit, OnDestroy {
  countes: ICounte[];
  currentAccount: any;
  eventSubscriber: Subscription;

  constructor(protected counteService: CounteService, protected eventManager: JhiEventManager, protected accountService: AccountService) {}

  loadAll() {
    this.counteService
      .query()
      .pipe(
        filter((res: HttpResponse<ICounte[]>) => res.ok),
        map((res: HttpResponse<ICounte[]>) => res.body)
      )
      .subscribe((res: ICounte[]) => {
        this.countes = res;
      });
  }

  ngOnInit() {
    this.loadAll();
    this.accountService.identity().subscribe(account => {
      this.currentAccount = account;
    });
    this.registerChangeInCountes();
  }

  ngOnDestroy() {
    this.eventManager.destroy(this.eventSubscriber);
  }

  trackId(index: number, item: ICounte) {
    return item.id;
  }

  registerChangeInCountes() {
    this.eventSubscriber = this.eventManager.subscribe('counteListModification', response => this.loadAll());
  }
}
