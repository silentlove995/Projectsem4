import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IAdsSong, AdsSong } from 'app/shared/model/ads-song.model';
import { AdsSongService } from './ads-song.service';
import { AdsSongComponent } from './ads-song.component';
import { AdsSongDetailComponent } from './ads-song-detail.component';
import { AdsSongUpdateComponent } from './ads-song-update.component';

@Injectable({ providedIn: 'root' })
export class AdsSongResolve implements Resolve<IAdsSong> {
  constructor(private service: AdsSongService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IAdsSong> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((adsSong: HttpResponse<AdsSong>) => {
          if (adsSong.body) {
            return of(adsSong.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new AdsSong());
  }
}

export const adsSongRoute: Routes = [
  {
    path: '',
    component: AdsSongComponent,
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      authorities: ['ROLE_USER'],
      defaultSort: 'id,asc',
      pageTitle: 'AdsSongs'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: AdsSongDetailComponent,
    resolve: {
      adsSong: AdsSongResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'AdsSongs'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: AdsSongUpdateComponent,
    resolve: {
      adsSong: AdsSongResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'AdsSongs'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: AdsSongUpdateComponent,
    resolve: {
      adsSong: AdsSongResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'AdsSongs'
    },
    canActivate: [UserRouteAccessService]
  }
];
