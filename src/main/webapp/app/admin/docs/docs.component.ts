import { Component } from '@angular/core';
import { JhiEventManager } from 'ng-jhipster';

@Component({
  selector: 'jhi-docs',
  templateUrl: './docs.component.html'
})
export class JhiDocsComponent {
  constructor(private eventManager: JhiEventManager) {
    this.eventManager.broadcast({
      name: 'endpointChanged',
      content: 'User switch to portail !'
    });
  }
}
