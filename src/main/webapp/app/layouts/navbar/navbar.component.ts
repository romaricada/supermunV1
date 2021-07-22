import { Component, OnDestroy, OnInit } from '@angular/core';
import { Router } from '@angular/router';

import { VERSION } from 'app/app.constants';
import { AccountService } from 'app/core/auth/account.service';
import { LoginModalService } from 'app/core/login/login-modal.service';
import { LoginService } from 'app/core/login/login.service';
import { Account } from 'app/core/user/account.model';
import { ProfileService } from 'app/layouts/profiles/profile.service';
import { JhiEventManager } from 'ng-jhipster';
import { Subscription } from 'rxjs';

@Component({
  selector: 'jhi-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['navbar.scss']
})
export class NavbarComponent implements OnInit, OnDestroy {
  inProduction: boolean;
  isNavbarCollapsed: boolean;
  swaggerEnabled: boolean;
  version: string;
  account: Account;
  eventSubscriber: Subscription;

  constructor(
    private loginService: LoginService,
    private accountService: AccountService,
    private loginModalService: LoginModalService,
    private profileService: ProfileService,
    private router: Router,
    private eventManager: JhiEventManager
  ) {
    this.version = VERSION ? (VERSION.toLowerCase().startsWith('v') ? VERSION : 'v' + VERSION) : '';
    this.isNavbarCollapsed = true;
  }

  ngOnInit() {
    this.accountService.identity().subscribe((account: Account) => {
      this.account = account;
    });
    this.profileService.getProfileInfo().subscribe(profileInfo => {
      this.inProduction = profileInfo.inProduction;
      this.swaggerEnabled = profileInfo.swaggerEnabled;
    });
    this.registerEndpointChanged();
  }

  ngOnDestroy() {
    this.eventManager.destroy(this.eventSubscriber);
  }

  registerEndpointChanged() {
    this.eventSubscriber = this.eventManager.subscribe('endpointChanged', () => {
      this.ischeckIfEndpointChanged();
    });
  }

  ischeckIfEndpointChanged(): boolean {
    return this.router.url.includes('/admin');
  }

  collapseNavbar() {
    this.isNavbarCollapsed = true;
  }

  isAuthenticated() {
    return this.accountService.isAuthenticated();
  }

  login() {
    this.router.navigate(['/admin']);
  }

  logout() {
    this.collapseNavbar();
    this.loginService.logout();
    this.router.navigate(['/admin']);
  }

  toggleNavbar() {
    this.isNavbarCollapsed = !this.isNavbarCollapsed;
  }
}
