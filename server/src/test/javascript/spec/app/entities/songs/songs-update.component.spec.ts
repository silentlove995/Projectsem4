import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { ServerTestModule } from '../../../test.module';
import { SongsUpdateComponent } from 'app/entities/songs/songs-update.component';
import { SongsService } from 'app/entities/songs/songs.service';
import { Songs } from 'app/shared/model/songs.model';

describe('Component Tests', () => {
  describe('Songs Management Update Component', () => {
    let comp: SongsUpdateComponent;
    let fixture: ComponentFixture<SongsUpdateComponent>;
    let service: SongsService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [ServerTestModule],
        declarations: [SongsUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(SongsUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(SongsUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(SongsService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new Songs(123);
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
        const entity = new Songs();
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
