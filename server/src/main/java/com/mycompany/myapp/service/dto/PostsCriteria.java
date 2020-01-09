package com.mycompany.myapp.service.dto;

import java.io.Serializable;
import java.util.Objects;
import io.github.jhipster.service.Criteria;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link com.mycompany.myapp.domain.Posts} entity. This class is used
 * in {@link com.mycompany.myapp.web.rest.PostsResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /posts?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class PostsCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter title;

    private StringFilter content;

    private StringFilter comment;

    private StringFilter image;

    private IntegerFilter like;

    private StringFilter songAddress;

    private LongFilter pagesId;

    public PostsCriteria(){
    }

    public PostsCriteria(PostsCriteria other){
        this.id = other.id == null ? null : other.id.copy();
        this.title = other.title == null ? null : other.title.copy();
        this.content = other.content == null ? null : other.content.copy();
        this.comment = other.comment == null ? null : other.comment.copy();
        this.image = other.image == null ? null : other.image.copy();
        this.like = other.like == null ? null : other.like.copy();
        this.songAddress = other.songAddress == null ? null : other.songAddress.copy();
        this.pagesId = other.pagesId == null ? null : other.pagesId.copy();
    }

    @Override
    public PostsCriteria copy() {
        return new PostsCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getTitle() {
        return title;
    }

    public void setTitle(StringFilter title) {
        this.title = title;
    }

    public StringFilter getContent() {
        return content;
    }

    public void setContent(StringFilter content) {
        this.content = content;
    }

    public StringFilter getComment() {
        return comment;
    }

    public void setComment(StringFilter comment) {
        this.comment = comment;
    }

    public StringFilter getImage() {
        return image;
    }

    public void setImage(StringFilter image) {
        this.image = image;
    }

    public IntegerFilter getLike() {
        return like;
    }

    public void setLike(IntegerFilter like) {
        this.like = like;
    }

    public StringFilter getSongAddress() {
        return songAddress;
    }

    public void setSongAddress(StringFilter songAddress) {
        this.songAddress = songAddress;
    }

    public LongFilter getPagesId() {
        return pagesId;
    }

    public void setPagesId(LongFilter pagesId) {
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
        final PostsCriteria that = (PostsCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(title, that.title) &&
            Objects.equals(content, that.content) &&
            Objects.equals(comment, that.comment) &&
            Objects.equals(image, that.image) &&
            Objects.equals(like, that.like) &&
            Objects.equals(songAddress, that.songAddress) &&
            Objects.equals(pagesId, that.pagesId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        title,
        content,
        comment,
        image,
        like,
        songAddress,
        pagesId
        );
    }

    @Override
    public String toString() {
        return "PostsCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (title != null ? "title=" + title + ", " : "") +
                (content != null ? "content=" + content + ", " : "") +
                (comment != null ? "comment=" + comment + ", " : "") +
                (image != null ? "image=" + image + ", " : "") +
                (like != null ? "like=" + like + ", " : "") +
                (songAddress != null ? "songAddress=" + songAddress + ", " : "") +
                (pagesId != null ? "pagesId=" + pagesId + ", " : "") +
            "}";
    }

}
