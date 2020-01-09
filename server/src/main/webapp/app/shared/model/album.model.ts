import { ISongs } from 'app/shared/model/songs.model';

export interface IAlbum {
  id?: number;
  title?: string;
  description?: string;
  vocal?: string;
  thumbnail?: string;
  songs?: ISongs[];
}

export class Album implements IAlbum {
  constructor(
    public id?: number,
    public title?: string,
    public description?: string,
    public vocal?: string,
    public thumbnail?: string,
    public songs?: ISongs[]
  ) {}
}
