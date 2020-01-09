import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption, SearchWithPagination } from 'app/shared/util/request-util';
import { IPages } from 'app/shared/model/pages.model';

type EntityResponseType = HttpResponse<IPages>;
type EntityArrayResponseType = HttpResponse<IPages[]>;

@Injectable({ providedIn: 'root' })
export class PagesService {
  public resourceUrl = SERVER_API_URL + 'api/pages';
  public resourceSearchUrl = SERVER_API_URL + 'api/_search/pages';

  constructor(protected http: HttpClient) {}

  create(pages: IPages): Observable<EntityResponseType> {
    return this.http.post<IPages>(this.resourceUrl, pages, { observe: 'response' });
  }

  update(pages: IPages): Observable<EntityResponseType> {
    return this.http.put<IPages>(this.resourceUrl, pages, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IPages>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IPages[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IPages[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
  }
}
