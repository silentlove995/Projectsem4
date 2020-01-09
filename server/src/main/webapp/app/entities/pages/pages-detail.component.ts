import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IPages } from 'app/shared/model/pages.model';

@Component({
  selector: 'jhi-pages-detail',
  templateUrl: './pages-detail.component.html'
})
export class PagesDetailComponent implements OnInit {
  pages: IPages | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ pages }) => {
      this.pages = pages;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
