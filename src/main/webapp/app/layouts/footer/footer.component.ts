import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'jhi-footer',
  templateUrl: './footer.component.html',
  styleUrls: ['./footer.component.scss']
})
export class FooterComponent implements OnInit {
  date = new Date();
  annee: number;

  constructor() {}

  ngOnInit() {
    setInterval(() => {
      this.date = new Date();
      this.annee = this.date.getFullYear();
    }, 1000);
  }
}
