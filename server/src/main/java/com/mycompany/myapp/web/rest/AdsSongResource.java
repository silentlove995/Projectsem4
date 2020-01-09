package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.service.AdsSongService;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import com.mycompany.myapp.service.dto.AdsSongDTO;
import com.mycompany.myapp.service.dto.AdsSongCriteria;
import com.mycompany.myapp.service.AdsSongQueryService;

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
 * REST controller for managing {@link com.mycompany.myapp.domain.AdsSong}.
 */
@RestController
@RequestMapping("/api")
public class AdsSongResource {

    private final Logger log = LoggerFactory.getLogger(AdsSongResource.class);

    private static final String ENTITY_NAME = "adsSong";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AdsSongService adsSongService;

    private final AdsSongQueryService adsSongQueryService;

    public AdsSongResource(AdsSongService adsSongService, AdsSongQueryService adsSongQueryService) {
        this.adsSongService = adsSongService;
        this.adsSongQueryService = adsSongQueryService;
    }

    /**
     * {@code POST  /ads-songs} : Create a new adsSong.
     *
     * @param adsSongDTO the adsSongDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new adsSongDTO, or with status {@code 400 (Bad Request)} if the adsSong has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/ads-songs")
    public ResponseEntity<AdsSongDTO> createAdsSong(@RequestBody AdsSongDTO adsSongDTO) throws URISyntaxException {
        log.debug("REST request to save AdsSong : {}", adsSongDTO);
        if (adsSongDTO.getId() != null) {
            throw new BadRequestAlertException("A new adsSong cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AdsSongDTO result = adsSongService.save(adsSongDTO);
        return ResponseEntity.created(new URI("/api/ads-songs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /ads-songs} : Updates an existing adsSong.
     *
     * @param adsSongDTO the adsSongDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated adsSongDTO,
     * or with status {@code 400 (Bad Request)} if the adsSongDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the adsSongDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/ads-songs")
    public ResponseEntity<AdsSongDTO> updateAdsSong(@RequestBody AdsSongDTO adsSongDTO) throws URISyntaxException {
        log.debug("REST request to update AdsSong : {}", adsSongDTO);
        if (adsSongDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        AdsSongDTO result = adsSongService.save(adsSongDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, adsSongDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /ads-songs} : get all the adsSongs.
     *

     * @param pageable the pagination information.

     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of adsSongs in body.
     */
    @GetMapping("/ads-songs")
    public ResponseEntity<List<AdsSongDTO>> getAllAdsSongs(AdsSongCriteria criteria, Pageable pageable) {
        log.debug("REST request to get AdsSongs by criteria: {}", criteria);
        Page<AdsSongDTO> page = adsSongQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
    * {@code GET  /ads-songs/count} : count all the adsSongs.
    *
    * @param criteria the criteria which the requested entities should match.
    * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
    */
    @GetMapping("/ads-songs/count")
    public ResponseEntity<Long> countAdsSongs(AdsSongCriteria criteria) {
        log.debug("REST request to count AdsSongs by criteria: {}", criteria);
        return ResponseEntity.ok().body(adsSongQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /ads-songs/:id} : get the "id" adsSong.
     *
     * @param id the id of the adsSongDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the adsSongDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/ads-songs/{id}")
    public ResponseEntity<AdsSongDTO> getAdsSong(@PathVariable Long id) {
        log.debug("REST request to get AdsSong : {}", id);
        Optional<AdsSongDTO> adsSongDTO = adsSongService.findOne(id);
        return ResponseUtil.wrapOrNotFound(adsSongDTO);
    }

    /**
     * {@code DELETE  /ads-songs/:id} : delete the "id" adsSong.
     *
     * @param id the id of the adsSongDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/ads-songs/{id}")
    public ResponseEntity<Void> deleteAdsSong(@PathVariable Long id) {
        log.debug("REST request to delete AdsSong : {}", id);
        adsSongService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/ads-songs?query=:query} : search for the adsSong corresponding
     * to the query.
     *
     * @param query the query of the adsSong search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/ads-songs")
    public ResponseEntity<List<AdsSongDTO>> searchAdsSongs(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of AdsSongs for query {}", query);
        Page<AdsSongDTO> page = adsSongService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
