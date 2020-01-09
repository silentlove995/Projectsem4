package com.mycompany.myapp.domain;

import javax.persistence.*;

import org.springframework.data.elasticsearch.annotations.FieldType;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A Playlist.
 */
@Entity
@Table(name = "playlist")
@org.springframework.data.elasticsearch.annotations.Document(indexName = "playlist")
public class Playlist implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @org.springframework.data.elasticsearch.annotations.Field(type = FieldType.Keyword)
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "vocal")
    private String vocal;

    @Column(name = "thumbnail")
    private String thumbnail;

    @OneToOne
    @JoinColumn(unique = true)
    private AdsPlaylist ads;

    @OneToMany(mappedBy = "playlist")
    private Set<Songs> songs = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public Playlist title(String title) {
        this.title = title;
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public Playlist description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getVocal() {
        return vocal;
    }

    public Playlist vocal(String vocal) {
        this.vocal = vocal;
        return this;
    }

    public void setVocal(String vocal) {
        this.vocal = vocal;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public Playlist thumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
        return this;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public AdsPlaylist getAds() {
        return ads;
    }

    public Playlist ads(AdsPlaylist adsPlaylist) {
        this.ads = adsPlaylist;
        return this;
    }

    public void setAds(AdsPlaylist adsPlaylist) {
        this.ads = adsPlaylist;
    }

    public Set<Songs> getSongs() {
        return songs;
    }

    public Playlist songs(Set<Songs> songs) {
        this.songs = songs;
        return this;
    }

    public Playlist addSong(Songs songs) {
        this.songs.add(songs);
        songs.setPlaylist(this);
        return this;
    }

    public Playlist removeSong(Songs songs) {
        this.songs.remove(songs);
        songs.setPlaylist(null);
        return this;
    }

    public void setSongs(Set<Songs> songs) {
        this.songs = songs;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Playlist)) {
            return false;
        }
        return id != null && id.equals(((Playlist) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Playlist{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", description='" + getDescription() + "'" +
            ", vocal='" + getVocal() + "'" +
            ", thumbnail='" + getThumbnail() + "'" +
            "}";
    }
}
