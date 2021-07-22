export enum Formule {
  TAUXCSPS = 'TAUXCSPS',
  RETARDMOYEN = 'RETARDMOYEN',
  TAUXPARTICIPATION = 'TAUXPARTICIPATION'
}

export interface IModalite {
  id?: number;
  idFictif?: number;
  code?: string;
  libelle?: string;
  borneMaximale?: number;
  borneMinimale?: number;
  valeur?: number;
  deleted?: boolean;
  indicateurId?: number;
  /*
    formule?: Formule;
    image1ContentType?: string;
    image1?: any;
    image2ContentType?: string;
    image2?: any;
  */
}

export class Modalite implements IModalite {
  constructor(
    public id?: number,
    public idFictif?: number,
    public code?: string,
    public libelle?: string,
    public borneMaximale?: number,
    public borneMinimale?: number,
    public valeur?: number,
    public deleted?: boolean,
    public indicateurId?: number /*
      public formule?: Formule,
      public image1ContentType?: string,
      public image1?: any,
      public image2ContentType?: string,
      public image2?: any
    */
  ) {
    this.deleted = this.deleted || false;
  }
}
