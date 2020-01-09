import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IAdsPlaylist, AdsPlaylist } from 'app/shared/model/ads-playlist.model';
import { AdsPlaylistService } from './ads-playlist.service';
import { AdsPlaylistComponent } from './ads-playlist.component';
import { AdsPlaylistDetailComponent } from './ads-playlist-detail.component';
import { AdsPlaylistUpdateComponent } from './ads-playlist-update.component';

@Injectable({ providedIn: 'root' })
export class AdsPlaylistResolve implements Resolve<IAdsPlaylist> {
  constructor(private service: AdsPlaylistService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IAdsPlaylist> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((adsPlaylist: HttpResponse<AdsPlaylist>) => {
          if (adsPlaylist.body) {
            return of(adsPlaylist.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new AdsPlaylist());
  }
}

export const adsPlaylistRoute: Routes = [
  {
    path: '',
    component: AdsPlaylistComponent,
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      authorities: ['ROLE_USER'],
      defaultSort: 'id,asc',
      pageTitle: 'AdsPlaylists'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: AdsPlaylistDetailComponent,
    resolve: {
      adsPlaylist: AdsPlaylistResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'AdsPlaylists'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: AdsPlaylistUpdateComponent,
    resolve: {
      adsPlaylist: AdsPlaylistResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'AdsPlaylists'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: AdsPlaylistUpdateComponent,
    resolve: {
      adsPlaylist: AdsPlaylistResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'AdsPlaylists'
    },
    canActivate: [UserRouteAccessService]
  }
];
