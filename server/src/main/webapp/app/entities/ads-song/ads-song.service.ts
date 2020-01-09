import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption, SearchWithPagination } from 'app/shared/util/request-util';
import { IAdsSong } from 'app/shared/model/ads-song.model';

type EntityResponseType = HttpResponse<IAdsSong>;
type EntityArrayResponseType = HttpResponse<IAdsSong[]>;

@Injectable({ providedIn: 'root' })
export class AdsSongService {
  public resourceUrl = SERVER_API_URL + 'api/ads-songs';
  public resourceSearchUrl = SERVER_API_URL + 'api/_search/ads-songs';

  constructor(protected http: HttpClient) {}

  create(adsSong: IAdsSong): Observable<EntityResponseType> {
    return this.http.post<IAdsSong>(this.resourceUrl, adsSong, { observe: 'response' });
  }

  update(adsSong: IAdsSong): Observable<EntityResponseType> {
    return this.http.put<IAdsSong>(this.resourceUrl, adsSong, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IAdsSong>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IAdsSong[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IAdsSong[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
  }
}
