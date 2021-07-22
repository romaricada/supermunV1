import { IModalite } from 'app/shared/model/modalite.model';

export interface IValeurModalite {
  id?: number;
  valeur?: string;
  exerciceId?: number;
  communeId?: number;
  modaliteId?: number;
  modalite?: IModalite;
  indicateurId?: number;
}

export class ValeurModalite implements IValeurModalite {
  constructor(
    public id?: number,
    public valeur?: string,
    public exerciceId?: number,
    public communeId?: number,
    public modaliteId?: number,
    public modalite?: IModalite,
    public indicateurId?: number
  ) {}
}
