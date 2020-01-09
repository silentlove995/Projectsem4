package com.mycompany.myapp.service.dto;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.mycompany.myapp.domain.Posts} entity.
 */
public class PostsDTO implements Serializable {

    private Long id;

    private String title;

    private String content;

    private String comment;

    private String image;

    private Integer like;

    private String songAddress;


    private Long pagesId;

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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Integer getLike() {
        return like;
    }

    public void setLike(Integer like) {
        this.like = like;
    }

    public String getSongAddress() {
        return songAddress;
    }

    public void setSongAddress(String songAddress) {
        this.songAddress = songAddress;
    }

    public Long getPagesId() {
        return pagesId;
    }

    public void setPagesId(Long pagesId) {
        this.pagesId = pagesId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        PostsDTO postsDTO = (PostsDTO) o;
        if (postsDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), postsDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "PostsDTO{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", content='" + getContent() + "'" +
            ", comment='" + getComment() + "'" +
            ", image='" + getImage() + "'" +
            ", like=" + getLike() +
            ", songAddress='" + getSongAddress() + "'" +
            ", pagesId=" + getPagesId() +
            "}";
    }
}
