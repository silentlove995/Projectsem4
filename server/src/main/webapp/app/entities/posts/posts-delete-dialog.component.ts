import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IPosts } from 'app/shared/model/posts.model';
import { PostsService } from './posts.service';

@Component({
  templateUrl: './posts-delete-dialog.component.html'
})
export class PostsDeleteDialogComponent {
  posts?: IPosts;

  constructor(protected postsService: PostsService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  clear(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.postsService.delete(id).subscribe(() => {
      this.eventManager.broadcast('postsListModification');
      this.activeModal.close();
    });
  }
}
