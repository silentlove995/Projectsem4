import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IPages, Pages } from 'app/shared/model/pages.model';
import { PagesService } from './pages.service';
import { PagesComponent } from './pages.component';
import { PagesDetailComponent } from './pages-detail.component';
import { PagesUpdateComponent } from './pages-update.component';

@Injectable({ providedIn: 'root' })
export class PagesResolve implements Resolve<IPages> {
  constructor(private service: PagesService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IPages> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((pages: HttpResponse<Pages>) => {
          if (pages.body) {
            return of(pages.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Pages());
  }
}

export const pagesRoute: Routes = [
  {
    path: '',
    component: PagesComponent,
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      authorities: ['ROLE_USER'],
      defaultSort: 'id,asc',
      pageTitle: 'Pages'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: PagesDetailComponent,
    resolve: {
      pages: PagesResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Pages'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: PagesUpdateComponent,
    resolve: {
      pages: PagesResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Pages'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: PagesUpdateComponent,
    resolve: {
      pages: PagesResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Pages'
    },
    canActivate: [UserRouteAccessService]
  }
];
