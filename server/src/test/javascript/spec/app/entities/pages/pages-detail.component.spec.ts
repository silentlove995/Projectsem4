import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ServerTestModule } from '../../../test.module';
import { PagesDetailComponent } from 'app/entities/pages/pages-detail.component';
import { Pages } from 'app/shared/model/pages.model';

describe('Component Tests', () => {
  describe('Pages Management Detail Component', () => {
    let comp: PagesDetailComponent;
    let fixture: ComponentFixture<PagesDetailComponent>;
    const route = ({ data: of({ pages: new Pages(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [ServerTestModule],
        declarations: [PagesDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(PagesDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(PagesDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load pages on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.pages).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
