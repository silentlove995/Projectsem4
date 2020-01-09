import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'payment',
        loadChildren: () => import('./payment/payment.module').then(m => m.ServerPaymentModule)
      },
      {
        path: 'songs',
        loadChildren: () => import('./songs/songs.module').then(m => m.ServerSongsModule)
      },
      {
        path: 'posts',
        loadChildren: () => import('./posts/posts.module').then(m => m.ServerPostsModule)
      },
      {
        path: 'pages',
        loadChildren: () => import('./pages/pages.module').then(m => m.ServerPagesModule)
      },
      {
        path: 'album',
        loadChildren: () => import('./album/album.module').then(m => m.ServerAlbumModule)
      },
      {
        path: 'playlist',
        loadChildren: () => import('./playlist/playlist.module').then(m => m.ServerPlaylistModule)
      },
      {
        path: 'ads-song',
        loadChildren: () => import('./ads-song/ads-song.module').then(m => m.ServerAdsSongModule)
      },
      {
        path: 'ads-playlist',
        loadChildren: () => import('./ads-playlist/ads-playlist.module').then(m => m.ServerAdsPlaylistModule)
      },
      {
        path: 'favorite',
        loadChildren: () => import('./favorite/favorite.module').then(m => m.ServerFavoriteModule)
      }
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ])
  ]
})
export class ServerEntityModule {}
