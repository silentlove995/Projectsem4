export interface IAdsSong {
  id?: number;
  adsImage?: string;
  adsContent?: string;
  songId?: number;
  songTitle?: string;
  songImage?: string;
}

export class AdsSong implements IAdsSong {
  constructor(
    public id?: number,
    public adsImage?: string,
    public adsContent?: string,
    public songId?: number,
    public songTitle?: string,
    public songImage?: string
  ) {}
}
