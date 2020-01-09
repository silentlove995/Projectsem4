import { IPosts } from 'app/shared/model/posts.model';

export interface IPages {
  id?: number;
  name?: string;
  avatar?: string;
  idol?: string;
  titles?: IPosts[];
}

export class Pages implements IPages {
  constructor(public id?: number, public name?: string, public avatar?: string, public idol?: string, public titles?: IPosts[]) {}
}
