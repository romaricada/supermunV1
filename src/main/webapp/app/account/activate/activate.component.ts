import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { LoginModalService } from 'app/core/login/login-modal.service';
import { JhiEventManager } from 'ng-jhipster';
import { flatMap } from 'rxjs/operators';
import { ActivateService } from './activate.service';

@Component({
  selector: 'jhi-activate',
  templateUrl: './activate.component.html'
})
export class ActivateComponent implements OnInit {
  error: string;
  success: string;

  constructor(
    private activateService: ActivateService,
    private loginModalService: LoginModalService,
    private route: ActivatedRoute,
    private eventManager: JhiEventManager
  ) {}

  ngOnInit() {
    this.eventManager.broadcast({
      name: 'endpointChanged',
      content: 'User switch to portail !'
    });

    this.route.queryParams.pipe(flatMap(params => this.activateService.get(params.key))).subscribe(
      () => {
        this.error = null;
        this.success = 'OK';
      },
      () => {
        this.success = null;
        this.error = 'ERROR';
      }
    );
  }

  login() {
    this.loginModalService.open();
  }
}
