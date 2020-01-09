import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IPlaylist, Playlist } from 'app/shared/model/playlist.model';
import { PlaylistService } from './playlist.service';
import { PlaylistComponent } from './playlist.component';
import { PlaylistDetailComponent } from './playlist-detail.component';
import { PlaylistUpdateComponent } from './playlist-update.component';

@Injectable({ providedIn: 'root' })
export class PlaylistResolve implements Resolve<IPlaylist> {
  constructor(private service: PlaylistService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IPlaylist> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((playlist: HttpResponse<Playlist>) => {
          if (playlist.body) {
            return of(playlist.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Playlist());
  }
}

export const playlistRoute: Routes = [
  {
    path: '',
    component: PlaylistComponent,
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      authorities: ['ROLE_USER'],
      defaultSort: 'id,asc',
      pageTitle: 'Playlists'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: PlaylistDetailComponent,
    resolve: {
      playlist: PlaylistResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Playlists'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: PlaylistUpdateComponent,
    resolve: {
      playlist: PlaylistResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Playlists'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: PlaylistUpdateComponent,
    resolve: {
      playlist: PlaylistResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Playlists'
    },
    canActivate: [UserRouteAccessService]
  }
];
