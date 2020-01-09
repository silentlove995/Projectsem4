package com.mycompany.myapp.service.dto;

import java.io.Serializable;
import java.util.Objects;
import io.github.jhipster.service.Criteria;
import com.mycompany.myapp.domain.enumeration.Genre;
import com.mycompany.myapp.domain.enumeration.Country;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link com.mycompany.myapp.domain.Songs} entity. This class is used
 * in {@link com.mycompany.myapp.web.rest.SongsResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /songs?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class SongsCriteria implements Serializable, Criteria {
    /**
     * Class for filtering Genre
     */
    public static class GenreFilter extends Filter<Genre> {

        public GenreFilter() {
        }

        public GenreFilter(GenreFilter filter) {
            super(filter);
        }

        @Override
        public GenreFilter copy() {
            return new GenreFilter(this);
        }

    }
    /**
     * Class for filtering Country
     */
    public static class CountryFilter extends Filter<Country> {

        public CountryFilter() {
        }

        public CountryFilter(CountryFilter filter) {
            super(filter);
        }

        @Override
        public CountryFilter copy() {
            return new CountryFilter(this);
        }

    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter title;

    private GenreFilter genre;

    private StringFilter vocal;

    private CountryFilter country;

    private StringFilter description;

    private StringFilter songAddress;

    private StringFilter avatar;

    private IntegerFilter listenCount;

    private IntegerFilter favoriteCount;

    private LongFilter adsId;

    private LongFilter playlistId;

    private LongFilter albumId;

    private LongFilter favoriteId;

    public SongsCriteria(){
    }

    public SongsCriteria(SongsCriteria other){
        this.id = other.id == null ? null : other.id.copy();
        this.title = other.title == null ? null : other.title.copy();
        this.genre = other.genre == null ? null : other.genre.copy();
        this.vocal = other.vocal == null ? null : other.vocal.copy();
        this.country = other.country == null ? null : other.country.copy();
        this.description = other.description == null ? null : other.description.copy();
        this.songAddress = other.songAddress == null ? null : other.songAddress.copy();
        this.avatar = other.avatar == null ? null : other.avatar.copy();
        this.listenCount = other.listenCount == null ? null : other.listenCount.copy();
        this.favoriteCount = other.favoriteCount == null ? null : other.favoriteCount.copy();
        this.adsId = other.adsId == null ? null : other.adsId.copy();
        this.playlistId = other.playlistId == null ? null : other.playlistId.copy();
        this.albumId = other.albumId == null ? null : other.albumId.copy();
        this.favoriteId = other.favoriteId == null ? null : other.favoriteId.copy();
    }

    @Override
    public SongsCriteria copy() {
        return new SongsCriteria(this);
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

    public GenreFilter getGenre() {
        return genre;
    }

    public void setGenre(GenreFilter genre) {
        this.genre = genre;
    }

    public StringFilter getVocal() {
        return vocal;
    }

    public void setVocal(StringFilter vocal) {
        this.vocal = vocal;
    }

    public CountryFilter getCountry() {
        return country;
    }

    public void setCountry(CountryFilter country) {
        this.country = country;
    }

    public StringFilter getDescription() {
        return description;
    }

    public void setDescription(StringFilter description) {
        this.description = description;
    }

    public StringFilter getSongAddress() {
        return songAddress;
    }

    public void setSongAddress(StringFilter songAddress) {
        this.songAddress = songAddress;
    }

    public StringFilter getAvatar() {
        return avatar;
    }

    public void setAvatar(StringFilter avatar) {
        this.avatar = avatar;
    }

    public IntegerFilter getListenCount() {
        return listenCount;
    }

    public void setListenCount(IntegerFilter listenCount) {
        this.listenCount = listenCount;
    }

    public IntegerFilter getFavoriteCount() {
        return favoriteCount;
    }

    public void setFavoriteCount(IntegerFilter favoriteCount) {
        this.favoriteCount = favoriteCount;
    }

    public LongFilter getAdsId() {
        return adsId;
    }

    public void setAdsId(LongFilter adsId) {
        this.adsId = adsId;
    }

    public LongFilter getPlaylistId() {
        return playlistId;
    }

    public void setPlaylistId(LongFilter playlistId) {
        this.playlistId = playlistId;
    }

    public LongFilter getAlbumId() {
        return albumId;
    }

    public void setAlbumId(LongFilter albumId) {
        this.albumId = albumId;
    }

    public LongFilter getFavoriteId() {
        return favoriteId;
    }

    public void setFavoriteId(LongFilter favoriteId) {
        this.favoriteId = favoriteId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final SongsCriteria that = (SongsCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(title, that.title) &&
            Objects.equals(genre, that.genre) &&
            Objects.equals(vocal, that.vocal) &&
            Objects.equals(country, that.country) &&
            Objects.equals(description, that.description) &&
            Objects.equals(songAddress, that.songAddress) &&
            Objects.equals(avatar, that.avatar) &&
            Objects.equals(listenCount, that.listenCount) &&
            Objects.equals(favoriteCount, that.favoriteCount) &&
            Objects.equals(adsId, that.adsId) &&
            Objects.equals(playlistId, that.playlistId) &&
            Objects.equals(albumId, that.albumId) &&
            Objects.equals(favoriteId, that.favoriteId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        title,
        genre,
        vocal,
        country,
        description,
        songAddress,
        avatar,
        listenCount,
        favoriteCount,
        adsId,
        playlistId,
        albumId,
        favoriteId
        );
    }

    @Override
    public String toString() {
        return "SongsCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (title != null ? "title=" + title + ", " : "") +
                (genre != null ? "genre=" + genre + ", " : "") +
                (vocal != null ? "vocal=" + vocal + ", " : "") +
                (country != null ? "country=" + country + ", " : "") +
                (description != null ? "description=" + description + ", " : "") +
                (songAddress != null ? "songAddress=" + songAddress + ", " : "") +
                (avatar != null ? "avatar=" + avatar + ", " : "") +
                (listenCount != null ? "listenCount=" + listenCount + ", " : "") +
                (favoriteCount != null ? "favoriteCount=" + favoriteCount + ", " : "") +
                (adsId != null ? "adsId=" + adsId + ", " : "") +
                (playlistId != null ? "playlistId=" + playlistId + ", " : "") +
                (albumId != null ? "albumId=" + albumId + ", " : "") +
                (favoriteId != null ? "favoriteId=" + favoriteId + ", " : "") +
            "}";
    }

}
