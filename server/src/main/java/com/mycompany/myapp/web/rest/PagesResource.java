package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.service.PagesService;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import com.mycompany.myapp.service.dto.PagesDTO;
import com.mycompany.myapp.service.dto.PagesCriteria;
import com.mycompany.myapp.service.PagesQueryService;

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
 * REST controller for managing {@link com.mycompany.myapp.domain.Pages}.
 */
@RestController
@RequestMapping("/api")
public class PagesResource {

    private final Logger log = LoggerFactory.getLogger(PagesResource.class);

    private static final String ENTITY_NAME = "pages";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PagesService pagesService;

    private final PagesQueryService pagesQueryService;

    public PagesResource(PagesService pagesService, PagesQueryService pagesQueryService) {
        this.pagesService = pagesService;
        this.pagesQueryService = pagesQueryService;
    }

    /**
     * {@code POST  /pages} : Create a new pages.
     *
     * @param pagesDTO the pagesDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new pagesDTO, or with status {@code 400 (Bad Request)} if the pages has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/pages")
    public ResponseEntity<PagesDTO> createPages(@RequestBody PagesDTO pagesDTO) throws URISyntaxException {
        log.debug("REST request to save Pages : {}", pagesDTO);
        if (pagesDTO.getId() != null) {
            throw new BadRequestAlertException("A new pages cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PagesDTO result = pagesService.save(pagesDTO);
        return ResponseEntity.created(new URI("/api/pages/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /pages} : Updates an existing pages.
     *
     * @param pagesDTO the pagesDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated pagesDTO,
     * or with status {@code 400 (Bad Request)} if the pagesDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the pagesDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/pages")
    public ResponseEntity<PagesDTO> updatePages(@RequestBody PagesDTO pagesDTO) throws URISyntaxException {
        log.debug("REST request to update Pages : {}", pagesDTO);
        if (pagesDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        PagesDTO result = pagesService.save(pagesDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, pagesDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /pages} : get all the pages.
     *

     * @param pageable the pagination information.

     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of pages in body.
     */
    @GetMapping("/pages")
    public ResponseEntity<List<PagesDTO>> getAllPages(PagesCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Pages by criteria: {}", criteria);
        Page<PagesDTO> page = pagesQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
    * {@code GET  /pages/count} : count all the pages.
    *
    * @param criteria the criteria which the requested entities should match.
    * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
    */
    @GetMapping("/pages/count")
    public ResponseEntity<Long> countPages(PagesCriteria criteria) {
        log.debug("REST request to count Pages by criteria: {}", criteria);
        return ResponseEntity.ok().body(pagesQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /pages/:id} : get the "id" pages.
     *
     * @param id the id of the pagesDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the pagesDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/pages/{id}")
    public ResponseEntity<PagesDTO> getPages(@PathVariable Long id) {
        log.debug("REST request to get Pages : {}", id);
        Optional<PagesDTO> pagesDTO = pagesService.findOne(id);
        return ResponseUtil.wrapOrNotFound(pagesDTO);
    }

    /**
     * {@code DELETE  /pages/:id} : delete the "id" pages.
     *
     * @param id the id of the pagesDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/pages/{id}")
    public ResponseEntity<Void> deletePages(@PathVariable Long id) {
        log.debug("REST request to delete Pages : {}", id);
        pagesService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/pages?query=:query} : search for the pages corresponding
     * to the query.
     *
     * @param query the query of the pages search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/pages")
    public ResponseEntity<List<PagesDTO>> searchPages(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Pages for query {}", query);
        Page<PagesDTO> page = pagesService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
