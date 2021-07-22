export interface ICouleur {
  id?: number;
  couleur?: string;
  minVal?: number;
  maxVal?: number;
  indicateurId?: number;
  idPerformance?: number;
  communeId?: number;
  exerciceId?: number;
  libelleIndicateur?: String;
  checked?: boolean;
  idFictif?: number;
  deleted?: boolean;
}

export class Couleur implements ICouleur {
  constructor(
    public id?: number,
    public couleur?: string,
    public minVal?: number,
    public maxVal?: number,
    public indicateurId?: number,
    public idPerformance?: number,
    public communeId?: number,
    public exerciceId?: number,
    public libelleIndicateur?: String,
    public checked?: boolean,
    public idFictif?: number,
    public deleted?: boolean
  ) {
    this.deleted = this.deleted || false;
  }
}
