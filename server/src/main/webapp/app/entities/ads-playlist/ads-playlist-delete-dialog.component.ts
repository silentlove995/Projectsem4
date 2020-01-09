import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IAdsPlaylist } from 'app/shared/model/ads-playlist.model';
import { AdsPlaylistService } from './ads-playlist.service';

@Component({
  templateUrl: './ads-playlist-delete-dialog.component.html'
})
export class AdsPlaylistDeleteDialogComponent {
  adsPlaylist?: IAdsPlaylist;

  constructor(
    protected adsPlaylistService: AdsPlaylistService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  clear(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.adsPlaylistService.delete(id).subscribe(() => {
      this.eventManager.broadcast('adsPlaylistListModification');
      this.activeModal.close();
    });
  }
}
