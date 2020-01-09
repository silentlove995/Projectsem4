import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

import { IPosts, Posts } from 'app/shared/model/posts.model';
import { PostsService } from './posts.service';
import { IPages } from 'app/shared/model/pages.model';
import { PagesService } from 'app/entities/pages/pages.service';

@Component({
  selector: 'jhi-posts-update',
  templateUrl: './posts-update.component.html'
})
export class PostsUpdateComponent implements OnInit {
  isSaving = false;

  pages: IPages[] = [];

  editForm = this.fb.group({
    id: [],
    title: [],
    content: [],
    comment: [],
    image: [],
    like: [],
    songAddress: [],
    pagesId: []
  });

  constructor(
    protected postsService: PostsService,
    protected pagesService: PagesService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ posts }) => {
      this.updateForm(posts);

      this.pagesService
        .query()
        .pipe(
          map((res: HttpResponse<IPages[]>) => {
            return res.body ? res.body : [];
          })
        )
        .subscribe((resBody: IPages[]) => (this.pages = resBody));
    });
  }

  updateForm(posts: IPosts): void {
    this.editForm.patchValue({
      id: posts.id,
      title: posts.title,
      content: posts.content,
      comment: posts.comment,
      image: posts.image,
      like: posts.like,
      songAddress: posts.songAddress,
      pagesId: posts.pagesId
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const posts = this.createFromForm();
    if (posts.id !== undefined) {
      this.subscribeToSaveResponse(this.postsService.update(posts));
    } else {
      this.subscribeToSaveResponse(this.postsService.create(posts));
    }
  }

  private createFromForm(): IPosts {
    return {
      ...new Posts(),
      id: this.editForm.get(['id'])!.value,
      title: this.editForm.get(['title'])!.value,
      content: this.editForm.get(['content'])!.value,
      comment: this.editForm.get(['comment'])!.value,
      image: this.editForm.get(['image'])!.value,
      like: this.editForm.get(['like'])!.value,
      songAddress: this.editForm.get(['songAddress'])!.value,
      pagesId: this.editForm.get(['pagesId'])!.value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPosts>>): void {
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

  trackById(index: number, item: IPages): any {
    return item.id;
  }
}
