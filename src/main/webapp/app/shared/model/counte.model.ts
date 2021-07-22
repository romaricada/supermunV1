export interface ICounte {
  id?: number;
  nombreVisiteurs?: number;
}

export class Counte implements ICounte {
  constructor(public id?: number, public nombreVisiteurs?: number) {}
}
