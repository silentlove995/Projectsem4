import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IPages } from 'app/shared/model/pages.model';
import { PagesService } from './pages.service';

@Component({
  templateUrl: './pages-delete-dialog.component.html'
})
export class PagesDeleteDialogComponent {
  pages?: IPages;

  constructor(protected pagesService: PagesService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  clear(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.pagesService.delete(id).subscribe(() => {
      this.eventManager.broadcast('pagesListModification');
      this.activeModal.close();
    });
  }
}
