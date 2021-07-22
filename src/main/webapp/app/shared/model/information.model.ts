export interface IInformation {
  id?: number;
  presentation?: string;
  contact?: string;
  deleted?: boolean;
}

export class Information implements IInformation {
  constructor(public id?: number, public presentation?: string, public contact?: string, public deleted?: boolean) {
    this.deleted = this.deleted || false;
  }
}
