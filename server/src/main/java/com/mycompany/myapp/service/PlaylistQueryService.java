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

import com.mycompany.myapp.domain.Playlist;
import com.mycompany.myapp.domain.*; // for static metamodels
import com.mycompany.myapp.repository.PlaylistRepository;
import com.mycompany.myapp.repository.search.PlaylistSearchRepository;
import com.mycompany.myapp.service.dto.PlaylistCriteria;
import com.mycompany.myapp.service.dto.PlaylistDTO;
import com.mycompany.myapp.service.mapper.PlaylistMapper;

/**
 * Service for executing complex queries for {@link Playlist} entities in the database.
 * The main input is a {@link PlaylistCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link PlaylistDTO} or a {@link Page} of {@link PlaylistDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class PlaylistQueryService extends QueryService<Playlist> {

    private final Logger log = LoggerFactory.getLogger(PlaylistQueryService.class);

    private final PlaylistRepository playlistRepository;

    private final PlaylistMapper playlistMapper;

    private final PlaylistSearchRepository playlistSearchRepository;

    public PlaylistQueryService(PlaylistRepository playlistRepository, PlaylistMapper playlistMapper, PlaylistSearchRepository playlistSearchRepository) {
        this.playlistRepository = playlistRepository;
        this.playlistMapper = playlistMapper;
        this.playlistSearchRepository = playlistSearchRepository;
    }

    /**
     * Return a {@link List} of {@link PlaylistDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<PlaylistDTO> findByCriteria(PlaylistCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Playlist> specification = createSpecification(criteria);
        return playlistMapper.toDto(playlistRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link PlaylistDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<PlaylistDTO> findByCriteria(PlaylistCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Playlist> specification = createSpecification(criteria);
        return playlistRepository.findAll(specification, page)
            .map(playlistMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(PlaylistCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Playlist> specification = createSpecification(criteria);
        return playlistRepository.count(specification);
    }

    /**
     * Function to convert {@link PlaylistCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Playlist> createSpecification(PlaylistCriteria criteria) {
        Specification<Playlist> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Playlist_.id));
            }
            if (criteria.getTitle() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTitle(), Playlist_.title));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), Playlist_.description));
            }
            if (criteria.getVocal() != null) {
                specification = specification.and(buildStringSpecification(criteria.getVocal(), Playlist_.vocal));
            }
            if (criteria.getThumbnail() != null) {
                specification = specification.and(buildStringSpecification(criteria.getThumbnail(), Playlist_.thumbnail));
            }
            if (criteria.getAdsId() != null) {
                specification = specification.and(buildSpecification(criteria.getAdsId(),
                    root -> root.join(Playlist_.ads, JoinType.LEFT).get(AdsPlaylist_.id)));
            }
            if (criteria.getSongId() != null) {
                specification = specification.and(buildSpecification(criteria.getSongId(),
                    root -> root.join(Playlist_.songs, JoinType.LEFT).get(Songs_.id)));
            }
        }
        return specification;
    }
}
