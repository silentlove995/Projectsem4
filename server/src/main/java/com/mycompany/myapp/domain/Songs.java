package com.mycompany.myapp.domain;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Type;

import javax.persistence.*;

import org.springframework.data.elasticsearch.annotations.FieldType;
import java.io.Serializable;

import com.mycompany.myapp.domain.enumeration.Genre;

import com.mycompany.myapp.domain.enumeration.Country;

/**
 * A Songs.
 */
@Entity
@Table(name = "songs")
@org.springframework.data.elasticsearch.annotations.Document(indexName = "songs")
public class Songs implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @org.springframework.data.elasticsearch.annotations.Field(type = FieldType.Keyword)
    private Long id;

    @Column(name = "title")
    private String title;

    @Enumerated(EnumType.STRING)
    @Column(name = "genre")
    private Genre genre;

    @Column(name = "vocal")
    private String vocal;

    @Enumerated(EnumType.STRING)
    @Column(name = "country")
    private Country country;

    @Column(name = "description")
    private String description;

    @Column(name = "song_address")
    private String songAddress;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "lyric")
    private String lyric;

    @Column(name = "avatar")
    private String avatar;

    @Column(name = "listen_count")
    private Integer listenCount;

    @Column(name = "favorite_count")
    private Integer favoriteCount;

    @OneToOne
    @JoinColumn(unique = true)
    private AdsSong ads;

    @ManyToOne
    @JsonIgnoreProperties("songs")
    private Playlist playlist;

    @ManyToOne
    @JsonIgnoreProperties("songs")
    private Album album;

    @ManyToOne
    @JsonIgnoreProperties("songs")
    private Favorite favorite;

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

    public Songs title(String title) {
        this.title = title;
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Genre getGenre() {
        return genre;
    }

    public Songs genre(Genre genre) {
        this.genre = genre;
        return this;
    }

    public void setGenre(Genre genre) {
        this.genre = genre;
    }

    public String getVocal() {
        return vocal;
    }

    public Songs vocal(String vocal) {
        this.vocal = vocal;
        return this;
    }

    public void setVocal(String vocal) {
        this.vocal = vocal;
    }

    public Country getCountry() {
        return country;
    }

    public Songs country(Country country) {
        this.country = country;
        return this;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    public String getDescription() {
        return description;
    }

    public Songs description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSongAddress() {
        return songAddress;
    }

    public Songs songAddress(String songAddress) {
        this.songAddress = songAddress;
        return this;
    }

    public void setSongAddress(String songAddress) {
        this.songAddress = songAddress;
    }

    public String getLyric() {
        return lyric;
    }

    public Songs lyric(String lyric) {
        this.lyric = lyric;
        return this;
    }

    public void setLyric(String lyric) {
        this.lyric = lyric;
    }

    public String getAvatar() {
        return avatar;
    }

    public Songs avatar(String avatar) {
        this.avatar = avatar;
        return this;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public Integer getListenCount() {
        return listenCount;
    }

    public Songs listenCount(Integer listenCount) {
        this.listenCount = listenCount;
        return this;
    }

    public void setListenCount(Integer listenCount) {
        this.listenCount = listenCount;
    }

    public Integer getFavoriteCount() {
        return favoriteCount;
    }

    public Songs favoriteCount(Integer favoriteCount) {
        this.favoriteCount = favoriteCount;
        return this;
    }

    public void setFavoriteCount(Integer favoriteCount) {
        this.favoriteCount = favoriteCount;
    }

    public AdsSong getAds() {
        return ads;
    }

    public Songs ads(AdsSong adsSong) {
        this.ads = adsSong;
        return this;
    }

    public void setAds(AdsSong adsSong) {
        this.ads = adsSong;
    }

    public Playlist getPlaylist() {
        return playlist;
    }

    public Songs playlist(Playlist playlist) {
        this.playlist = playlist;
        return this;
    }

    public void setPlaylist(Playlist playlist) {
        this.playlist = playlist;
    }

    public Album getAlbum() {
        return album;
    }

    public Songs album(Album album) {
        this.album = album;
        return this;
    }

    public void setAlbum(Album album) {
        this.album = album;
    }

    public Favorite getFavorite() {
        return favorite;
    }

    public Songs favorite(Favorite favorite) {
        this.favorite = favorite;
        return this;
    }

    public void setFavorite(Favorite favorite) {
        this.favorite = favorite;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Songs)) {
            return false;
        }
        return id != null && id.equals(((Songs) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Songs{" +
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
            "}";
    }
}
