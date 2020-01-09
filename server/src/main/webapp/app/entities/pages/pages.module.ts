import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { ServerSharedModule } from 'app/shared/shared.module';
import { PagesComponent } from './pages.component';
import { PagesDetailComponent } from './pages-detail.component';
import { PagesUpdateComponent } from './pages-update.component';
import { PagesDeleteDialogComponent } from './pages-delete-dialog.component';
import { pagesRoute } from './pages.route';

@NgModule({
  imports: [ServerSharedModule, RouterModule.forChild(pagesRoute)],
  declarations: [PagesComponent, PagesDetailComponent, PagesUpdateComponent, PagesDeleteDialogComponent],
  entryComponents: [PagesDeleteDialogComponent]
})
export class ServerPagesModule {}
