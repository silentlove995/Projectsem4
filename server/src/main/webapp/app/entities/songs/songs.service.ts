import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption, SearchWithPagination } from 'app/shared/util/request-util';
import { ISongs } from 'app/shared/model/songs.model';

type EntityResponseType = HttpResponse<ISongs>;
type EntityArrayResponseType = HttpResponse<ISongs[]>;

@Injectable({ providedIn: 'root' })
export class SongsService {
  public resourceUrl = SERVER_API_URL + 'api/songs';
  public resourceSearchUrl = SERVER_API_URL + 'api/_search/songs';

  constructor(protected http: HttpClient) {}

  create(songs: ISongs): Observable<EntityResponseType> {
    return this.http.post<ISongs>(this.resourceUrl, songs, { observe: 'response' });
  }

  update(songs: ISongs): Observable<EntityResponseType> {
    return this.http.put<ISongs>(this.resourceUrl, songs, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ISongs>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ISongs[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ISongs[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
  }
}
