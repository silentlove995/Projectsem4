package com.mycompany.myapp.domain;

import javax.persistence.*;

import org.springframework.data.elasticsearch.annotations.FieldType;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A Pages.
 */
@Entity
@Table(name = "pages")
@org.springframework.data.elasticsearch.annotations.Document(indexName = "pages")
public class Pages implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @org.springframework.data.elasticsearch.annotations.Field(type = FieldType.Keyword)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "avatar")
    private String avatar;

    @Column(name = "idol")
    private String idol;

    @OneToMany(mappedBy = "pages")
    private Set<Posts> titles = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Pages name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvatar() {
        return avatar;
    }

    public Pages avatar(String avatar) {
        this.avatar = avatar;
        return this;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getIdol() {
        return idol;
    }

    public Pages idol(String idol) {
        this.idol = idol;
        return this;
    }

    public void setIdol(String idol) {
        this.idol = idol;
    }

    public Set<Posts> getTitles() {
        return titles;
    }

    public Pages titles(Set<Posts> posts) {
        this.titles = posts;
        return this;
    }

    public Pages addTitle(Posts posts) {
        this.titles.add(posts);
        posts.setPages(this);
        return this;
    }

    public Pages removeTitle(Posts posts) {
        this.titles.remove(posts);
        posts.setPages(null);
        return this;
    }

    public void setTitles(Set<Posts> posts) {
        this.titles = posts;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Pages)) {
            return false;
        }
        return id != null && id.equals(((Pages) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Pages{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", avatar='" + getAvatar() + "'" +
            ", idol='" + getIdol() + "'" +
            "}";
    }
}
