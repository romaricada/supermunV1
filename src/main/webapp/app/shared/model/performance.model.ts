import { ICommune } from 'app/shared/model/commune.model';
import { IIndicateur } from 'app/shared/model/indicateur.model';

export enum TypePrformance {
  COMMUNE = 'COMMUNE',
  INDICATEUR = 'INDICATEUR',
  DOMAINE = 'DOMAINE'
}

export interface IPerformance {
  id?: number;
  score?: number;
  nbEtoile?: number;
  typePerformance?: TypePrformance;
  deleted?: boolean;
  communeId?: number;
  commune?: ICommune;
  indicateurId?: number;
  indicateurLibelle?: string;
  exerciceId?: number;
  domaineId?: number;
  domaineLibelle?: string;
  typeDomainId?: number;
  indicateurs?: IIndicateur[];
  communes?: ICommune[];
  anneePreced?: number;
  scoreAnneePrec?: string;
}

export class Performance implements IPerformance {
  constructor(
    public id?: number,
    public score?: number,
    public nbEtoile?: number,
    public typePerformance?: TypePrformance,
    public deleted?: boolean,
    public communeId?: number,
    public commune?: ICommune,
    public indicateurId?: number,
    public exerciceId?: number,
    public domaineId?: number,
    public typeDomainId?: number,
    public indicateurs?: IIndicateur[],
    public communes?: ICommune[],
    public domaineLibelle?: string,
    public indicateurLibelle?: string,
    public anneePreced?: number,
    public scoreAnneePrec?: string
  ) {
    this.deleted = this.deleted || false;
  }
}
