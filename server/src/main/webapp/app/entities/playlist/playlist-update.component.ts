import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

import { IPlaylist, Playlist } from 'app/shared/model/playlist.model';
import { PlaylistService } from './playlist.service';
import { IAdsPlaylist } from 'app/shared/model/ads-playlist.model';
import { AdsPlaylistService } from 'app/entities/ads-playlist/ads-playlist.service';

@Component({
  selector: 'jhi-playlist-update',
  templateUrl: './playlist-update.component.html'
})
export class PlaylistUpdateComponent implements OnInit {
  isSaving = false;

  ads: IAdsPlaylist[] = [];

  editForm = this.fb.group({
    id: [],
    title: [],
    description: [],
    vocal: [],
    thumbnail: [],
    adsId: []
  });

  constructor(
    protected playlistService: PlaylistService,
    protected adsPlaylistService: AdsPlaylistService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ playlist }) => {
      this.updateForm(playlist);

      this.adsPlaylistService
        .query({ 'playlistId.specified': 'false' })
        .pipe(
          map((res: HttpResponse<IAdsPlaylist[]>) => {
            return res.body ? res.body : [];
          })
        )
        .subscribe((resBody: IAdsPlaylist[]) => {
          if (!playlist.adsId) {
            this.ads = resBody;
          } else {
            this.adsPlaylistService
              .find(playlist.adsId)
              .pipe(
                map((subRes: HttpResponse<IAdsPlaylist>) => {
                  return subRes.body ? [subRes.body].concat(resBody) : resBody;
                })
              )
              .subscribe((concatRes: IAdsPlaylist[]) => {
                this.ads = concatRes;
              });
          }
        });
    });
  }

  updateForm(playlist: IPlaylist): void {
    this.editForm.patchValue({
      id: playlist.id,
      title: playlist.title,
      description: playlist.description,
      vocal: playlist.vocal,
      thumbnail: playlist.thumbnail,
      adsId: playlist.adsId
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const playlist = this.createFromForm();
    if (playlist.id !== undefined) {
      this.subscribeToSaveResponse(this.playlistService.update(playlist));
    } else {
      this.subscribeToSaveResponse(this.playlistService.create(playlist));
    }
  }

  private createFromForm(): IPlaylist {
    return {
      ...new Playlist(),
      id: this.editForm.get(['id'])!.value,
      title: this.editForm.get(['title'])!.value,
      description: this.editForm.get(['description'])!.value,
      vocal: this.editForm.get(['vocal'])!.value,
      thumbnail: this.editForm.get(['thumbnail'])!.value,
      adsId: this.editForm.get(['adsId'])!.value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPlaylist>>): void {
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

  trackById(index: number, item: IAdsPlaylist): any {
    return item.id;
  }
}
