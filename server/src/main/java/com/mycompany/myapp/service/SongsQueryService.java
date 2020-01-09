package com.mycompany.myapp.service;

import java.util.List;

import javax.persistence.criteria.JoinType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.service.QueryService;

import com.mycompany.myapp.domain.Songs;
import com.mycompany.myapp.domain.*; // for static metamodels
import com.mycompany.myapp.repository.SongsRepository;
import com.mycompany.myapp.repository.search.SongsSearchRepository;
import com.mycompany.myapp.service.dto.SongsCriteria;
import com.mycompany.myapp.service.dto.SongsDTO;
import com.mycompany.myapp.service.mapper.SongsMapper;

/**
 * Service for executing complex queries for {@link Songs} entities in the database.
 * The main input is a {@link SongsCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link SongsDTO} or a {@link Page} of {@link SongsDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class SongsQueryService extends QueryService<Songs> {

    private final Logger log = LoggerFactory.getLogger(SongsQueryService.class);

    private final SongsRepository songsRepository;

    private final SongsMapper songsMapper;

    private final SongsSearchRepository songsSearchRepository;

    public SongsQueryService(SongsRepository songsRepository, SongsMapper songsMapper, SongsSearchRepository songsSearchRepository) {
        this.songsRepository = songsRepository;
        this.songsMapper = songsMapper;
        this.songsSearchRepository = songsSearchRepository;
    }

    /**
     * Return a {@link List} of {@link SongsDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<SongsDTO> findByCriteria(SongsCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Songs> specification = createSpecification(criteria);
        return songsMapper.toDto(songsRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link SongsDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<SongsDTO> findByCriteria(SongsCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Songs> specification = createSpecification(criteria);
        return songsRepository.findAll(specification, page)
            .map(songsMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(SongsCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Songs> specification = createSpecification(criteria);
        return songsRepository.count(specification);
    }

    /**
     * Function to convert {@link SongsCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Songs> createSpecification(SongsCriteria criteria) {
        Specification<Songs> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Songs_.id));
            }
            if (criteria.getTitle() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTitle(), Songs_.title));
            }
            if (criteria.getGenre() != null) {
                specification = specification.and(buildSpecification(criteria.getGenre(), Songs_.genre));
            }
            if (criteria.getVocal() != null) {
                specification = specification.and(buildStringSpecification(criteria.getVocal(), Songs_.vocal));
            }
            if (criteria.getCountry() != null) {
                specification = specification.and(buildSpecification(criteria.getCountry(), Songs_.country));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), Songs_.description));
            }
            if (criteria.getSongAddress() != null) {
                specification = specification.and(buildStringSpecification(criteria.getSongAddress(), Songs_.songAddress));
            }
            if (criteria.getAvatar() != null) {
                specification = specification.and(buildStringSpecification(criteria.getAvatar(), Songs_.avatar));
            }
            if (criteria.getListenCount() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getListenCount(), Songs_.listenCount));
            }
            if (criteria.getFavoriteCount() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getFavoriteCount(), Songs_.favoriteCount));
            }
            if (criteria.getAdsId() != null) {
                specification = specification.and(buildSpecification(criteria.getAdsId(),
                    root -> root.join(Songs_.ads, JoinType.LEFT).get(AdsSong_.id)));
            }
            if (criteria.getPlaylistId() != null) {
                specification = specification.and(buildSpecification(criteria.getPlaylistId(),
                    root -> root.join(Songs_.playlist, JoinType.LEFT).get(Playlist_.id)));
            }
            if (criteria.getAlbumId() != null) {
                specification = specification.and(buildSpecification(criteria.getAlbumId(),
                    root -> root.join(Songs_.album, JoinType.LEFT).get(Album_.id)));
            }
            if (criteria.getFavoriteId() != null) {
                specification = specification.and(buildSpecification(criteria.getFavoriteId(),
                    root -> root.join(Songs_.favorite, JoinType.LEFT).get(Favorite_.id)));
            }
        }
        return specification;
    }
}
