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

import com.mycompany.myapp.domain.Posts;
import com.mycompany.myapp.domain.*; // for static metamodels
import com.mycompany.myapp.repository.PostsRepository;
import com.mycompany.myapp.repository.search.PostsSearchRepository;
import com.mycompany.myapp.service.dto.PostsCriteria;
import com.mycompany.myapp.service.dto.PostsDTO;
import com.mycompany.myapp.service.mapper.PostsMapper;

/**
 * Service for executing complex queries for {@link Posts} entities in the database.
 * The main input is a {@link PostsCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link PostsDTO} or a {@link Page} of {@link PostsDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class PostsQueryService extends QueryService<Posts> {

    private final Logger log = LoggerFactory.getLogger(PostsQueryService.class);

    private final PostsRepository postsRepository;

    private final PostsMapper postsMapper;

    private final PostsSearchRepository postsSearchRepository;

    public PostsQueryService(PostsRepository postsRepository, PostsMapper postsMapper, PostsSearchRepository postsSearchRepository) {
        this.postsRepository = postsRepository;
        this.postsMapper = postsMapper;
        this.postsSearchRepository = postsSearchRepository;
    }

    /**
     * Return a {@link List} of {@link PostsDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<PostsDTO> findByCriteria(PostsCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Posts> specification = createSpecification(criteria);
        return postsMapper.toDto(postsRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link PostsDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<PostsDTO> findByCriteria(PostsCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Posts> specification = createSpecification(criteria);
        return postsRepository.findAll(specification, page)
            .map(postsMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(PostsCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Posts> specification = createSpecification(criteria);
        return postsRepository.count(specification);
    }

    /**
     * Function to convert {@link PostsCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Posts> createSpecification(PostsCriteria criteria) {
        Specification<Posts> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Posts_.id));
            }
            if (criteria.getTitle() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTitle(), Posts_.title));
            }
            if (criteria.getContent() != null) {
                specification = specification.and(buildStringSpecification(criteria.getContent(), Posts_.content));
            }
            if (criteria.getComment() != null) {
                specification = specification.and(buildStringSpecification(criteria.getComment(), Posts_.comment));
            }
            if (criteria.getImage() != null) {
                specification = specification.and(buildStringSpecification(criteria.getImage(), Posts_.image));
            }
            if (criteria.getLike() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getLike(), Posts_.like));
            }
            if (criteria.getSongAddress() != null) {
                specification = specification.and(buildStringSpecification(criteria.getSongAddress(), Posts_.songAddress));
            }
            if (criteria.getPagesId() != null) {
                specification = specification.and(buildSpecification(criteria.getPagesId(),
                    root -> root.join(Posts_.pages, JoinType.LEFT).get(Pages_.id)));
            }
        }
        return specification;
    }
}
