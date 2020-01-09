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

import com.mycompany.myapp.domain.Pages;
import com.mycompany.myapp.domain.*; // for static metamodels
import com.mycompany.myapp.repository.PagesRepository;
import com.mycompany.myapp.repository.search.PagesSearchRepository;
import com.mycompany.myapp.service.dto.PagesCriteria;
import com.mycompany.myapp.service.dto.PagesDTO;
import com.mycompany.myapp.service.mapper.PagesMapper;

/**
 * Service for executing complex queries for {@link Pages} entities in the database.
 * The main input is a {@link PagesCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link PagesDTO} or a {@link Page} of {@link PagesDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class PagesQueryService extends QueryService<Pages> {

    private final Logger log = LoggerFactory.getLogger(PagesQueryService.class);

    private final PagesRepository pagesRepository;

    private final PagesMapper pagesMapper;

    private final PagesSearchRepository pagesSearchRepository;

    public PagesQueryService(PagesRepository pagesRepository, PagesMapper pagesMapper, PagesSearchRepository pagesSearchRepository) {
        this.pagesRepository = pagesRepository;
        this.pagesMapper = pagesMapper;
        this.pagesSearchRepository = pagesSearchRepository;
    }

    /**
     * Return a {@link List} of {@link PagesDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<PagesDTO> findByCriteria(PagesCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Pages> specification = createSpecification(criteria);
        return pagesMapper.toDto(pagesRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link PagesDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<PagesDTO> findByCriteria(PagesCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Pages> specification = createSpecification(criteria);
        return pagesRepository.findAll(specification, page)
            .map(pagesMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(PagesCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Pages> specification = createSpecification(criteria);
        return pagesRepository.count(specification);
    }

    /**
     * Function to convert {@link PagesCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Pages> createSpecification(PagesCriteria criteria) {
        Specification<Pages> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Pages_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Pages_.name));
            }
            if (criteria.getAvatar() != null) {
                specification = specification.and(buildStringSpecification(criteria.getAvatar(), Pages_.avatar));
            }
            if (criteria.getIdol() != null) {
                specification = specification.and(buildStringSpecification(criteria.getIdol(), Pages_.idol));
            }
            if (criteria.getTitleId() != null) {
                specification = specification.and(buildSpecification(criteria.getTitleId(),
                    root -> root.join(Pages_.titles, JoinType.LEFT).get(Posts_.id)));
            }
        }
        return specification;
    }
}
