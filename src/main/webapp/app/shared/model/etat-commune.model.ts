import { ICommune } from 'app/shared/model/commune.model';

export interface IEtatCommune {
  id?: number;
  validated?: boolean;
  priseEnCompte?: boolean;
  communeLibelle?: string;
  communeId?: number;
  exerciceAnnee?: number;
  exerciceId?: number;
  commune?: ICommune;
}

export class EtatCommune implements IEtatCommune {
  constructor(
    public id?: number,
    public validated?: boolean,
    public priseEnCompte?: boolean,
    public communeLibelle?: string,
    public communeId?: number,
    public exerciceAnnee?: number,
    public exerciceId?: number,
    public commune?: ICommune
  ) {
    this.validated = this.validated || false;
    this.priseEnCompte = this.priseEnCompte || false;
  }
}

export interface IValidationCommune {
  id?: number;
  validated?: boolean;
  communeLibelle?: string;
  communeId?: number;
  exerciceAnnee?: number;
  exerciceId?: number;
  typeDomaineId?: number;
}

export class ValidationCommune implements IValidationCommune {
  constructor(
    public id?: number,
    public validated?: boolean,
    public communeLibelle?: string,
    public communeId?: number,
    public exerciceAnnee?: number,
    public exerciceId?: number,
    public typeDomaineId?: number
  ) {
    this.validated = this.validated || false;
  }
}
