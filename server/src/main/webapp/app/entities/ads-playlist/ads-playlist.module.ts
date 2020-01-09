import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { ServerSharedModule } from 'app/shared/shared.module';
import { AdsPlaylistComponent } from './ads-playlist.component';
import { AdsPlaylistDetailComponent } from './ads-playlist-detail.component';
import { AdsPlaylistUpdateComponent } from './ads-playlist-update.component';
import { AdsPlaylistDeleteDialogComponent } from './ads-playlist-delete-dialog.component';
import { adsPlaylistRoute } from './ads-playlist.route';

@NgModule({
  imports: [ServerSharedModule, RouterModule.forChild(adsPlaylistRoute)],
  declarations: [AdsPlaylistComponent, AdsPlaylistDetailComponent, AdsPlaylistUpdateComponent, AdsPlaylistDeleteDialogComponent],
  entryComponents: [AdsPlaylistDeleteDialogComponent]
})
export class ServerAdsPlaylistModule {}
