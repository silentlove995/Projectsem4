import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { ServerSharedModule } from 'app/shared/shared.module';
import { SongsComponent } from './songs.component';
import { SongsDetailComponent } from './songs-detail.component';
import { SongsUpdateComponent } from './songs-update.component';
import { SongsDeleteDialogComponent } from './songs-delete-dialog.component';
import { songsRoute } from './songs.route';

@NgModule({
  imports: [ServerSharedModule, RouterModule.forChild(songsRoute)],
  declarations: [SongsComponent, SongsDetailComponent, SongsUpdateComponent, SongsDeleteDialogComponent],
  entryComponents: [SongsDeleteDialogComponent]
})
export class ServerSongsModule {}
