import { IPerformance } from 'app/shared/model/performance.model';
import { IValeurModalite } from 'app/shared/model/valeur-modalite.model';
import { IEtatCommune, IValidationCommune } from 'app/shared/model/etat-commune.model';

export class CommuneCopy {
  constructor(
    public id?: number,
    public libelle?: string,
    public performances?: IPerformance[],
    public valeurModalites?: IValeurModalite[],
    public etatCommune?: IEtatCommune,
    public validationCommune?: IValidationCommune,
    public validated?: boolean
  ) {}
}
