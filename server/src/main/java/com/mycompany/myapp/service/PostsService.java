package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Posts;
import com.mycompany.myapp.repository.PostsRepository;
import com.mycompany.myapp.repository.search.PostsSearchRepository;
import com.mycompany.myapp.service.dto.PostsDTO;
import com.mycompany.myapp.service.mapper.PostsMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing {@link Posts}.
 */
@Service
@Transactional
public class PostsService {

    private final Logger log = LoggerFactory.getLogger(PostsService.class);

    private final PostsRepository postsRepository;

    private final PostsMapper postsMapper;

    private final PostsSearchRepository postsSearchRepository;

    public PostsService(PostsRepository postsRepository, PostsMapper postsMapper, PostsSearchRepository postsSearchRepository) {
        this.postsRepository = postsRepository;
        this.postsMapper = postsMapper;
        this.postsSearchRepository = postsSearchRepository;
    }

    /**
     * Save a posts.
     *
     * @param postsDTO the entity to save.
     * @return the persisted entity.
     */
    public PostsDTO save(PostsDTO postsDTO) {
        log.debug("Request to save Posts : {}", postsDTO);
        Posts posts = postsMapper.toEntity(postsDTO);
        posts = postsRepository.save(posts);
        PostsDTO result = postsMapper.toDto(posts);
        postsSearchRepository.save(posts);
        return result;
    }

    /**
     * Get all the posts.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<PostsDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Posts");
        return postsRepository.findAll(pageable)
            .map(postsMapper::toDto);
    }


    /**
     * Get one posts by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<PostsDTO> findOne(Long id) {
        log.debug("Request to get Posts : {}", id);
        return postsRepository.findById(id)
            .map(postsMapper::toDto);
    }

    /**
     * Delete the posts by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Posts : {}", id);
        postsRepository.deleteById(id);
        postsSearchRepository.deleteById(id);
    }

    /**
     * Search for the posts corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<PostsDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Posts for query {}", query);
        return postsSearchRepository.search(queryStringQuery(query), pageable)
            .map(postsMapper::toDto);
    }
}
