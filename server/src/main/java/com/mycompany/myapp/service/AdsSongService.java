package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.AdsSong;
import com.mycompany.myapp.repository.AdsSongRepository;
import com.mycompany.myapp.repository.search.AdsSongSearchRepository;
import com.mycompany.myapp.service.dto.AdsSongDTO;
import com.mycompany.myapp.service.mapper.AdsSongMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing {@link AdsSong}.
 */
@Service
@Transactional
public class AdsSongService {

    private final Logger log = LoggerFactory.getLogger(AdsSongService.class);

    private final AdsSongRepository adsSongRepository;

    private final AdsSongMapper adsSongMapper;

    private final AdsSongSearchRepository adsSongSearchRepository;

    public AdsSongService(AdsSongRepository adsSongRepository, AdsSongMapper adsSongMapper, AdsSongSearchRepository adsSongSearchRepository) {
        this.adsSongRepository = adsSongRepository;
        this.adsSongMapper = adsSongMapper;
        this.adsSongSearchRepository = adsSongSearchRepository;
    }

    /**
     * Save a adsSong.
     *
     * @param adsSongDTO the entity to save.
     * @return the persisted entity.
     */
    public AdsSongDTO save(AdsSongDTO adsSongDTO) {
        log.debug("Request to save AdsSong : {}", adsSongDTO);
        AdsSong adsSong = adsSongMapper.toEntity(adsSongDTO);
        adsSong = adsSongRepository.save(adsSong);
        AdsSongDTO result = adsSongMapper.toDto(adsSong);
        adsSongSearchRepository.save(adsSong);
        return result;
    }

    /**
     * Get all the adsSongs.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<AdsSongDTO> findAll(Pageable pageable) {
        log.debug("Request to get all AdsSongs");
        return adsSongRepository.findAll(pageable)
            .map(adsSongMapper::toDto);
    }


    /**
     * Get one adsSong by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<AdsSongDTO> findOne(Long id) {
        log.debug("Request to get AdsSong : {}", id);
        return adsSongRepository.findById(id)
            .map(adsSongMapper::toDto);
    }

    /**
     * Delete the adsSong by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete AdsSong : {}", id);
        adsSongRepository.deleteById(id);
        adsSongSearchRepository.deleteById(id);
    }

    /**
     * Search for the adsSong corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<AdsSongDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of AdsSongs for query {}", query);
        return adsSongSearchRepository.search(queryStringQuery(query), pageable)
            .map(adsSongMapper::toDto);
    }
}
