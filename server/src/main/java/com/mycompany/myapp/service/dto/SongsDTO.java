package com.mycompany.myapp.service.dto;
import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Lob;
import com.mycompany.myapp.domain.enumeration.Genre;
import com.mycompany.myapp.domain.enumeration.Country;

/**
 * A DTO for the {@link com.mycompany.myapp.domain.Songs} entity.
 */
public class SongsDTO implements Serializable {

    private Long id;

    private String title;

    private Genre genre;

    private String vocal;

    private Country country;

    private String description;

    private String songAddress;

    @Lob
    private String lyric;

    private String avatar;

    private Integer listenCount;

    private Integer favoriteCount;


    private Long adsId;

    private Long playlistId;

    private Long albumId;

    private Long favoriteId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Genre getGenre() {
        return genre;
    }

    public void setGenre(Genre genre) {
        this.genre = genre;
    }

    public String getVocal() {
        return vocal;
    }

    public void setVocal(String vocal) {
        this.vocal = vocal;
    }

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSongAddress() {
        return songAddress;
    }

    public void setSongAddress(String songAddress) {
        this.songAddress = songAddress;
    }

    public String getLyric() {
        return lyric;
    }

    public void setLyric(String lyric) {
        this.lyric = lyric;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public Integer getListenCount() {
        return listenCount;
    }

    public void setListenCount(Integer listenCount) {
        this.listenCount = listenCount;
    }

    public Integer getFavoriteCount() {
        return favoriteCount;
    }

    public void setFavoriteCount(Integer favoriteCount) {
        this.favoriteCount = favoriteCount;
    }

    public Long getAdsId() {
        return adsId;
    }

    public void setAdsId(Long adsSongId) {
        this.adsId = adsSongId;
    }

    public Long getPlaylistId() {
        return playlistId;
    }

    public void setPlaylistId(Long playlistId) {
        this.playlistId = playlistId;
    }

    public Long getAlbumId() {
        return albumId;
    }

    public void setAlbumId(Long albumId) {
        this.albumId = albumId;
    }

    public Long getFavoriteId() {
        return favoriteId;
    }

    public void setFavoriteId(Long favoriteId) {
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

        SongsDTO songsDTO = (SongsDTO) o;
        if (songsDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), songsDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "SongsDTO{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", genre='" + getGenre() + "'" +
            ", vocal='" + getVocal() + "'" +
            ", country='" + getCountry() + "'" +
            ", description='" + getDescription() + "'" +
            ", songAddress='" + getSongAddress() + "'" +
            ", lyric='" + getLyric() + "'" +
            ", avatar='" + getAvatar() + "'" +
            ", listenCount=" + getListenCount() +
            ", favoriteCount=" + getFavoriteCount() +
            ", adsId=" + getAdsId() +
            ", playlistId=" + getPlaylistId() +
            ", albumId=" + getAlbumId() +
            ", favoriteId=" + getFavoriteId() +
            "}";
    }
}
