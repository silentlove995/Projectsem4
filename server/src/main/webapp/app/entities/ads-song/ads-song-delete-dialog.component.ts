import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IAdsSong } from 'app/shared/model/ads-song.model';
import { AdsSongService } from './ads-song.service';

@Component({
  templateUrl: './ads-song-delete-dialog.component.html'
})
export class AdsSongDeleteDialogComponent {
  adsSong?: IAdsSong;

  constructor(protected adsSongService: AdsSongService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  clear(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.adsSongService.delete(id).subscribe(() => {
      this.eventManager.broadcast('adsSongListModification');
      this.activeModal.close();
    });
  }
}
