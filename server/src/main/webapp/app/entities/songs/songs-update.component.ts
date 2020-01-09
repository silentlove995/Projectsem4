import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { JhiDataUtils, JhiFileLoadError, JhiEventManager, JhiEventWithContent } from 'ng-jhipster';

import { ISongs, Songs } from 'app/shared/model/songs.model';
import { SongsService } from './songs.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { IAdsSong } from 'app/shared/model/ads-song.model';
import { AdsSongService } from 'app/entities/ads-song/ads-song.service';
import { IPlaylist } from 'app/shared/model/playlist.model';
import { PlaylistService } from 'app/entities/playlist/playlist.service';
import { IAlbum } from 'app/shared/model/album.model';
import { AlbumService } from 'app/entities/album/album.service';
import { IFavorite } from 'app/shared/model/favorite.model';
import { FavoriteService } from 'app/entities/favorite/favorite.service';

type SelectableEntity = IAdsSong | IPlaylist | IAlbum | IFavorite;

@Component({
  selector: 'jhi-songs-update',
  templateUrl: './songs-update.component.html'
})
export class SongsUpdateComponent implements OnInit {
  isSaving = false;

  ads: IAdsSong[] = [];

  playlists: IPlaylist[] = [];

  albums: IAlbum[] = [];

  favorites: IFavorite[] = [];

  editForm = this.fb.group({
    id: [],
    title: [],
    genre: [],
    vocal: [],
    country: [],
    description: [],
    songAddress: [],
    lyric: [],
    avatar: [],
    listenCount: [],
    favoriteCount: [],
    adsId: [],
    playlistId: [],
    albumId: [],
    favoriteId: []
  });

  constructor(
    protected dataUtils: JhiDataUtils,
    protected eventManager: JhiEventManager,
    protected songsService: SongsService,
    protected adsSongService: AdsSongService,
    protected playlistService: PlaylistService,
    protected albumService: AlbumService,
    protected favoriteService: FavoriteService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ songs }) => {
      this.updateForm(songs);

      this.adsSongService
        .query({ 'songsId.specified': 'false' })
        .pipe(
          map((res: HttpResponse<IAdsSong[]>) => {
            return res.body ? res.body : [];
          })
        )
        .subscribe((resBody: IAdsSong[]) => {
          if (!songs.adsId) {
            this.ads = resBody;
          } else {
            this.adsSongService
              .find(songs.adsId)
              .pipe(
                map((subRes: HttpResponse<IAdsSong>) => {
                  return subRes.body ? [subRes.body].concat(resBody) : resBody;
                })
              )
              .subscribe((concatRes: IAdsSong[]) => {
                this.ads = concatRes;
              });
          }
        });

      this.playlistService
        .query()
        .pipe(
          map((res: HttpResponse<IPlaylist[]>) => {
            return res.body ? res.body : [];
          })
        )
        .subscribe((resBody: IPlaylist[]) => (this.playlists = resBody));

      this.albumService
        .query()
        .pipe(
          map((res: HttpResponse<IAlbum[]>) => {
            return res.body ? res.body : [];
          })
        )
        .subscribe((resBody: IAlbum[]) => (this.albums = resBody));

      this.favoriteService
        .query()
        .pipe(
          map((res: HttpResponse<IFavorite[]>) => {
            return res.body ? res.body : [];
          })
        )
        .subscribe((resBody: IFavorite[]) => (this.favorites = resBody));
    });
  }

  updateForm(songs: ISongs): void {
    this.editForm.patchValue({
      id: songs.id,
      title: songs.title,
      genre: songs.genre,
      vocal: songs.vocal,
      country: songs.country,
      description: songs.description,
      songAddress: songs.songAddress,
      lyric: songs.lyric,
      avatar: songs.avatar,
      listenCount: songs.listenCount,
      favoriteCount: songs.favoriteCount,
      adsId: songs.adsId,
      playlistId: songs.playlistId,
      albumId: songs.albumId,
      favoriteId: songs.favoriteId
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(contentType: string, base64String: string): void {
    this.dataUtils.openFile(contentType, base64String);
  }

  setFileData(event: Event, field: string, isImage: boolean): void {
    this.dataUtils.loadFileToForm(event, this.editForm, field, isImage).subscribe(null, (err: JhiFileLoadError) => {
      this.eventManager.broadcast(
        new JhiEventWithContent<AlertError>('serverApp.error', { message: err.message })
      );
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const songs = this.createFromForm();
    if (songs.id !== undefined) {
      this.subscribeToSaveResponse(this.songsService.update(songs));
    } else {
      this.subscribeToSaveResponse(this.songsService.create(songs));
    }
  }

  private createFromForm(): ISongs {
    return {
      ...new Songs(),
      id: this.editForm.get(['id'])!.value,
      title: this.editForm.get(['title'])!.value,
      genre: this.editForm.get(['genre'])!.value,
      vocal: this.editForm.get(['vocal'])!.value,
      country: this.editForm.get(['country'])!.value,
      description: this.editForm.get(['description'])!.value,
      songAddress: this.editForm.get(['songAddress'])!.value,
      lyric: this.editForm.get(['lyric'])!.value,
      avatar: this.editForm.get(['avatar'])!.value,
      listenCount: this.editForm.get(['listenCount'])!.value,
      favoriteCount: this.editForm.get(['favoriteCount'])!.value,
      adsId: this.editForm.get(['adsId'])!.value,
      playlistId: this.editForm.get(['playlistId'])!.value,
      albumId: this.editForm.get(['albumId'])!.value,
      favoriteId: this.editForm.get(['favoriteId'])!.value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ISongs>>): void {
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

  trackById(index: number, item: SelectableEntity): any {
    return item.id;
  }
}
