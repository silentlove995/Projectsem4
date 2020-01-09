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
 * Criteria class for the {@link com.mycompany.myapp.domain.Pages} entity. This class is used
 * in {@link com.mycompany.myapp.web.rest.PagesResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /pages?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class PagesCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private StringFilter avatar;

    private StringFilter idol;

    private LongFilter titleId;

    public PagesCriteria(){
    }

    public PagesCriteria(PagesCriteria other){
        this.id = other.id == null ? null : other.id.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.avatar = other.avatar == null ? null : other.avatar.copy();
        this.idol = other.idol == null ? null : other.idol.copy();
        this.titleId = other.titleId == null ? null : other.titleId.copy();
    }

    @Override
    public PagesCriteria copy() {
        return new PagesCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getName() {
        return name;
    }

    public void setName(StringFilter name) {
        this.name = name;
    }

    public StringFilter getAvatar() {
        return avatar;
    }

    public void setAvatar(StringFilter avatar) {
        this.avatar = avatar;
    }

    public StringFilter getIdol() {
        return idol;
    }

    public void setIdol(StringFilter idol) {
        this.idol = idol;
    }

    public LongFilter getTitleId() {
        return titleId;
    }

    public void setTitleId(LongFilter titleId) {
        this.titleId = titleId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final PagesCriteria that = (PagesCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(avatar, that.avatar) &&
            Objects.equals(idol, that.idol) &&
            Objects.equals(titleId, that.titleId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        name,
        avatar,
        idol,
        titleId
        );
    }

    @Override
    public String toString() {
        return "PagesCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (name != null ? "name=" + name + ", " : "") +
                (avatar != null ? "avatar=" + avatar + ", " : "") +
                (idol != null ? "idol=" + idol + ", " : "") +
                (titleId != null ? "titleId=" + titleId + ", " : "") +
            "}";
    }

}
