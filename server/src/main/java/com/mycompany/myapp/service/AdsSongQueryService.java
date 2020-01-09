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

import com.mycompany.myapp.domain.AdsSong;
import com.mycompany.myapp.domain.*; // for static metamodels
import com.mycompany.myapp.repository.AdsSongRepository;
import com.mycompany.myapp.repository.search.AdsSongSearchRepository;
import com.mycompany.myapp.service.dto.AdsSongCriteria;
import com.mycompany.myapp.service.dto.AdsSongDTO;
import com.mycompany.myapp.service.mapper.AdsSongMapper;

/**
 * Service for executing complex queries for {@link AdsSong} entities in the database.
 * The main input is a {@link AdsSongCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link AdsSongDTO} or a {@link Page} of {@link AdsSongDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class AdsSongQueryService extends QueryService<AdsSong> {

    private final Logger log = LoggerFactory.getLogger(AdsSongQueryService.class);

    private final AdsSongRepository adsSongRepository;

    private final AdsSongMapper adsSongMapper;

    private final AdsSongSearchRepository adsSongSearchRepository;

    public AdsSongQueryService(AdsSongRepository adsSongRepository, AdsSongMapper adsSongMapper, AdsSongSearchRepository adsSongSearchRepository) {
        this.adsSongRepository = adsSongRepository;
        this.adsSongMapper = adsSongMapper;
        this.adsSongSearchRepository = adsSongSearchRepository;
    }

    /**
     * Return a {@link List} of {@link AdsSongDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<AdsSongDTO> findByCriteria(AdsSongCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<AdsSong> specification = createSpecification(criteria);
        return adsSongMapper.toDto(adsSongRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link AdsSongDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<AdsSongDTO> findByCriteria(AdsSongCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<AdsSong> specification = createSpecification(criteria);
        return adsSongRepository.findAll(specification, page)
            .map(adsSongMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(AdsSongCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<AdsSong> specification = createSpecification(criteria);
        return adsSongRepository.count(specification);
    }

    /**
     * Function to convert {@link AdsSongCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<AdsSong> createSpecification(AdsSongCriteria criteria) {
        Specification<AdsSong> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), AdsSong_.id));
            }
            if (criteria.getAdsImage() != null) {
                specification = specification.and(buildStringSpecification(criteria.getAdsImage(), AdsSong_.adsImage));
            }
            if (criteria.getAdsContent() != null) {
                specification = specification.and(buildStringSpecification(criteria.getAdsContent(), AdsSong_.adsContent));
            }
            if (criteria.getSongId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getSongId(), AdsSong_.songId));
            }
            if (criteria.getSongTitle() != null) {
                specification = specification.and(buildStringSpecification(criteria.getSongTitle(), AdsSong_.songTitle));
            }
            if (criteria.getSongImage() != null) {
                specification = specification.and(buildStringSpecification(criteria.getSongImage(), AdsSong_.songImage));
            }
        }
        return specification;
    }
}
