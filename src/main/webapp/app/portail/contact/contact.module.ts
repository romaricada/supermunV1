import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { ContactComponent } from 'app/portail/contact/contact.component';
import { contactRoute } from 'app/portail/contact/contact.route';
import { SupermunSharedModule } from 'app/shared/shared.module';

@NgModule({
  declarations: [ContactComponent],
  imports: [SupermunSharedModule, RouterModule.forChild(contactRoute)]
})
export class ContactModule {}
