import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { ISongs, Songs } from 'app/shared/model/songs.model';
import { SongsService } from './songs.service';
import { SongsComponent } from './songs.component';
import { SongsDetailComponent } from './songs-detail.component';
import { SongsUpdateComponent } from './songs-update.component';

@Injectable({ providedIn: 'root' })
export class SongsResolve implements Resolve<ISongs> {
  constructor(private service: SongsService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ISongs> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((songs: HttpResponse<Songs>) => {
          if (songs.body) {
            return of(songs.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Songs());
  }
}

export const songsRoute: Routes = [
  {
    path: '',
    component: SongsComponent,
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      authorities: ['ROLE_USER'],
      defaultSort: 'id,asc',
      pageTitle: 'Songs'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: SongsDetailComponent,
    resolve: {
      songs: SongsResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Songs'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: SongsUpdateComponent,
    resolve: {
      songs: SongsResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Songs'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: SongsUpdateComponent,
    resolve: {
      songs: SongsResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Songs'
    },
    canActivate: [UserRouteAccessService]
  }
];
