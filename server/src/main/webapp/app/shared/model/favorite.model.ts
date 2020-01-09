import { ISongs } from 'app/shared/model/songs.model';

export interface IFavorite {
  id?: number;
  user?: string;
  song?: string;
  songs?: ISongs[];
}

export class Favorite implements IFavorite {
  constructor(public id?: number, public user?: string, public song?: string, public songs?: ISongs[]) {}
}
