export interface IExerciceCommune {
  anneeId?: number;
  communeId?: number;
}

export class ExerciceCommune implements IExerciceCommune {
  constructor(public anneeId?: number, public communeId?: number) {}
}
