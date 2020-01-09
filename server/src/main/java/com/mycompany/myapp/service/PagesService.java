package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Pages;
import com.mycompany.myapp.repository.PagesRepository;
import com.mycompany.myapp.repository.search.PagesSearchRepository;
import com.mycompany.myapp.service.dto.PagesDTO;
import com.mycompany.myapp.service.mapper.PagesMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing {@link Pages}.
 */
@Service
@Transactional
public class PagesService {

    private final Logger log = LoggerFactory.getLogger(PagesService.class);

    private final PagesRepository pagesRepository;

    private final PagesMapper pagesMapper;

    private final PagesSearchRepository pagesSearchRepository;

    public PagesService(PagesRepository pagesRepository, PagesMapper pagesMapper, PagesSearchRepository pagesSearchRepository) {
        this.pagesRepository = pagesRepository;
        this.pagesMapper = pagesMapper;
        this.pagesSearchRepository = pagesSearchRepository;
    }

    /**
     * Save a pages.
     *
     * @param pagesDTO the entity to save.
     * @return the persisted entity.
     */
    public PagesDTO save(PagesDTO pagesDTO) {
        log.debug("Request to save Pages : {}", pagesDTO);
        Pages pages = pagesMapper.toEntity(pagesDTO);
        pages = pagesRepository.save(pages);
        PagesDTO result = pagesMapper.toDto(pages);
        pagesSearchRepository.save(pages);
        return result;
    }

    /**
     * Get all the pages.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<PagesDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Pages");
        return pagesRepository.findAll(pageable)
            .map(pagesMapper::toDto);
    }


    /**
     * Get one pages by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<PagesDTO> findOne(Long id) {
        log.debug("Request to get Pages : {}", id);
        return pagesRepository.findById(id)
            .map(pagesMapper::toDto);
    }

    /**
     * Delete the pages by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Pages : {}", id);
        pagesRepository.deleteById(id);
        pagesSearchRepository.deleteById(id);
    }

    /**
     * Search for the pages corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<PagesDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Pages for query {}", query);
        return pagesSearchRepository.search(queryStringQuery(query), pageable)
            .map(pagesMapper::toDto);
    }
}
