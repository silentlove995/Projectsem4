import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ServerTestModule } from '../../../test.module';
import { AdsPlaylistDetailComponent } from 'app/entities/ads-playlist/ads-playlist-detail.component';
import { AdsPlaylist } from 'app/shared/model/ads-playlist.model';

describe('Component Tests', () => {
  describe('AdsPlaylist Management Detail Component', () => {
    let comp: AdsPlaylistDetailComponent;
    let fixture: ComponentFixture<AdsPlaylistDetailComponent>;
    const route = ({ data: of({ adsPlaylist: new AdsPlaylist(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [ServerTestModule],
        declarations: [AdsPlaylistDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(AdsPlaylistDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(AdsPlaylistDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load adsPlaylist on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.adsPlaylist).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
