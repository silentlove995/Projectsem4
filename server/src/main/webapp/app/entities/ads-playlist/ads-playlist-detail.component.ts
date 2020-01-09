import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IAdsPlaylist } from 'app/shared/model/ads-playlist.model';

@Component({
  selector: 'jhi-ads-playlist-detail',
  templateUrl: './ads-playlist-detail.component.html'
})
export class AdsPlaylistDetailComponent implements OnInit {
  adsPlaylist: IAdsPlaylist | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ adsPlaylist }) => {
      this.adsPlaylist = adsPlaylist;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
