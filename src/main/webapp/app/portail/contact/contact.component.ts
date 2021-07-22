import { Component, OnInit } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { ContactService } from 'app/portail/contact/contact.service';
import { Contact } from 'app/shared/model/contact';
import { MessageService } from 'primeng/api';

@Component({
  selector: 'jhi-contact',
  templateUrl: './contact.component.html',
  styleUrls: ['./contact.component.scss']
})
export class ContactComponent implements OnInit {
  contact: Contact;

  contactForm = this.fb.group({
    nom: ['', [Validators.required, Validators.minLength(2)]],
    prenom: ['', [Validators.required, Validators.minLength(2), Validators.email]],
    telephone: ['', [Validators.minLength(8), Validators.pattern('[0-9]+')]],
    email: ['', [Validators.required, Validators.minLength(4), Validators.maxLength(354), Validators.email]],
    message: ['', [Validators.required, Validators.minLength(4)]]
  });

  constructor(private fb: FormBuilder, private contactService: ContactService, private messageService: MessageService) {}

  ngOnInit() {
    this.contact = new Contact();
  }

  sendEmail() {
    this.contact.nom = this.contactForm.get(['nom']).value;
    this.contact.prenom = this.contactForm.get(['prenom']).value;
    this.contact.telephone = this.contactForm.get(['telephone']).value;
    this.contact.email = this.contactForm.get(['email']).value;
    this.contact.message = this.contactForm.get(['message']).value;

    if (this.contact.nom && this.contact.prenom && this.contact.email && this.contact.message) {
      this.contactService.sendMessage(this.contact).subscribe(
        () => {
          this.messageService.add({
            severity: 'success',
            summary: 'Information',
            detail: 'Merci de nous avoir contacté, nous vous reviendrons incessemment !'
          });
        },
        () =>
          this.messageService.add({
            severity: 'error',
            summary: 'Information',
            detail: "Error lors de l'envoi du message !"
          })
      );
    } else {
      this.messageService.add({
        severity: 'warn',
        summary: 'Information',
        detail: 'Formulaire invalide !'
      });
    }
  }
}
