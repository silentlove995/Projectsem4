import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { IPages, Pages } from 'app/shared/model/pages.model';
import { PagesService } from './pages.service';

@Component({
  selector: 'jhi-pages-update',
  templateUrl: './pages-update.component.html'
})
export class PagesUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    name: [],
    avatar: [],
    idol: []
  });

  constructor(protected pagesService: PagesService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ pages }) => {
      this.updateForm(pages);
    });
  }

  updateForm(pages: IPages): void {
    this.editForm.patchValue({
      id: pages.id,
      name: pages.name,
      avatar: pages.avatar,
      idol: pages.idol
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const pages = this.createFromForm();
    if (pages.id !== undefined) {
      this.subscribeToSaveResponse(this.pagesService.update(pages));
    } else {
      this.subscribeToSaveResponse(this.pagesService.create(pages));
    }
  }

  private createFromForm(): IPages {
    return {
      ...new Pages(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      avatar: this.editForm.get(['avatar'])!.value,
      idol: this.editForm.get(['idol'])!.value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPages>>): void {
    result.subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError(): void {
    this.isSaving = false;
  }
}
