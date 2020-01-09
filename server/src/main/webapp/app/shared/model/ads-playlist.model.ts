export interface IAdsPlaylist {
  id?: number;
  adsImage?: string;
  adsContent?: string;
  playlistId?: number;
  playlistTitle?: string;
  playlistImage?: string;
}

export class AdsPlaylist implements IAdsPlaylist {
  constructor(
    public id?: number,
    public adsImage?: string,
    public adsContent?: string,
    public playlistId?: number,
    public playlistTitle?: string,
    public playlistImage?: string
  ) {}
}
