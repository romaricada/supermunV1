export interface IDictionaires {
  id?: number;
  entite?: string;
  definition?: string;
  regleCalcule?: string;
}

export class Dictionaires implements IDictionaires {
  constructor(public id?: number, public entite?: string, public definition?: string, public regleCalcule?: string) {}
}
