package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Playlist;
import com.mycompany.myapp.repository.PlaylistRepository;
import com.mycompany.myapp.repository.search.PlaylistSearchRepository;
import com.mycompany.myapp.service.dto.PlaylistDTO;
import com.mycompany.myapp.service.mapper.PlaylistMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing {@link Playlist}.
 */
@Service
@Transactional
public class PlaylistService {

    private final Logger log = LoggerFactory.getLogger(PlaylistService.class);

    private final PlaylistRepository playlistRepository;

    private final PlaylistMapper playlistMapper;

    private final PlaylistSearchRepository playlistSearchRepository;

    public PlaylistService(PlaylistRepository playlistRepository, PlaylistMapper playlistMapper, PlaylistSearchRepository playlistSearchRepository) {
        this.playlistRepository = playlistRepository;
        this.playlistMapper = playlistMapper;
        this.playlistSearchRepository = playlistSearchRepository;
    }

    /**
     * Save a playlist.
     *
     * @param playlistDTO the entity to save.
     * @return the persisted entity.
     */
    public PlaylistDTO save(PlaylistDTO playlistDTO) {
        log.debug("Request to save Playlist : {}", playlistDTO);
        Playlist playlist = playlistMapper.toEntity(playlistDTO);
        playlist = playlistRepository.save(playlist);
        PlaylistDTO result = playlistMapper.toDto(playlist);
        playlistSearchRepository.save(playlist);
        return result;
    }

    /**
     * Get all the playlists.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<PlaylistDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Playlists");
        return playlistRepository.findAll(pageable)
            .map(playlistMapper::toDto);
    }


    /**
     * Get one playlist by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<PlaylistDTO> findOne(Long id) {
        log.debug("Request to get Playlist : {}", id);
        return playlistRepository.findById(id)
            .map(playlistMapper::toDto);
    }

    /**
     * Delete the playlist by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Playlist : {}", id);
        playlistRepository.deleteById(id);
        playlistSearchRepository.deleteById(id);
    }

    /**
     * Search for the playlist corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<PlaylistDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Playlists for query {}", query);
        return playlistSearchRepository.search(queryStringQuery(query), pageable)
            .map(playlistMapper::toDto);
    }
}
