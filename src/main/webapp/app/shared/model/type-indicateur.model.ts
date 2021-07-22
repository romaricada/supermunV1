import { IDomaine } from 'app/shared/model/domaine.model';

export interface ITypeIndicateur {
  id?: number;
  libelle?: string;
  deleted?: boolean;
  scoreTypeIndicateur?: number;
  totalTypeDomaine?: number;
  nombreEtoile?: number;
  domaines?: IDomaine[];
  checked?: boolean;
}

export class TypeIndicateur implements ITypeIndicateur {
  constructor(
    public id?: number,
    public libelle?: string,
    public deleted?: boolean,
    public checked?: boolean,
    public domaines?: IDomaine[],
    public totalTypeDomaine?: number,
    public scoreTypeIndicateur?: number,
    public nombreEtoile?: number
  ) {
    this.deleted = this.deleted || false;
  }
}
