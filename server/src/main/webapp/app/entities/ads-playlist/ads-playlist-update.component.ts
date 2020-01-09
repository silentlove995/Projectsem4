import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { IAdsPlaylist, AdsPlaylist } from 'app/shared/model/ads-playlist.model';
import { AdsPlaylistService } from './ads-playlist.service';

@Component({
  selector: 'jhi-ads-playlist-update',
  templateUrl: './ads-playlist-update.component.html'
})
export class AdsPlaylistUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    adsImage: [],
    adsContent: [],
    playlistId: [],
    playlistTitle: [],
    playlistImage: []
  });

  constructor(protected adsPlaylistService: AdsPlaylistService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ adsPlaylist }) => {
      this.updateForm(adsPlaylist);
    });
  }

  updateForm(adsPlaylist: IAdsPlaylist): void {
    this.editForm.patchValue({
      id: adsPlaylist.id,
      adsImage: adsPlaylist.adsImage,
      adsContent: adsPlaylist.adsContent,
      playlistId: adsPlaylist.playlistId,
      playlistTitle: adsPlaylist.playlistTitle,
      playlistImage: adsPlaylist.playlistImage
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const adsPlaylist = this.createFromForm();
    if (adsPlaylist.id !== undefined) {
      this.subscribeToSaveResponse(this.adsPlaylistService.update(adsPlaylist));
    } else {
      this.subscribeToSaveResponse(this.adsPlaylistService.create(adsPlaylist));
    }
  }

  private createFromForm(): IAdsPlaylist {
    return {
      ...new AdsPlaylist(),
      id: this.editForm.get(['id'])!.value,
      adsImage: this.editForm.get(['adsImage'])!.value,
      adsContent: this.editForm.get(['adsContent'])!.value,
      playlistId: this.editForm.get(['playlistId'])!.value,
      playlistTitle: this.editForm.get(['playlistTitle'])!.value,
      playlistImage: this.editForm.get(['playlistImage'])!.value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAdsPlaylist>>): void {
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
