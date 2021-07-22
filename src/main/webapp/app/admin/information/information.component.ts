import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { Observable } from 'rxjs';
import { JhiAlertService, JhiEventManager, JhiParseLinks } from 'ng-jhipster';

import { IInformation, Information } from 'app/shared/model/information.model';
import { AccountService } from 'app/core/auth/account.service';

import { InformationService } from './information.service';
import { FormBuilder } from '@angular/forms';
import { MessageService } from 'primeng/api';

@Component({
  selector: 'jhi-information',
  templateUrl: './information.component.html',
  styleUrls: ['./information.component.scss'],
  providers: [MessageService]
})
export class InformationComponent implements OnInit, OnDestroy {
  currentAccount: any;
  informations: IInformation[] = [];
  information: IInformation;
  isSaving: boolean;
  editForm: any;

  constructor(
    protected informationService: InformationService,
    protected parseLinks: JhiParseLinks,
    protected accountService: AccountService,
    protected activatedRoute: ActivatedRoute,
    protected router: Router,
    protected jhiAlertService: JhiAlertService,
    private fb: FormBuilder,
    protected eventManager: JhiEventManager
  ) {}

  init() {
    this.editForm = this.fb.group({
      id: [],
      presentation: [],
      contact: [],
      deleted: []
    });
  }

  loadAll() {
    this.informationService.query().subscribe((res: HttpResponse<IInformation[]>) => {
      this.informations = res.body;
      if (this.informations.length > 0) {
        this.information = this.informations[0];
      }
    });
  }

  ngOnInit() {
    this.eventManager.broadcast({
      name: 'endpointChanged',
      content: 'User switch to portail !'
    });

    this.information = new Information();
    this.loadAll();
    this.accountService.identity().subscribe(account => {
      this.currentAccount = account;
    });
  }

  ngOnDestroy() {
    //  this.eventManager.destroy(this.eventSubscriber);
  }

  updateForm(information: IInformation) {
    this.editForm.patchValue({
      id: information.id,
      presentation: information.presentation,
      contact: information.contact,
      deleted: information.deleted
    });
  }

  edit(information) {
    this.information = information;
  }

  save() {
    this.isSaving = true;
    if (this.information.id !== undefined) {
      this.subscribeToSaveResponse(this.informationService.update(this.information));
    } else {
      this.subscribeToSaveResponse(this.informationService.create(this.information));
    }
    this.information = new Information();
  }

  private createFromForm(): IInformation {
    return {
      ...new Information(),
      id: this.editForm.get(['id']).value,
      presentation: this.editForm.get(['presentation']).value,
      contact: this.editForm.get(['contact']).value,
      deleted: this.editForm.get(['deleted']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IInformation>>) {
    result.subscribe(() => this.onSaveSuccess(), () => this.onSaveError());
  }

  protected onSaveSuccess() {
    this.isSaving = false;
    this.loadAll();
  }

  protected onSaveError() {
    this.isSaving = false;
  }

  annulerSaisie() {
    this.init();
    this.information = new Information();
  }
}
