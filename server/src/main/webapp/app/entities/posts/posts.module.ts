import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { ServerSharedModule } from 'app/shared/shared.module';
import { PostsComponent } from './posts.component';
import { PostsDetailComponent } from './posts-detail.component';
import { PostsUpdateComponent } from './posts-update.component';
import { PostsDeleteDialogComponent } from './posts-delete-dialog.component';
import { postsRoute } from './posts.route';

@NgModule({
  imports: [ServerSharedModule, RouterModule.forChild(postsRoute)],
  declarations: [PostsComponent, PostsDetailComponent, PostsUpdateComponent, PostsDeleteDialogComponent],
  entryComponents: [PostsDeleteDialogComponent]
})
export class ServerPostsModule {}
