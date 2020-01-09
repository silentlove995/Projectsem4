package com.mycompany.myapp.domain;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;

import org.springframework.data.elasticsearch.annotations.FieldType;
import java.io.Serializable;

/**
 * A Posts.
 */
@Entity
@Table(name = "posts")
@org.springframework.data.elasticsearch.annotations.Document(indexName = "posts")
public class Posts implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @org.springframework.data.elasticsearch.annotations.Field(type = FieldType.Keyword)
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "content")
    private String content;

    @Column(name = "comment")
    private String comment;

    @Column(name = "image")
    private String image;

    @Column(name = "jhi_like")
    private Integer like;

    @Column(name = "song_address")
    private String songAddress;

    @ManyToOne
    @JsonIgnoreProperties("titles")
    private Pages pages;

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

    public Posts title(String title) {
        this.title = title;
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public Posts content(String content) {
        this.content = content;
        return this;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getComment() {
        return comment;
    }

    public Posts comment(String comment) {
        this.comment = comment;
        return this;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getImage() {
        return image;
    }

    public Posts image(String image) {
        this.image = image;
        return this;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Integer getLike() {
        return like;
    }

    public Posts like(Integer like) {
        this.like = like;
        return this;
    }

    public void setLike(Integer like) {
        this.like = like;
    }

    public String getSongAddress() {
        return songAddress;
    }

    public Posts songAddress(String songAddress) {
        this.songAddress = songAddress;
        return this;
    }

    public void setSongAddress(String songAddress) {
        this.songAddress = songAddress;
    }

    public Pages getPages() {
        return pages;
    }

    public Posts pages(Pages pages) {
        this.pages = pages;
        return this;
    }

    public void setPages(Pages pages) {
        this.pages = pages;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Posts)) {
            return false;
        }
        return id != null && id.equals(((Posts) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Posts{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", content='" + getContent() + "'" +
            ", comment='" + getComment() + "'" +
            ", image='" + getImage() + "'" +
            ", like=" + getLike() +
            ", songAddress='" + getSongAddress() + "'" +
            "}";
    }
}
