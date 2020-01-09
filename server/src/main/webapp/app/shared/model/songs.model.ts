import { Genre } from 'app/shared/model/enumerations/genre.model';
import { Country } from 'app/shared/model/enumerations/country.model';

export interface ISongs {
  id?: number;
  title?: string;
  genre?: Genre;
  vocal?: string;
  country?: Country;
  description?: string;
  songAddress?: string;
  lyric?: any;
  avatar?: string;
  listenCount?: number;
  favoriteCount?: number;
  adsId?: number;
  playlistId?: number;
  albumId?: number;
  favoriteId?: number;
}

export class Songs implements ISongs {
  constructor(
    public id?: number,
    public title?: string,
    public genre?: Genre,
    public vocal?: string,
    public country?: Country,
    public description?: string,
    public songAddress?: string,
    public lyric?: any,
    public avatar?: string,
    public listenCount?: number,
    public favoriteCount?: number,
    public adsId?: number,
    public playlistId?: number,
    public albumId?: number,
    public favoriteId?: number
  ) {}
}
