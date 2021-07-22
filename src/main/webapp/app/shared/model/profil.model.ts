export interface IProfil {
  id?: number;
  profilName?: string;
  deleted?: boolean;
  authorities?: any[];
}

export class Profil implements IProfil {
  constructor(public id?: number, public profilName?: string, public deleted?: boolean, public authorities?: any[]) {
    this.deleted = this.deleted || false;
  }
}
