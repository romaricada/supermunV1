export interface IPublication {
  id?: number;
  libelle?: string;
  description?: string;
  contenuContentType?: string;
  contenu?: any;
  published?: boolean;
  deleted?: boolean;
  typePublicationLibelle?: string;
  typePublicationId?: number;
  image?: any;
  imageContentType?: string;
}

export class Publication implements IPublication {
  constructor(
    public id?: number,
    public libelle?: string,
    public description?: string,
    public contenuContentType?: string,
    public contenu?: any,
    public published?: boolean,
    public deleted?: boolean,
    public typePublicationLibelle?: string,
    public typePublicationId?: number,
    public image?: any,
    public imageContentType?: string
  ) {
    this.published = this.published || false;
    this.deleted = this.deleted || false;
  }
}
