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
 * Criteria class for the {@link com.mycompany.myapp.domain.AdsSong} entity. This class is used
 * in {@link com.mycompany.myapp.web.rest.AdsSongResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /ads-songs?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class AdsSongCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter adsImage;

    private StringFilter adsContent;

    private IntegerFilter songId;

    private StringFilter songTitle;

    private StringFilter songImage;

    public AdsSongCriteria(){
    }

    public AdsSongCriteria(AdsSongCriteria other){
        this.id = other.id == null ? null : other.id.copy();
        this.adsImage = other.adsImage == null ? null : other.adsImage.copy();
        this.adsContent = other.adsContent == null ? null : other.adsContent.copy();
        this.songId = other.songId == null ? null : other.songId.copy();
        this.songTitle = other.songTitle == null ? null : other.songTitle.copy();
        this.songImage = other.songImage == null ? null : other.songImage.copy();
    }

    @Override
    public AdsSongCriteria copy() {
        return new AdsSongCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getAdsImage() {
        return adsImage;
    }

    public void setAdsImage(StringFilter adsImage) {
        this.adsImage = adsImage;
    }

    public StringFilter getAdsContent() {
        return adsContent;
    }

    public void setAdsContent(StringFilter adsContent) {
        this.adsContent = adsContent;
    }

    public IntegerFilter getSongId() {
        return songId;
    }

    public void setSongId(IntegerFilter songId) {
        this.songId = songId;
    }

    public StringFilter getSongTitle() {
        return songTitle;
    }

    public void setSongTitle(StringFilter songTitle) {
        this.songTitle = songTitle;
    }

    public StringFilter getSongImage() {
        return songImage;
    }

    public void setSongImage(StringFilter songImage) {
        this.songImage = songImage;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final AdsSongCriteria that = (AdsSongCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(adsImage, that.adsImage) &&
            Objects.equals(adsContent, that.adsContent) &&
            Objects.equals(songId, that.songId) &&
            Objects.equals(songTitle, that.songTitle) &&
            Objects.equals(songImage, that.songImage);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        adsImage,
        adsContent,
        songId,
        songTitle,
        songImage
        );
    }

    @Override
    public String toString() {
        return "AdsSongCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (adsImage != null ? "adsImage=" + adsImage + ", " : "") +
                (adsContent != null ? "adsContent=" + adsContent + ", " : "") +
                (songId != null ? "songId=" + songId + ", " : "") +
                (songTitle != null ? "songTitle=" + songTitle + ", " : "") +
                (songImage != null ? "songImage=" + songImage + ", " : "") +
            "}";
    }

}
