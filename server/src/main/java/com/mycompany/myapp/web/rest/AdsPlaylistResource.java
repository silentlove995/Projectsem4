package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.service.AdsPlaylistService;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import com.mycompany.myapp.service.dto.AdsPlaylistDTO;
import com.mycompany.myapp.service.dto.AdsPlaylistCriteria;
import com.mycompany.myapp.service.AdsPlaylistQueryService;

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
 * REST controller for managing {@link com.mycompany.myapp.domain.AdsPlaylist}.
 */
@RestController
@RequestMapping("/api")
public class AdsPlaylistResource {

    private final Logger log = LoggerFactory.getLogger(AdsPlaylistResource.class);

    private static final String ENTITY_NAME = "adsPlaylist";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AdsPlaylistService adsPlaylistService;

    private final AdsPlaylistQueryService adsPlaylistQueryService;

    public AdsPlaylistResource(AdsPlaylistService adsPlaylistService, AdsPlaylistQueryService adsPlaylistQueryService) {
        this.adsPlaylistService = adsPlaylistService;
        this.adsPlaylistQueryService = adsPlaylistQueryService;
    }

    /**
     * {@code POST  /ads-playlists} : Create a new adsPlaylist.
     *
     * @param adsPlaylistDTO the adsPlaylistDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new adsPlaylistDTO, or with status {@code 400 (Bad Request)} if the adsPlaylist has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/ads-playlists")
    public ResponseEntity<AdsPlaylistDTO> createAdsPlaylist(@RequestBody AdsPlaylistDTO adsPlaylistDTO) throws URISyntaxException {
        log.debug("REST request to save AdsPlaylist : {}", adsPlaylistDTO);
        if (adsPlaylistDTO.getId() != null) {
            throw new BadRequestAlertException("A new adsPlaylist cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AdsPlaylistDTO result = adsPlaylistService.save(adsPlaylistDTO);
        return ResponseEntity.created(new URI("/api/ads-playlists/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /ads-playlists} : Updates an existing adsPlaylist.
     *
     * @param adsPlaylistDTO the adsPlaylistDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated adsPlaylistDTO,
     * or with status {@code 400 (Bad Request)} if the adsPlaylistDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the adsPlaylistDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/ads-playlists")
    public ResponseEntity<AdsPlaylistDTO> updateAdsPlaylist(@RequestBody AdsPlaylistDTO adsPlaylistDTO) throws URISyntaxException {
        log.debug("REST request to update AdsPlaylist : {}", adsPlaylistDTO);
        if (adsPlaylistDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        AdsPlaylistDTO result = adsPlaylistService.save(adsPlaylistDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, adsPlaylistDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /ads-playlists} : get all the adsPlaylists.
     *

     * @param pageable the pagination information.

     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of adsPlaylists in body.
     */
    @GetMapping("/ads-playlists")
    public ResponseEntity<List<AdsPlaylistDTO>> getAllAdsPlaylists(AdsPlaylistCriteria criteria, Pageable pageable) {
        log.debug("REST request to get AdsPlaylists by criteria: {}", criteria);
        Page<AdsPlaylistDTO> page = adsPlaylistQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
    * {@code GET  /ads-playlists/count} : count all the adsPlaylists.
    *
    * @param criteria the criteria which the requested entities should match.
    * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
    */
    @GetMapping("/ads-playlists/count")
    public ResponseEntity<Long> countAdsPlaylists(AdsPlaylistCriteria criteria) {
        log.debug("REST request to count AdsPlaylists by criteria: {}", criteria);
        return ResponseEntity.ok().body(adsPlaylistQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /ads-playlists/:id} : get the "id" adsPlaylist.
     *
     * @param id the id of the adsPlaylistDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the adsPlaylistDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/ads-playlists/{id}")
    public ResponseEntity<AdsPlaylistDTO> getAdsPlaylist(@PathVariable Long id) {
        log.debug("REST request to get AdsPlaylist : {}", id);
        Optional<AdsPlaylistDTO> adsPlaylistDTO = adsPlaylistService.findOne(id);
        return ResponseUtil.wrapOrNotFound(adsPlaylistDTO);
    }

    /**
     * {@code DELETE  /ads-playlists/:id} : delete the "id" adsPlaylist.
     *
     * @param id the id of the adsPlaylistDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/ads-playlists/{id}")
    public ResponseEntity<Void> deleteAdsPlaylist(@PathVariable Long id) {
        log.debug("REST request to delete AdsPlaylist : {}", id);
        adsPlaylistService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/ads-playlists?query=:query} : search for the adsPlaylist corresponding
     * to the query.
     *
     * @param query the query of the adsPlaylist search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/ads-playlists")
    public ResponseEntity<List<AdsPlaylistDTO>> searchAdsPlaylists(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of AdsPlaylists for query {}", query);
        Page<AdsPlaylistDTO> page = adsPlaylistService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
