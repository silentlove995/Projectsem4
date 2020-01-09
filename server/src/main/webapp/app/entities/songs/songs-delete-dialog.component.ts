import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ISongs } from 'app/shared/model/songs.model';
import { SongsService } from './songs.service';

@Component({
  templateUrl: './songs-delete-dialog.component.html'
})
export class SongsDeleteDialogComponent {
  songs?: ISongs;

  constructor(protected songsService: SongsService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  clear(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.songsService.delete(id).subscribe(() => {
      this.eventManager.broadcast('songsListModification');
      this.activeModal.close();
    });
  }
}
