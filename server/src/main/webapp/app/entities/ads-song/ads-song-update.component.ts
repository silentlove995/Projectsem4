import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { IAdsSong, AdsSong } from 'app/shared/model/ads-song.model';
import { AdsSongService } from './ads-song.service';

@Component({
  selector: 'jhi-ads-song-update',
  templateUrl: './ads-song-update.component.html'
})
export class AdsSongUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    adsImage: [],
    adsContent: [],
    songId: [],
    songTitle: [],
    songImage: []
  });

  constructor(protected adsSongService: AdsSongService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ adsSong }) => {
      this.updateForm(adsSong);
    });
  }

  updateForm(adsSong: IAdsSong): void {
    this.editForm.patchValue({
      id: adsSong.id,
      adsImage: adsSong.adsImage,
      adsContent: adsSong.adsContent,
      songId: adsSong.songId,
      songTitle: adsSong.songTitle,
      songImage: adsSong.songImage
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const adsSong = this.createFromForm();
    if (adsSong.id !== undefined) {
      this.subscribeToSaveResponse(this.adsSongService.update(adsSong));
    } else {
      this.subscribeToSaveResponse(this.adsSongService.create(adsSong));
    }
  }

  private createFromForm(): IAdsSong {
    return {
      ...new AdsSong(),
      id: this.editForm.get(['id'])!.value,
      adsImage: this.editForm.get(['adsImage'])!.value,
      adsContent: this.editForm.get(['adsContent'])!.value,
      songId: this.editForm.get(['songId'])!.value,
      songTitle: this.editForm.get(['songTitle'])!.value,
      songImage: this.editForm.get(['songImage'])!.value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAdsSong>>): void {
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
