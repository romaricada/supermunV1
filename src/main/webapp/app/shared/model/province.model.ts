export interface IProvince {
  id?: number;
  code?: string;
  libelle?: string;
  deleted?: boolean;
  regionId?: number;
  libelleRegion?: string;
}

export class Province implements IProvince {
  constructor(
    public id?: number,
    public code?: string,
    public libelle?: string,
    public deleted?: boolean,
    public regionId?: number,
    public libelleRegion?: string
  ) {
    this.deleted = this.deleted || false;
  }
}
