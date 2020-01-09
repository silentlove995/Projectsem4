package com.mycompany.myapp.service.dto;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.mycompany.myapp.domain.AdsPlaylist} entity.
 */
public class AdsPlaylistDTO implements Serializable {

    private Long id;

    private String adsImage;

    private String adsContent;

    private Integer playlistId;

    private String playlistTitle;

    private String playlistImage;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAdsImage() {
        return adsImage;
    }

    public void setAdsImage(String adsImage) {
        this.adsImage = adsImage;
    }

    public String getAdsContent() {
        return adsContent;
    }

    public void setAdsContent(String adsContent) {
        this.adsContent = adsContent;
    }

    public Integer getPlaylistId() {
        return playlistId;
    }

    public void setPlaylistId(Integer playlistId) {
        this.playlistId = playlistId;
    }

    public String getPlaylistTitle() {
        return playlistTitle;
    }

    public void setPlaylistTitle(String playlistTitle) {
        this.playlistTitle = playlistTitle;
    }

    public String getPlaylistImage() {
        return playlistImage;
    }

    public void setPlaylistImage(String playlistImage) {
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

        AdsPlaylistDTO adsPlaylistDTO = (AdsPlaylistDTO) o;
        if (adsPlaylistDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), adsPlaylistDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "AdsPlaylistDTO{" +
            "id=" + getId() +
            ", adsImage='" + getAdsImage() + "'" +
            ", adsContent='" + getAdsContent() + "'" +
            ", playlistId=" + getPlaylistId() +
            ", playlistTitle='" + getPlaylistTitle() + "'" +
            ", playlistImage='" + getPlaylistImage() + "'" +
            "}";
    }
}
