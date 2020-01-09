package com.mycompany.myapp.domain;

import javax.persistence.*;

import org.springframework.data.elasticsearch.annotations.FieldType;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A Favorite.
 */
@Entity
@Table(name = "favorite")
@org.springframework.data.elasticsearch.annotations.Document(indexName = "favorite")
public class Favorite implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @org.springframework.data.elasticsearch.annotations.Field(type = FieldType.Keyword)
    private Long id;

    @Column(name = "jhi_user")
    private String user;

    @Column(name = "song")
    private String song;

    @OneToMany(mappedBy = "favorite")
    private Set<Songs> songs = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUser() {
        return user;
    }

    public Favorite user(String user) {
        this.user = user;
        return this;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getSong() {
        return song;
    }

    public Favorite song(String song) {
        this.song = song;
        return this;
    }

    public void setSong(String song) {
        this.song = song;
    }

    public Set<Songs> getSongs() {
        return songs;
    }

    public Favorite songs(Set<Songs> songs) {
        this.songs = songs;
        return this;
    }

    public Favorite addSong(Songs songs) {
        this.songs.add(songs);
        songs.setFavorite(this);
        return this;
    }

    public Favorite removeSong(Songs songs) {
        this.songs.remove(songs);
        songs.setFavorite(null);
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
        if (!(o instanceof Favorite)) {
            return false;
        }
        return id != null && id.equals(((Favorite) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Favorite{" +
            "id=" + getId() +
            ", user='" + getUser() + "'" +
            ", song='" + getSong() + "'" +
            "}";
    }
}
