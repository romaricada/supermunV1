import { AfterViewInit, Component, ElementRef, OnInit, Renderer } from '@angular/core';
import { Router } from '@angular/router';
import { AccountService } from 'app/core/auth/account.service';
import { StateStorageService } from 'app/core/auth/state-storage.service';
import { LoginModalService } from 'app/core/login/login-modal.service';
import { LoginService } from 'app/core/login/login.service';
import { Account } from 'app/core/user/account.model';
import { JhiEventManager } from 'ng-jhipster';
import { Subscription } from 'rxjs';

@Component({
  selector: 'jhi-admin-home',
  templateUrl: './admin-home.component.html',
  styleUrls: ['./admin-home.component.scss']
})
export class AdminHomeComponent implements AfterViewInit, OnInit {
  account: Account;
  authenticationError: boolean;
  password: string;
  rememberMe: boolean;
  username: string;
  credentials: any;
  subscription: Subscription;
  passvisible = false;
  descTitle = '';
  descType = '';

  constructor(
    private accountService: AccountService,
    private loginModalService: LoginModalService,
    private eventManager: JhiEventManager,
    private loginService: LoginService,
    private stateStorageService: StateStorageService,
    private elementRef: ElementRef,
    private renderer: Renderer,
    private router: Router
  ) {
    this.credentials = {};
  }

  ngOnInit() {
    this.eventManager.broadcast({
      name: 'endpointChanged',
      content: 'User switch to portail !'
    });

    this.registerAuthentificationSuccess();
  }

  ngAfterViewInit() {
    setTimeout(() => this.renderer.invokeElementMethod(this.elementRef.nativeElement.querySelector('#username'), 'focus', []), 0);
  }

  isAuthenticated() {
    return this.accountService.isAuthenticated();
  }

  login() {
    this.loginService
      .login({
        username: this.username,
        password: this.password,
        rememberMe: this.rememberMe
      })
      .subscribe(
        () => {
          this.authenticationError = false;
          if (
            this.router.url === '/admin/register' ||
            this.router.url.startsWith('/admin/activate') ||
            this.router.url.startsWith('/admin/reset/')
          ) {
            this.router.navigate(['/admin']);
          }

          this.eventManager.broadcast({
            name: 'authenticationSuccess',
            content: 'Sending Authentication Success'
          });

          // previousState was set in the authExpiredInterceptor before being redirected to login modal.
          // since login is successful, go to stored previousState and clear previousState
          const redirect = this.stateStorageService.getUrl();
          if (redirect) {
            this.stateStorageService.storeUrl(null);
            this.router.navigate([redirect]);
          }
        },
        () => (this.authenticationError = true)
      );
  }

  register() {
    this.router.navigate(['/admin/register']);
  }

  requestResetPassword() {
    this.router.navigate(['/admin/reset', 'request']);
  }

  registerAuthentificationSuccess() {
    this.subscription = this.eventManager.subscribe('authenticationSuccess', () => {
      this.accountService.identity().subscribe((account: Account) => {
        this.account = account;
      });
    });
  }

  handleVisibility() {
    this.passvisible = !this.passvisible;
  }

  getDecrption(title, type) {
    this.descTitle = title;
    this.descType = type;
  }

  resetvalue() {
    this.descTitle = '';
    this.descType = '';
  }
}
