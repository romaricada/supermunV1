export interface IRegion {
  id?: number;
  code?: string;
  libelle?: string;
  deleted?: boolean;
  couleur?: string;
}

export class Region implements IRegion {
  constructor(public id?: number, public code?: string, public libelle?: string, public deleted?: boolean, public couleur?: string) {
    this.deleted = this.deleted || false;
  }
}
