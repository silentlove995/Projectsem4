package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Songs;
import com.mycompany.myapp.repository.SongsRepository;
import com.mycompany.myapp.repository.search.SongsSearchRepository;
import com.mycompany.myapp.service.dto.SongsDTO;
import com.mycompany.myapp.service.mapper.SongsMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing {@link Songs}.
 */
@Service
@Transactional
public class SongsService {

    private final Logger log = LoggerFactory.getLogger(SongsService.class);

    private final SongsRepository songsRepository;

    private final SongsMapper songsMapper;

    private final SongsSearchRepository songsSearchRepository;

    public SongsService(SongsRepository songsRepository, SongsMapper songsMapper, SongsSearchRepository songsSearchRepository) {
        this.songsRepository = songsRepository;
        this.songsMapper = songsMapper;
        this.songsSearchRepository = songsSearchRepository;
    }

    /**
     * Save a songs.
     *
     * @param songsDTO the entity to save.
     * @return the persisted entity.
     */
    public SongsDTO save(SongsDTO songsDTO) {
        log.debug("Request to save Songs : {}", songsDTO);
        Songs songs = songsMapper.toEntity(songsDTO);
        songs = songsRepository.save(songs);
        SongsDTO result = songsMapper.toDto(songs);
        songsSearchRepository.save(songs);
        return result;
    }

    /**
     * Get all the songs.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<SongsDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Songs");
        return songsRepository.findAll(pageable)
            .map(songsMapper::toDto);
    }


    /**
     * Get one songs by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<SongsDTO> findOne(Long id) {
        log.debug("Request to get Songs : {}", id);
        return songsRepository.findById(id)
            .map(songsMapper::toDto);
    }

    /**
     * Delete the songs by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Songs : {}", id);
        songsRepository.deleteById(id);
        songsSearchRepository.deleteById(id);
    }

    /**
     * Search for the songs corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<SongsDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Songs for query {}", query);
        return songsSearchRepository.search(queryStringQuery(query), pageable)
            .map(songsMapper::toDto);
    }
}
