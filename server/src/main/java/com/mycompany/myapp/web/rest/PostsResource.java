package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.service.PostsService;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import com.mycompany.myapp.service.dto.PostsDTO;
import com.mycompany.myapp.service.dto.PostsCriteria;
import com.mycompany.myapp.service.PostsQueryService;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.Posts}.
 */
@RestController
@RequestMapping("/api")
public class PostsResource {

    private final Logger log = LoggerFactory.getLogger(PostsResource.class);

    private static final String ENTITY_NAME = "posts";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PostsService postsService;

    private final PostsQueryService postsQueryService;

    public PostsResource(PostsService postsService, PostsQueryService postsQueryService) {
        this.postsService = postsService;
        this.postsQueryService = postsQueryService;
    }

    /**
     * {@code POST  /posts} : Create a new posts.
     *
     * @param postsDTO the postsDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new postsDTO, or with status {@code 400 (Bad Request)} if the posts has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/posts")
    public ResponseEntity<PostsDTO> createPosts(@RequestBody PostsDTO postsDTO) throws URISyntaxException {
        log.debug("REST request to save Posts : {}", postsDTO);
        if (postsDTO.getId() != null) {
            throw new BadRequestAlertException("A new posts cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PostsDTO result = postsService.save(postsDTO);
        return ResponseEntity.created(new URI("/api/posts/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /posts} : Updates an existing posts.
     *
     * @param postsDTO the postsDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated postsDTO,
     * or with status {@code 400 (Bad Request)} if the postsDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the postsDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/posts")
    public ResponseEntity<PostsDTO> updatePosts(@RequestBody PostsDTO postsDTO) throws URISyntaxException {
        log.debug("REST request to update Posts : {}", postsDTO);
        if (postsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        PostsDTO result = postsService.save(postsDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, postsDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /posts} : get all the posts.
     *

     * @param pageable the pagination information.

     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of posts in body.
     */
    @GetMapping("/posts")
    public ResponseEntity<List<PostsDTO>> getAllPosts(PostsCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Posts by criteria: {}", criteria);
        Page<PostsDTO> page = postsQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
    * {@code GET  /posts/count} : count all the posts.
    *
    * @param criteria the criteria which the requested entities should match.
    * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
    */
    @GetMapping("/posts/count")
    public ResponseEntity<Long> countPosts(PostsCriteria criteria) {
        log.debug("REST request to count Posts by criteria: {}", criteria);
        return ResponseEntity.ok().body(postsQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /posts/:id} : get the "id" posts.
     *
     * @param id the id of the postsDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the postsDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/posts/{id}")
    public ResponseEntity<PostsDTO> getPosts(@PathVariable Long id) {
        log.debug("REST request to get Posts : {}", id);
        Optional<PostsDTO> postsDTO = postsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(postsDTO);
    }

    /**
     * {@code DELETE  /posts/:id} : delete the "id" posts.
     *
     * @param id the id of the postsDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/posts/{id}")
    public ResponseEntity<Void> deletePosts(@PathVariable Long id) {
        log.debug("REST request to delete Posts : {}", id);
        postsService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/posts?query=:query} : search for the posts corresponding
     * to the query.
     *
     * @param query the query of the posts search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/posts")
    public ResponseEntity<List<PostsDTO>> searchPosts(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Posts for query {}", query);
        Page<PostsDTO> page = postsService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
