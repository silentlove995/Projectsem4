import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { ServerTestModule } from '../../../test.module';
import { AdsPlaylistUpdateComponent } from 'app/entities/ads-playlist/ads-playlist-update.component';
import { AdsPlaylistService } from 'app/entities/ads-playlist/ads-playlist.service';
import { AdsPlaylist } from 'app/shared/model/ads-playlist.model';

describe('Component Tests', () => {
  describe('AdsPlaylist Management Update Component', () => {
    let comp: AdsPlaylistUpdateComponent;
    let fixture: ComponentFixture<AdsPlaylistUpdateComponent>;
    let service: AdsPlaylistService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [ServerTestModule],
        declarations: [AdsPlaylistUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(AdsPlaylistUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(AdsPlaylistUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(AdsPlaylistService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new AdsPlaylist(123);
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
        const entity = new AdsPlaylist();
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
