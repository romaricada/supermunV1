import { IModalite } from 'app/shared/model/modalite.model';
import { IValeurModalite } from 'app/shared/model/valeur-modalite.model';
import { IDomaine } from 'app/shared/model/domaine.model';

export interface IIndicateur {
  id?: number;
  code?: string;
  libelle?: string;
  description?: string;
  sousIndicateur?: boolean;
  imageModaliteOKContentType?: string;
  imageModaliteOK?: any;
  imageModaliteNOKContentType?: string;
  imageModaliteNOK?: any;
  image1ContentType?: string;
  image1?: any;
  image2ContentType?: string;
  image2?: any;
  totalPoint?: number;
  sroreIndicateur?: number;
  valeurIndicateur?: number;
  interval?: boolean;
  deleted?: boolean;
  domaineId?: number;
  domaine?: IDomaine;
  scores?: any[];
  photoContentType?: string;
  modalites?: IModalite[];
  checked?: boolean;
  typeIndicateur?: string;
  addSousIndicateur?: boolean;
  uniteBorne?: string;
  borneMin?: number;
  borneMax?: number;
  valeurmodalites?: IValeurModalite[];
}

export class Indicateur implements IIndicateur {
  constructor(
    public id?: number,
    public code?: string,
    public libelle?: string,
    public description?: string,
    public sousIndicateur?: boolean,
    public imageModaliteOKContentType?: string,
    public imageModaliteOK?: any,
    public imageModaliteNOKContentType?: string,
    public imageModaliteNOK?: any,
    public image1ContentType?: string,
    public image1?: any,
    public image2ContentType?: string,
    public image2?: any,
    public totalPoint?: number,
    public sroreIndicateur?: number,
    public valeurIndicateur?: number,
    public interval?: boolean,
    public deleted?: boolean,
    public domaineId?: number,
    public domaine?: IDomaine,
    public scores?: any[],
    public modalites?: IModalite[],
    public checked?: boolean,
    public typeIndicateur?: string,
    public addSousIndicateur?: boolean,
    public uniteBorne?: string,
    public borneMin?: number,
    public borneMax?: number,
    public valeurmodalites?: IValeurModalite[]
  ) {
    this.interval = this.interval || false;
    this.deleted = this.deleted || false;
    this.sousIndicateur = this.sousIndicateur || false;
  }
}
