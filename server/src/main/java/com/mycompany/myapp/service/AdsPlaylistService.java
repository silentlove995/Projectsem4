package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.AdsPlaylist;
import com.mycompany.myapp.repository.AdsPlaylistRepository;
import com.mycompany.myapp.repository.search.AdsPlaylistSearchRepository;
import com.mycompany.myapp.service.dto.AdsPlaylistDTO;
import com.mycompany.myapp.service.mapper.AdsPlaylistMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing {@link AdsPlaylist}.
 */
@Service
@Transactional
public class AdsPlaylistService {

    private final Logger log = LoggerFactory.getLogger(AdsPlaylistService.class);

    private final AdsPlaylistRepository adsPlaylistRepository;

    private final AdsPlaylistMapper adsPlaylistMapper;

    private final AdsPlaylistSearchRepository adsPlaylistSearchRepository;

    public AdsPlaylistService(AdsPlaylistRepository adsPlaylistRepository, AdsPlaylistMapper adsPlaylistMapper, AdsPlaylistSearchRepository adsPlaylistSearchRepository) {
        this.adsPlaylistRepository = adsPlaylistRepository;
        this.adsPlaylistMapper = adsPlaylistMapper;
        this.adsPlaylistSearchRepository = adsPlaylistSearchRepository;
    }

    /**
     * Save a adsPlaylist.
     *
     * @param adsPlaylistDTO the entity to save.
     * @return the persisted entity.
     */
    public AdsPlaylistDTO save(AdsPlaylistDTO adsPlaylistDTO) {
        log.debug("Request to save AdsPlaylist : {}", adsPlaylistDTO);
        AdsPlaylist adsPlaylist = adsPlaylistMapper.toEntity(adsPlaylistDTO);
        adsPlaylist = adsPlaylistRepository.save(adsPlaylist);
        AdsPlaylistDTO result = adsPlaylistMapper.toDto(adsPlaylist);
        adsPlaylistSearchRepository.save(adsPlaylist);
        return result;
    }

    /**
     * Get all the adsPlaylists.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<AdsPlaylistDTO> findAll(Pageable pageable) {
        log.debug("Request to get all AdsPlaylists");
        return adsPlaylistRepository.findAll(pageable)
            .map(adsPlaylistMapper::toDto);
    }


    /**
     * Get one adsPlaylist by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<AdsPlaylistDTO> findOne(Long id) {
        log.debug("Request to get AdsPlaylist : {}", id);
        return adsPlaylistRepository.findById(id)
            .map(adsPlaylistMapper::toDto);
    }

    /**
     * Delete the adsPlaylist by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete AdsPlaylist : {}", id);
        adsPlaylistRepository.deleteById(id);
        adsPlaylistSearchRepository.deleteById(id);
    }

    /**
     * Search for the adsPlaylist corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<AdsPlaylistDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of AdsPlaylists for query {}", query);
        return adsPlaylistSearchRepository.search(queryStringQuery(query), pageable)
            .map(adsPlaylistMapper::toDto);
    }
}
