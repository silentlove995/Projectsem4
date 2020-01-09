package com.mycompany.myapp.domain;

import javax.persistence.*;

import org.springframework.data.elasticsearch.annotations.FieldType;
import java.io.Serializable;

/**
 * A AdsPlaylist.
 */
@Entity
@Table(name = "ads_playlist")
@org.springframework.data.elasticsearch.annotations.Document(indexName = "adsplaylist")
public class AdsPlaylist implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @org.springframework.data.elasticsearch.annotations.Field(type = FieldType.Keyword)
    private Long id;

    @Column(name = "ads_image")
    private String adsImage;

    @Column(name = "ads_content")
    private String adsContent;

    @Column(name = "playlist_id")
    private Integer playlistId;

    @Column(name = "playlist_title")
    private String playlistTitle;

    @Column(name = "playlist_image")
    private String playlistImage;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAdsImage() {
        return adsImage;
    }

    public AdsPlaylist adsImage(String adsImage) {
        this.adsImage = adsImage;
        return this;
    }

    public void setAdsImage(String adsImage) {
        this.adsImage = adsImage;
    }

    public String getAdsContent() {
        return adsContent;
    }

    public AdsPlaylist adsContent(String adsContent) {
        this.adsContent = adsContent;
        return this;
    }

    public void setAdsContent(String adsContent) {
        this.adsContent = adsContent;
    }

    public Integer getPlaylistId() {
        return playlistId;
    }

    public AdsPlaylist playlistId(Integer playlistId) {
        this.playlistId = playlistId;
        return this;
    }

    public void setPlaylistId(Integer playlistId) {
        this.playlistId = playlistId;
    }

    public String getPlaylistTitle() {
        return playlistTitle;
    }

    public AdsPlaylist playlistTitle(String playlistTitle) {
        this.playlistTitle = playlistTitle;
        return this;
    }

    public void setPlaylistTitle(String playlistTitle) {
        this.playlistTitle = playlistTitle;
    }

    public String getPlaylistImage() {
        return playlistImage;
    }

    public AdsPlaylist playlistImage(String playlistImage) {
        this.playlistImage = playlistImage;
        return this;
    }

    public void setPlaylistImage(String playlistImage) {
        this.playlistImage = playlistImage;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AdsPlaylist)) {
            return false;
        }
        return id != null && id.equals(((AdsPlaylist) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "AdsPlaylist{" +
            "id=" + getId() +
            ", adsImage='" + getAdsImage() + "'" +
            ", adsContent='" + getAdsContent() + "'" +
            ", playlistId=" + getPlaylistId() +
            ", playlistTitle='" + getPlaylistTitle() + "'" +
            ", playlistImage='" + getPlaylistImage() + "'" +
            "}";
    }
}
