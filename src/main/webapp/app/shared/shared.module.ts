import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { BreadcrumbComponent } from 'app/shared/breadcrumb/breadcrumb.component';
import { TableGlobalSearchDirective } from 'app/shared/directives/table-global-search.directive';
import { JhMaterialModule } from 'app/shared/jh-material.module';
import { PasswordStrengthBarComponent } from 'app/shared/password-check/password-strength-bar.component';
import { Ng5SliderModule } from 'ng5-slider';
import { DropdownModule } from 'primeng/dropdown';
import { InputTextareaModule } from 'primeng/inputtextarea';
import { ListboxModule } from 'primeng/listbox';
import {
  AccordionModule,
  AutoCompleteModule,
  ButtonModule,
  CalendarModule,
  CardModule,
  ChartModule,
  CheckboxModule,
  ColorPickerModule,
  ConfirmationService,
  ConfirmDialogModule,
  DialogModule,
  EditorModule,
  FieldsetModule,
  InputSwitchModule,
  InputTextModule,
  KeyFilterModule,
  MenubarModule,
  MessageService,
  MessagesModule,
  MultiSelectModule,
  OverlayPanelModule,
  PaginatorModule,
  PanelMenuModule,
  PanelModule,
  ProgressBarModule,
  RadioButtonModule,
  SliderModule,
  SpinnerModule,
  SplitButtonModule,
  TabViewModule,
  ToggleButtonModule,
  ToolbarModule,
  TooltipModule
} from 'primeng/primeng';
import { ProgressSpinnerModule } from 'primeng/progressspinner';
import { RatingModule } from 'primeng/rating';
import { ScrollPanelModule } from 'primeng/scrollpanel';
import { TableModule } from 'primeng/table';
import { ToastModule } from 'primeng/toast';
import { JhiAlertErrorComponent } from './alert/alert-error.component';
import { JhiAlertComponent } from './alert/alert.component';
import { HasAnyAuthorityDirective } from './auth/has-any-authority.directive';
import { JhiLoginModalComponent } from './login/login.component';
import { SupermunSharedLibsModule } from './shared-libs.module';

@NgModule({
  imports: [
    JhMaterialModule,
    SupermunSharedLibsModule,
    ListboxModule,
    DropdownModule,
    TableModule,
    ToolbarModule,
    InputTextModule,
    SplitButtonModule,
    PaginatorModule,
    DialogModule,
    ToggleButtonModule,
    ButtonModule,
    MultiSelectModule,
    CheckboxModule,
    AutoCompleteModule,
    MessagesModule,
    InputSwitchModule,
    KeyFilterModule,
    TooltipModule,
    CalendarModule,
    TabViewModule,
    FormsModule,
    PanelMenuModule,
    ToastModule,
    SliderModule,
    ProgressSpinnerModule,
    ColorPickerModule,
    FieldsetModule,
    ConfirmDialogModule,
    ChartModule,
    CardModule,
    RadioButtonModule,
    InputTextareaModule,
    AccordionModule,
    RatingModule,
    Ng5SliderModule,
    ProgressBarModule,
    ScrollPanelModule,
    MenubarModule,
    PanelModule,
    EditorModule,
    SpinnerModule,
    OverlayPanelModule
  ],
  declarations: [
    JhiAlertComponent,
    JhiAlertErrorComponent,
    JhiLoginModalComponent,
    BreadcrumbComponent,
    HasAnyAuthorityDirective,
    TableGlobalSearchDirective,
    PasswordStrengthBarComponent
  ],
  entryComponents: [JhiLoginModalComponent],
  providers: [ConfirmationService, MessageService],
  exports: [
    JhMaterialModule,
    SupermunSharedLibsModule,
    JhiAlertComponent,
    JhiAlertErrorComponent,
    JhiLoginModalComponent,
    PasswordStrengthBarComponent,
    HasAnyAuthorityDirective,
    TableGlobalSearchDirective,
    BreadcrumbComponent,
    ListboxModule,
    DropdownModule,
    TableModule,
    ToolbarModule,
    InputTextModule,
    SplitButtonModule,
    PaginatorModule,
    DialogModule,
    ToggleButtonModule,
    ButtonModule,
    MultiSelectModule,
    CheckboxModule,
    AutoCompleteModule,
    MessagesModule,
    InputSwitchModule,
    KeyFilterModule,
    TooltipModule,
    CalendarModule,
    TabViewModule,
    FormsModule,
    PanelMenuModule,
    ToastModule,
    SliderModule,
    ProgressSpinnerModule,
    ColorPickerModule,
    FieldsetModule,
    ConfirmDialogModule,
    ChartModule,
    CardModule,
    RadioButtonModule,
    InputTextareaModule,
    AccordionModule,
    RatingModule,
    Ng5SliderModule,
    ProgressBarModule,
    ScrollPanelModule,
    SliderModule,
    MenubarModule,
    PanelModule,
    EditorModule,
    SpinnerModule,
    OverlayPanelModule
  ]
})
export class SupermunSharedModule {}
