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
 * Criteria class for the {@link com.mycompany.myapp.domain.AdsPlaylist} entity. This class is used
 * in {@link com.mycompany.myapp.web.rest.AdsPlaylistResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /ads-playlists?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class AdsPlaylistCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter adsImage;

    private StringFilter adsContent;

    private IntegerFilter playlistId;

    private StringFilter playlistTitle;

    private StringFilter playlistImage;

    public AdsPlaylistCriteria(){
    }

    public AdsPlaylistCriteria(AdsPlaylistCriteria other){
        this.id = other.id == null ? null : other.id.copy();
        this.adsImage = other.adsImage == null ? null : other.adsImage.copy();
        this.adsContent = other.adsContent == null ? null : other.adsContent.copy();
        this.playlistId = other.playlistId == null ? null : other.playlistId.copy();
        this.playlistTitle = other.playlistTitle == null ? null : other.playlistTitle.copy();
        this.playlistImage = other.playlistImage == null ? null : other.playlistImage.copy();
    }

    @Override
    public AdsPlaylistCriteria copy() {
        return new AdsPlaylistCriteria(this);
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

    public IntegerFilter getPlaylistId() {
        return playlistId;
    }

    public void setPlaylistId(IntegerFilter playlistId) {
        this.playlistId = playlistId;
    }

    public StringFilter getPlaylistTitle() {
        return playlistTitle;
    }

    public void setPlaylistTitle(StringFilter playlistTitle) {
        this.playlistTitle = playlistTitle;
    }

    public StringFilter getPlaylistImage() {
        return playlistImage;
    }

    public void setPlaylistImage(StringFilter playlistImage) {
        this.playlistImage = playlistImage;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final AdsPlaylistCriteria that = (AdsPlaylistCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(adsImage, that.adsImage) &&
            Objects.equals(adsContent, that.adsContent) &&
            Objects.equals(playlistId, that.playlistId) &&
            Objects.equals(playlistTitle, that.playlistTitle) &&
            Objects.equals(playlistImage, that.playlistImage);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        adsImage,
        adsContent,
        playlistId,
        playlistTitle,
        playlistImage
        );
    }

    @Override
    public String toString() {
        return "AdsPlaylistCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (adsImage != null ? "adsImage=" + adsImage + ", " : "") +
                (adsContent != null ? "adsContent=" + adsContent + ", " : "") +
                (playlistId != null ? "playlistId=" + playlistId + ", " : "") +
                (playlistTitle != null ? "playlistTitle=" + playlistTitle + ", " : "") +
                (playlistImage != null ? "playlistImage=" + playlistImage + ", " : "") +
            "}";
    }

}
