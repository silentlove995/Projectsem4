import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { ServerTestModule } from '../../../test.module';
import { AdsSongUpdateComponent } from 'app/entities/ads-song/ads-song-update.component';
import { AdsSongService } from 'app/entities/ads-song/ads-song.service';
import { AdsSong } from 'app/shared/model/ads-song.model';

describe('Component Tests', () => {
  describe('AdsSong Management Update Component', () => {
    let comp: AdsSongUpdateComponent;
    let fixture: ComponentFixture<AdsSongUpdateComponent>;
    let service: AdsSongService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [ServerTestModule],
        declarations: [AdsSongUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(AdsSongUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(AdsSongUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(AdsSongService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new AdsSong(123);
        spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.update).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));

      it('Should call create service on save for new entity', fakeAsync(() => {
        // GIVEN
        const entity = new AdsSong();
        spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.create).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));
    });
  });
});
