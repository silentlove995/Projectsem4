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

import com.mycompany.myapp.domain.AdsPlaylist;
import com.mycompany.myapp.domain.*; // for static metamodels
import com.mycompany.myapp.repository.AdsPlaylistRepository;
import com.mycompany.myapp.repository.search.AdsPlaylistSearchRepository;
import com.mycompany.myapp.service.dto.AdsPlaylistCriteria;
import com.mycompany.myapp.service.dto.AdsPlaylistDTO;
import com.mycompany.myapp.service.mapper.AdsPlaylistMapper;

/**
 * Service for executing complex queries for {@link AdsPlaylist} entities in the database.
 * The main input is a {@link AdsPlaylistCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link AdsPlaylistDTO} or a {@link Page} of {@link AdsPlaylistDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class AdsPlaylistQueryService extends QueryService<AdsPlaylist> {

    private final Logger log = LoggerFactory.getLogger(AdsPlaylistQueryService.class);

    private final AdsPlaylistRepository adsPlaylistRepository;

    private final AdsPlaylistMapper adsPlaylistMapper;

    private final AdsPlaylistSearchRepository adsPlaylistSearchRepository;

    public AdsPlaylistQueryService(AdsPlaylistRepository adsPlaylistRepository, AdsPlaylistMapper adsPlaylistMapper, AdsPlaylistSearchRepository adsPlaylistSearchRepository) {
        this.adsPlaylistRepository = adsPlaylistRepository;
        this.adsPlaylistMapper = adsPlaylistMapper;
        this.adsPlaylistSearchRepository = adsPlaylistSearchRepository;
    }

    /**
     * Return a {@link List} of {@link AdsPlaylistDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<AdsPlaylistDTO> findByCriteria(AdsPlaylistCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<AdsPlaylist> specification = createSpecification(criteria);
        return adsPlaylistMapper.toDto(adsPlaylistRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link AdsPlaylistDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<AdsPlaylistDTO> findByCriteria(AdsPlaylistCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<AdsPlaylist> specification = createSpecification(criteria);
        return adsPlaylistRepository.findAll(specification, page)
            .map(adsPlaylistMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(AdsPlaylistCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<AdsPlaylist> specification = createSpecification(criteria);
        return adsPlaylistRepository.count(specification);
    }

    /**
     * Function to convert {@link AdsPlaylistCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<AdsPlaylist> createSpecification(AdsPlaylistCriteria criteria) {
        Specification<AdsPlaylist> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), AdsPlaylist_.id));
            }
            if (criteria.getAdsImage() != null) {
                specification = specification.and(buildStringSpecification(criteria.getAdsImage(), AdsPlaylist_.adsImage));
            }
            if (criteria.getAdsContent() != null) {
                specification = specification.and(buildStringSpecification(criteria.getAdsContent(), AdsPlaylist_.adsContent));
            }
            if (criteria.getPlaylistId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPlaylistId(), AdsPlaylist_.playlistId));
            }
            if (criteria.getPlaylistTitle() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPlaylistTitle(), AdsPlaylist_.playlistTitle));
            }
            if (criteria.getPlaylistImage() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPlaylistImage(), AdsPlaylist_.playlistImage));
            }
        }
        return specification;
    }
}
