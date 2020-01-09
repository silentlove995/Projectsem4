import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { ServerSharedModule } from 'app/shared/shared.module';
import { AdsSongComponent } from './ads-song.component';
import { AdsSongDetailComponent } from './ads-song-detail.component';
import { AdsSongUpdateComponent } from './ads-song-update.component';
import { AdsSongDeleteDialogComponent } from './ads-song-delete-dialog.component';
import { adsSongRoute } from './ads-song.route';

@NgModule({
  imports: [ServerSharedModule, RouterModule.forChild(adsSongRoute)],
  declarations: [AdsSongComponent, AdsSongDetailComponent, AdsSongUpdateComponent, AdsSongDeleteDialogComponent],
  entryComponents: [AdsSongDeleteDialogComponent]
})
export class ServerAdsSongModule {}
