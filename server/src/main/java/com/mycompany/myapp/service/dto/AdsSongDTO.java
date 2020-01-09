package com.mycompany.myapp.service.dto;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.mycompany.myapp.domain.AdsSong} entity.
 */
public class AdsSongDTO implements Serializable {

    private Long id;

    private String adsImage;

    private String adsContent;

    private Integer songId;

    private String songTitle;

    private String songImage;


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

    public Integer getSongId() {
        return songId;
    }

    public void setSongId(Integer songId) {
        this.songId = songId;
    }

    public String getSongTitle() {
        return songTitle;
    }

    public void setSongTitle(String songTitle) {
        this.songTitle = songTitle;
    }

    public String getSongImage() {
        return songImage;
    }

    public void setSongImage(String songImage) {
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

        AdsSongDTO adsSongDTO = (AdsSongDTO) o;
        if (adsSongDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), adsSongDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "AdsSongDTO{" +
            "id=" + getId() +
            ", adsImage='" + getAdsImage() + "'" +
            ", adsContent='" + getAdsContent() + "'" +
            ", songId=" + getSongId() +
            ", songTitle='" + getSongTitle() + "'" +
            ", songImage='" + getSongImage() + "'" +
            "}";
    }
}
