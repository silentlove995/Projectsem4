package com.mycompany.myapp.service.dto;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.mycompany.myapp.domain.Favorite} entity.
 */
public class FavoriteDTO implements Serializable {

    private Long id;

    private String user;

    private String song;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getSong() {
        return song;
    }

    public void setSong(String song) {
        this.song = song;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        FavoriteDTO favoriteDTO = (FavoriteDTO) o;
        if (favoriteDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), favoriteDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "FavoriteDTO{" +
            "id=" + getId() +
            ", user='" + getUser() + "'" +
            ", song='" + getSong() + "'" +
            "}";
    }
}
