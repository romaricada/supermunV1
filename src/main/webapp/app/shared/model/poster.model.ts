export interface IPoster {
  id?: number;
  url?: string;
  contenuContentType?: string;
  contenu?: any;
  deleted?: boolean;
  exerciceId?: number;
  communeId?: number;
}

export class Poster implements IPoster {
  constructor(
    public id?: number,
    public url?: string,
    public contenuContentType?: string,
    public contenu?: any,
    public deleted?: boolean,
    public exerciceId?: number,
    public communeId?: number
  ) {
    this.deleted = this.deleted || false;
  }
}
