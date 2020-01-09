import { ISongs } from 'app/shared/model/songs.model';

export interface IPlaylist {
  id?: number;
  title?: string;
  description?: string;
  vocal?: string;
  thumbnail?: string;
  adsId?: number;
  songs?: ISongs[];
}

export class Playlist implements IPlaylist {
  constructor(
    public id?: number,
    public title?: string,
    public description?: string,
    public vocal?: string,
    public thumbnail?: string,
    public adsId?: number,
    public songs?: ISongs[]
  ) {}
}
