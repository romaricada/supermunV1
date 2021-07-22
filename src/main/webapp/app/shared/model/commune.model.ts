import { IIndicateur } from 'app/shared/model/indicateur.model';

export interface ICommune {
  id?: number;
  code?: string;
  libelle?: string;
  population?: number;
  superficie?: number;
  positionLabelLat?: number;
  positionLabelLon?: number;
  geom?: any;
  deleted?: boolean;
  scoreCommune?: number;
  provinceId?: number;
  rangNational?: number;
  rangRegional?: number;
  libelleProvince?: string;
  libelleRegion?: string;
  indicateurs?: IIndicateur[];
}

export class Commune implements ICommune {
  constructor(
    public id?: number,
    public code?: string,
    public libelle?: string,
    public population?: number,
    public superficie?: number,
    public positionLabelLat?: number,
    public positionLabelLon?: number,
    public geom?: any,
    public deleted?: boolean,
    public provinceId?: number,
    public libelleProvince?: string,
    public scoreCommune?: number,
    public rangNational?: number,
    public rangRegional?: number,
    public libelleRegion?: string,
    public indicateurs?: IIndicateur[]
  ) {
    this.deleted = this.deleted || false;
  }
}
