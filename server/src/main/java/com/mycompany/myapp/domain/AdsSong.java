package com.mycompany.myapp.domain;

import javax.persistence.*;

import org.springframework.data.elasticsearch.annotations.FieldType;
import java.io.Serializable;

/**
 * A AdsSong.
 */
@Entity
@Table(name = "ads_song")
@org.springframework.data.elasticsearch.annotations.Document(indexName = "adssong")
public class AdsSong implements Serializable {

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

    @Column(name = "song_id")
    private Integer songId;

    @Column(name = "song_title")
    private String songTitle;

    @Column(name = "song_image")
    private String songImage;

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

    public AdsSong adsImage(String adsImage) {
        this.adsImage = adsImage;
        return this;
    }

    public void setAdsImage(String adsImage) {
        this.adsImage = adsImage;
    }

    public String getAdsContent() {
        return adsContent;
    }

    public AdsSong adsContent(String adsContent) {
        this.adsContent = adsContent;
        return this;
    }

    public void setAdsContent(String adsContent) {
        this.adsContent = adsContent;
    }

    public Integer getSongId() {
        return songId;
    }

    public AdsSong songId(Integer songId) {
        this.songId = songId;
        return this;
    }

    public void setSongId(Integer songId) {
        this.songId = songId;
    }

    public String getSongTitle() {
        return songTitle;
    }

    public AdsSong songTitle(String songTitle) {
        this.songTitle = songTitle;
        return this;
    }

    public void setSongTitle(String songTitle) {
        this.songTitle = songTitle;
    }

    public String getSongImage() {
        return songImage;
    }

    public AdsSong songImage(String songImage) {
        this.songImage = songImage;
        return this;
    }

    public void setSongImage(String songImage) {
        this.songImage = songImage;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AdsSong)) {
            return false;
        }
        return id != null && id.equals(((AdsSong) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "AdsSong{" +
            "id=" + getId() +
            ", adsImage='" + getAdsImage() + "'" +
            ", adsContent='" + getAdsContent() + "'" +
            ", songId=" + getSongId() +
            ", songTitle='" + getSongTitle() + "'" +
            ", songImage='" + getSongImage() + "'" +
            "}";
    }
}
