import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IAdsSong } from 'app/shared/model/ads-song.model';

@Component({
  selector: 'jhi-ads-song-detail',
  templateUrl: './ads-song-detail.component.html'
})
export class AdsSongDetailComponent implements OnInit {
  adsSong: IAdsSong | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ adsSong }) => {
      this.adsSong = adsSong;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
