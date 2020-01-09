import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ServerTestModule } from '../../../test.module';
import { AdsSongDetailComponent } from 'app/entities/ads-song/ads-song-detail.component';
import { AdsSong } from 'app/shared/model/ads-song.model';

describe('Component Tests', () => {
  describe('AdsSong Management Detail Component', () => {
    let comp: AdsSongDetailComponent;
    let fixture: ComponentFixture<AdsSongDetailComponent>;
    const route = ({ data: of({ adsSong: new AdsSong(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [ServerTestModule],
        declarations: [AdsSongDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(AdsSongDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(AdsSongDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load adsSong on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.adsSong).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
