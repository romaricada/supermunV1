export interface IFichier {
  fileNameContentType?: string;
  fileName?: any;
}

export class Fichier implements IFichier {
  constructor(public fileNameContentType?: string, public fileName?: any) {}
}
