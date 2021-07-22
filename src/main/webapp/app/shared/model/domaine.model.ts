import { IIndicateur } from 'app/shared/model/indicateur.model';
import { ITypeIndicateur } from 'app/shared/model/type-indicateur.model';

export interface IDomaine {
  id?: number;
  code?: string;
  libelle?: string;
  description?: string;
  imageContentType?: string;
  image?: any;
  deleted?: boolean;
  typeIndicateurId?: number;
  typeIndicateur?: ITypeIndicateur;
  totalScore?: number;
  pointTotal?: number;
  nbEtoile?: number;
  indicateurs?: IIndicateur[];
}

export class Domaine implements IDomaine {
  constructor(
    public id?: number,
    public code?: string,
    public libelle?: string,
    public description?: string,
    public imageContentType?: string,
    public image?: any,
    public deleted?: boolean,
    public typeIndicateurId?: number,
    public typeIndicateur?: ITypeIndicateur,
    public totalScore?: number,
    public pointTotal?: number,
    public nbEtoile?: number,
    public indicateurs?: IIndicateur[]
  ) {
    this.deleted = this.deleted || false;
  }
}
