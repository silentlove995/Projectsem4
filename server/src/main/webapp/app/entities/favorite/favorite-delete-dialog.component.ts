import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IFavorite } from 'app/shared/model/favorite.model';
import { FavoriteService } from './favorite.service';

@Component({
  templateUrl: './favorite-delete-dialog.component.html'
})
export class FavoriteDeleteDialogComponent {
  favorite?: IFavorite;

  constructor(protected favoriteService: FavoriteService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  clear(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.favoriteService.delete(id).subscribe(() => {
      this.eventManager.broadcast('favoriteListModification');
      this.activeModal.close();
    });
  }
}
