import { IPublication } from 'app/shared/model/publication.model';

export interface ITypePublication {
  id?: number;
  libelle?: string;
  publications?: IPublication[];
}

export class TypePublication implements ITypePublication {
  constructor(public id?: number, public libelle?: string, public publications?: IPublication[]) {}
}
