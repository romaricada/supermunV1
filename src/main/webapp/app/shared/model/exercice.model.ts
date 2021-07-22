export interface IExercice {
  id?: number;
  annee?: number;
  deleted?: boolean;
  validated?: boolean;
}

export class Exercice implements IExercice {
  constructor(public id?: number, public annee?: number, public deleted?: boolean, public validated?: boolean) {
    this.deleted = this.deleted || false;
    this.validated = this.validated || false;
  }
}
