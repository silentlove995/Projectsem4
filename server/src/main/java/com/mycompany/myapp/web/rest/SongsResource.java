package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.service.SongsService;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import com.mycompany.myapp.service.dto.SongsDTO;
import com.mycompany.myapp.service.dto.SongsCriteria;
import com.mycompany.myapp.service.SongsQueryService;

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
 * REST controller for managing {@link com.mycompany.myapp.domain.Songs}.
 */
@RestController
@RequestMapping("/api")
public class SongsResource {

    private final Logger log = LoggerFactory.getLogger(SongsResource.class);

    private static final String ENTITY_NAME = "songs";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SongsService songsService;

    private final SongsQueryService songsQueryService;

    public SongsResource(SongsService songsService, SongsQueryService songsQueryService) {
        this.songsService = songsService;
        this.songsQueryService = songsQueryService;
    }

    /**
     * {@code POST  /songs} : Create a new songs.
     *
     * @param songsDTO the songsDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new songsDTO, or with status {@code 400 (Bad Request)} if the songs has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/songs")
    public ResponseEntity<SongsDTO> createSongs(@RequestBody SongsDTO songsDTO) throws URISyntaxException {
        log.debug("REST request to save Songs : {}", songsDTO);
        if (songsDTO.getId() != null) {
            throw new BadRequestAlertException("A new songs cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SongsDTO result = songsService.save(songsDTO);
        return ResponseEntity.created(new URI("/api/songs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /songs} : Updates an existing songs.
     *
     * @param songsDTO the songsDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated songsDTO,
     * or with status {@code 400 (Bad Request)} if the songsDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the songsDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/songs")
    public ResponseEntity<SongsDTO> updateSongs(@RequestBody SongsDTO songsDTO) throws URISyntaxException {
        log.debug("REST request to update Songs : {}", songsDTO);
        if (songsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        SongsDTO result = songsService.save(songsDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, songsDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /songs} : get all the songs.
     *

     * @param pageable the pagination information.

     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of songs in body.
     */
    @GetMapping("/songs")
    public ResponseEntity<List<SongsDTO>> getAllSongs(SongsCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Songs by criteria: {}", criteria);
        Page<SongsDTO> page = songsQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
    * {@code GET  /songs/count} : count all the songs.
    *
    * @param criteria the criteria which the requested entities should match.
    * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
    */
    @GetMapping("/songs/count")
    public ResponseEntity<Long> countSongs(SongsCriteria criteria) {
        log.debug("REST request to count Songs by criteria: {}", criteria);
        return ResponseEntity.ok().body(songsQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /songs/:id} : get the "id" songs.
     *
     * @param id the id of the songsDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the songsDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/songs/{id}")
    public ResponseEntity<SongsDTO> getSongs(@PathVariable Long id) {
        log.debug("REST request to get Songs : {}", id);
        Optional<SongsDTO> songsDTO = songsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(songsDTO);
    }

    /**
     * {@code DELETE  /songs/:id} : delete the "id" songs.
     *
     * @param id the id of the songsDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/songs/{id}")
    public ResponseEntity<Void> deleteSongs(@PathVariable Long id) {
        log.debug("REST request to delete Songs : {}", id);
        songsService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/songs?query=:query} : search for the songs corresponding
     * to the query.
     *
     * @param query the query of the songs search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/songs")
    public ResponseEntity<List<SongsDTO>> searchSongs(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Songs for query {}", query);
        Page<SongsDTO> page = songsService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
