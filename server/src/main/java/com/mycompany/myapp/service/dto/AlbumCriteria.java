package com.mycompany.myapp.service.dto;

import java.io.Serializable;
import java.util.Objects;
import io.github.jhipster.service.Criteria;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link com.mycompany.myapp.domain.Album} entity. This class is used
 * in {@link com.mycompany.myapp.web.rest.AlbumResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /albums?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class AlbumCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter title;

    private StringFilter description;

    private StringFilter vocal;

    private StringFilter thumbnail;

    private LongFilter songId;

    public AlbumCriteria(){
    }

    public AlbumCriteria(AlbumCriteria other){
        this.id = other.id == null ? null : other.id.copy();
        this.title = other.title == null ? null : other.title.copy();
        this.description = other.description == null ? null : other.description.copy();
        this.vocal = other.vocal == null ? null : other.vocal.copy();
        this.thumbnail = other.thumbnail == null ? null : other.thumbnail.copy();
        this.songId = other.songId == null ? null : other.songId.copy();
    }

    @Override
    public AlbumCriteria copy() {
        return new AlbumCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getTitle() {
        return title;
    }

    public void setTitle(StringFilter title) {
        this.title = title;
    }

    public StringFilter getDescription() {
        return description;
    }

    public void setDescription(StringFilter description) {
        this.description = description;
    }

    public StringFilter getVocal() {
        return vocal;
    }

    public void setVocal(StringFilter vocal) {
        this.vocal = vocal;
    }

    public StringFilter getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(StringFilter thumbnail) {
        this.thumbnail = thumbnail;
    }

    public LongFilter getSongId() {
        return songId;
    }

    public void setSongId(LongFilter songId) {
        this.songId = songId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final AlbumCriteria that = (AlbumCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(title, that.title) &&
            Objects.equals(description, that.description) &&
            Objects.equals(vocal, that.vocal) &&
            Objects.equals(thumbnail, that.thumbnail) &&
            Objects.equals(songId, that.songId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        title,
        description,
        vocal,
        thumbnail,
        songId
        );
    }

    @Override
    public String toString() {
        return "AlbumCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (title != null ? "title=" + title + ", " : "") +
                (description != null ? "description=" + description + ", " : "") +
                (vocal != null ? "vocal=" + vocal + ", " : "") +
                (thumbnail != null ? "thumbnail=" + thumbnail + ", " : "") +
                (songId != null ? "songId=" + songId + ", " : "") +
            "}";
    }

}
