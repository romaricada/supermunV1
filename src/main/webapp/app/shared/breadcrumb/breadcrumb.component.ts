import { Component, Input, OnInit } from '@angular/core';
import { AccountService } from 'app/core/auth/account.service';

@Component({
  selector: 'jhi-breadcrumb',
  templateUrl: './breadcrumb.component.html'
})
export class BreadcrumbComponent implements OnInit {
  @Input()
  breadCrumb: string;
  @Input()
  title: string;
  constructor(private accountService: AccountService) {}

  ngOnInit() {}

  isAuthenticated() {
    return this.accountService.isAuthenticated();
  }
}
