package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.ServerApp;
import com.mycompany.myapp.domain.Album;
import com.mycompany.myapp.domain.Songs;
import com.mycompany.myapp.repository.AlbumRepository;
import com.mycompany.myapp.repository.search.AlbumSearchRepository;
import com.mycompany.myapp.service.AlbumService;
import com.mycompany.myapp.service.dto.AlbumDTO;
import com.mycompany.myapp.service.mapper.AlbumMapper;
import com.mycompany.myapp.web.rest.errors.ExceptionTranslator;
import com.mycompany.myapp.service.dto.AlbumCriteria;
import com.mycompany.myapp.service.AlbumQueryService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.util.Collections;
import java.util.List;

import static com.mycompany.myapp.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link AlbumResource} REST controller.
 */
@SpringBootTest(classes = ServerApp.class)
public class AlbumResourceIT {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_VOCAL = "AAAAAAAAAA";
    private static final String UPDATED_VOCAL = "BBBBBBBBBB";

    private static final String DEFAULT_THUMBNAIL = "AAAAAAAAAA";
    private static final String UPDATED_THUMBNAIL = "BBBBBBBBBB";

    @Autowired
    private AlbumRepository albumRepository;

    @Autowired
    private AlbumMapper albumMapper;

    @Autowired
    private AlbumService albumService;

    /**
     * This repository is mocked in the com.mycompany.myapp.repository.search test package.
     *
     * @see com.mycompany.myapp.repository.search.AlbumSearchRepositoryMockConfiguration
     */
    @Autowired
    private AlbumSearchRepository mockAlbumSearchRepository;

    @Autowired
    private AlbumQueryService albumQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    @Autowired
    private Validator validator;

    private MockMvc restAlbumMockMvc;

    private Album album;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final AlbumResource albumResource = new AlbumResource(albumService, albumQueryService);
        this.restAlbumMockMvc = MockMvcBuilders.standaloneSetup(albumResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Album createEntity(EntityManager em) {
        Album album = new Album()
            .title(DEFAULT_TITLE)
            .description(DEFAULT_DESCRIPTION)
            .vocal(DEFAULT_VOCAL)
            .thumbnail(DEFAULT_THUMBNAIL);
        return album;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Album createUpdatedEntity(EntityManager em) {
        Album album = new Album()
            .title(UPDATED_TITLE)
            .description(UPDATED_DESCRIPTION)
            .vocal(UPDATED_VOCAL)
            .thumbnail(UPDATED_THUMBNAIL);
        return album;
    }

    @BeforeEach
    public void initTest() {
        album = createEntity(em);
    }

    @Test
    @Transactional
    public void createAlbum() throws Exception {
        int databaseSizeBeforeCreate = albumRepository.findAll().size();

        // Create the Album
        AlbumDTO albumDTO = albumMapper.toDto(album);
        restAlbumMockMvc.perform(post("/api/albums")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(albumDTO)))
            .andExpect(status().isCreated());

        // Validate the Album in the database
        List<Album> albumList = albumRepository.findAll();
        assertThat(albumList).hasSize(databaseSizeBeforeCreate + 1);
        Album testAlbum = albumList.get(albumList.size() - 1);
        assertThat(testAlbum.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testAlbum.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testAlbum.getVocal()).isEqualTo(DEFAULT_VOCAL);
        assertThat(testAlbum.getThumbnail()).isEqualTo(DEFAULT_THUMBNAIL);

        // Validate the Album in Elasticsearch
        verify(mockAlbumSearchRepository, times(1)).save(testAlbum);
    }

    @Test
    @Transactional
    public void createAlbumWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = albumRepository.findAll().size();

        // Create the Album with an existing ID
        album.setId(1L);
        AlbumDTO albumDTO = albumMapper.toDto(album);

        // An entity with an existing ID cannot be created, so this API call must fail
        restAlbumMockMvc.perform(post("/api/albums")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(albumDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Album in the database
        List<Album> albumList = albumRepository.findAll();
        assertThat(albumList).hasSize(databaseSizeBeforeCreate);

        // Validate the Album in Elasticsearch
        verify(mockAlbumSearchRepository, times(0)).save(album);
    }


    @Test
    @Transactional
    public void getAllAlbums() throws Exception {
        // Initialize the database
        albumRepository.saveAndFlush(album);

        // Get all the albumList
        restAlbumMockMvc.perform(get("/api/albums?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(album.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].vocal").value(hasItem(DEFAULT_VOCAL)))
            .andExpect(jsonPath("$.[*].thumbnail").value(hasItem(DEFAULT_THUMBNAIL)));
    }
    
    @Test
    @Transactional
    public void getAlbum() throws Exception {
        // Initialize the database
        albumRepository.saveAndFlush(album);

        // Get the album
        restAlbumMockMvc.perform(get("/api/albums/{id}", album.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(album.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.vocal").value(DEFAULT_VOCAL))
            .andExpect(jsonPath("$.thumbnail").value(DEFAULT_THUMBNAIL));
    }


    @Test
    @Transactional
    public void getAlbumsByIdFiltering() throws Exception {
        // Initialize the database
        albumRepository.saveAndFlush(album);

        Long id = album.getId();

        defaultAlbumShouldBeFound("id.equals=" + id);
        defaultAlbumShouldNotBeFound("id.notEquals=" + id);

        defaultAlbumShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultAlbumShouldNotBeFound("id.greaterThan=" + id);

        defaultAlbumShouldBeFound("id.lessThanOrEqual=" + id);
        defaultAlbumShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllAlbumsByTitleIsEqualToSomething() throws Exception {
        // Initialize the database
        albumRepository.saveAndFlush(album);

        // Get all the albumList where title equals to DEFAULT_TITLE
        defaultAlbumShouldBeFound("title.equals=" + DEFAULT_TITLE);

        // Get all the albumList where title equals to UPDATED_TITLE
        defaultAlbumShouldNotBeFound("title.equals=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    public void getAllAlbumsByTitleIsNotEqualToSomething() throws Exception {
        // Initialize the database
        albumRepository.saveAndFlush(album);

        // Get all the albumList where title not equals to DEFAULT_TITLE
        defaultAlbumShouldNotBeFound("title.notEquals=" + DEFAULT_TITLE);

        // Get all the albumList where title not equals to UPDATED_TITLE
        defaultAlbumShouldBeFound("title.notEquals=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    public void getAllAlbumsByTitleIsInShouldWork() throws Exception {
        // Initialize the database
        albumRepository.saveAndFlush(album);

        // Get all the albumList where title in DEFAULT_TITLE or UPDATED_TITLE
        defaultAlbumShouldBeFound("title.in=" + DEFAULT_TITLE + "," + UPDATED_TITLE);

        // Get all the albumList where title equals to UPDATED_TITLE
        defaultAlbumShouldNotBeFound("title.in=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    public void getAllAlbumsByTitleIsNullOrNotNull() throws Exception {
        // Initialize the database
        albumRepository.saveAndFlush(album);

        // Get all the albumList where title is not null
        defaultAlbumShouldBeFound("title.specified=true");

        // Get all the albumList where title is null
        defaultAlbumShouldNotBeFound("title.specified=false");
    }
                @Test
    @Transactional
    public void getAllAlbumsByTitleContainsSomething() throws Exception {
        // Initialize the database
        albumRepository.saveAndFlush(album);

        // Get all the albumList where title contains DEFAULT_TITLE
        defaultAlbumShouldBeFound("title.contains=" + DEFAULT_TITLE);

        // Get all the albumList where title contains UPDATED_TITLE
        defaultAlbumShouldNotBeFound("title.contains=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    public void getAllAlbumsByTitleNotContainsSomething() throws Exception {
        // Initialize the database
        albumRepository.saveAndFlush(album);

        // Get all the albumList where title does not contain DEFAULT_TITLE
        defaultAlbumShouldNotBeFound("title.doesNotContain=" + DEFAULT_TITLE);

        // Get all the albumList where title does not contain UPDATED_TITLE
        defaultAlbumShouldBeFound("title.doesNotContain=" + UPDATED_TITLE);
    }


    @Test
    @Transactional
    public void getAllAlbumsByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        albumRepository.saveAndFlush(album);

        // Get all the albumList where description equals to DEFAULT_DESCRIPTION
        defaultAlbumShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the albumList where description equals to UPDATED_DESCRIPTION
        defaultAlbumShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllAlbumsByDescriptionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        albumRepository.saveAndFlush(album);

        // Get all the albumList where description not equals to DEFAULT_DESCRIPTION
        defaultAlbumShouldNotBeFound("description.notEquals=" + DEFAULT_DESCRIPTION);

        // Get all the albumList where description not equals to UPDATED_DESCRIPTION
        defaultAlbumShouldBeFound("description.notEquals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllAlbumsByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        albumRepository.saveAndFlush(album);

        // Get all the albumList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultAlbumShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the albumList where description equals to UPDATED_DESCRIPTION
        defaultAlbumShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllAlbumsByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        albumRepository.saveAndFlush(album);

        // Get all the albumList where description is not null
        defaultAlbumShouldBeFound("description.specified=true");

        // Get all the albumList where description is null
        defaultAlbumShouldNotBeFound("description.specified=false");
    }
                @Test
    @Transactional
    public void getAllAlbumsByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        albumRepository.saveAndFlush(album);

        // Get all the albumList where description contains DEFAULT_DESCRIPTION
        defaultAlbumShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the albumList where description contains UPDATED_DESCRIPTION
        defaultAlbumShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllAlbumsByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        albumRepository.saveAndFlush(album);

        // Get all the albumList where description does not contain DEFAULT_DESCRIPTION
        defaultAlbumShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the albumList where description does not contain UPDATED_DESCRIPTION
        defaultAlbumShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }


    @Test
    @Transactional
    public void getAllAlbumsByVocalIsEqualToSomething() throws Exception {
        // Initialize the database
        albumRepository.saveAndFlush(album);

        // Get all the albumList where vocal equals to DEFAULT_VOCAL
        defaultAlbumShouldBeFound("vocal.equals=" + DEFAULT_VOCAL);

        // Get all the albumList where vocal equals to UPDATED_VOCAL
        defaultAlbumShouldNotBeFound("vocal.equals=" + UPDATED_VOCAL);
    }

    @Test
    @Transactional
    public void getAllAlbumsByVocalIsNotEqualToSomething() throws Exception {
        // Initialize the database
        albumRepository.saveAndFlush(album);

        // Get all the albumList where vocal not equals to DEFAULT_VOCAL
        defaultAlbumShouldNotBeFound("vocal.notEquals=" + DEFAULT_VOCAL);

        // Get all the albumList where vocal not equals to UPDATED_VOCAL
        defaultAlbumShouldBeFound("vocal.notEquals=" + UPDATED_VOCAL);
    }

    @Test
    @Transactional
    public void getAllAlbumsByVocalIsInShouldWork() throws Exception {
        // Initialize the database
        albumRepository.saveAndFlush(album);

        // Get all the albumList where vocal in DEFAULT_VOCAL or UPDATED_VOCAL
        defaultAlbumShouldBeFound("vocal.in=" + DEFAULT_VOCAL + "," + UPDATED_VOCAL);

        // Get all the albumList where vocal equals to UPDATED_VOCAL
        defaultAlbumShouldNotBeFound("vocal.in=" + UPDATED_VOCAL);
    }

    @Test
    @Transactional
    public void getAllAlbumsByVocalIsNullOrNotNull() throws Exception {
        // Initialize the database
        albumRepository.saveAndFlush(album);

        // Get all the albumList where vocal is not null
        defaultAlbumShouldBeFound("vocal.specified=true");

        // Get all the albumList where vocal is null
        defaultAlbumShouldNotBeFound("vocal.specified=false");
    }
                @Test
    @Transactional
    public void getAllAlbumsByVocalContainsSomething() throws Exception {
        // Initialize the database
        albumRepository.saveAndFlush(album);

        // Get all the albumList where vocal contains DEFAULT_VOCAL
        defaultAlbumShouldBeFound("vocal.contains=" + DEFAULT_VOCAL);

        // Get all the albumList where vocal contains UPDATED_VOCAL
        defaultAlbumShouldNotBeFound("vocal.contains=" + UPDATED_VOCAL);
    }

    @Test
    @Transactional
    public void getAllAlbumsByVocalNotContainsSomething() throws Exception {
        // Initialize the database
        albumRepository.saveAndFlush(album);

        // Get all the albumList where vocal does not contain DEFAULT_VOCAL
        defaultAlbumShouldNotBeFound("vocal.doesNotContain=" + DEFAULT_VOCAL);

        // Get all the albumList where vocal does not contain UPDATED_VOCAL
        defaultAlbumShouldBeFound("vocal.doesNotContain=" + UPDATED_VOCAL);
    }


    @Test
    @Transactional
    public void getAllAlbumsByThumbnailIsEqualToSomething() throws Exception {
        // Initialize the database
        albumRepository.saveAndFlush(album);

        // Get all the albumList where thumbnail equals to DEFAULT_THUMBNAIL
        defaultAlbumShouldBeFound("thumbnail.equals=" + DEFAULT_THUMBNAIL);

        // Get all the albumList where thumbnail equals to UPDATED_THUMBNAIL
        defaultAlbumShouldNotBeFound("thumbnail.equals=" + UPDATED_THUMBNAIL);
    }

    @Test
    @Transactional
    public void getAllAlbumsByThumbnailIsNotEqualToSomething() throws Exception {
        // Initialize the database
        albumRepository.saveAndFlush(album);

        // Get all the albumList where thumbnail not equals to DEFAULT_THUMBNAIL
        defaultAlbumShouldNotBeFound("thumbnail.notEquals=" + DEFAULT_THUMBNAIL);

        // Get all the albumList where thumbnail not equals to UPDATED_THUMBNAIL
        defaultAlbumShouldBeFound("thumbnail.notEquals=" + UPDATED_THUMBNAIL);
    }

    @Test
    @Transactional
    public void getAllAlbumsByThumbnailIsInShouldWork() throws Exception {
        // Initialize the database
        albumRepository.saveAndFlush(album);

        // Get all the albumList where thumbnail in DEFAULT_THUMBNAIL or UPDATED_THUMBNAIL
        defaultAlbumShouldBeFound("thumbnail.in=" + DEFAULT_THUMBNAIL + "," + UPDATED_THUMBNAIL);

        // Get all the albumList where thumbnail equals to UPDATED_THUMBNAIL
        defaultAlbumShouldNotBeFound("thumbnail.in=" + UPDATED_THUMBNAIL);
    }

    @Test
    @Transactional
    public void getAllAlbumsByThumbnailIsNullOrNotNull() throws Exception {
        // Initialize the database
        albumRepository.saveAndFlush(album);

        // Get all the albumList where thumbnail is not null
        defaultAlbumShouldBeFound("thumbnail.specified=true");

        // Get all the albumList where thumbnail is null
        defaultAlbumShouldNotBeFound("thumbnail.specified=false");
    }
                @Test
    @Transactional
    public void getAllAlbumsByThumbnailContainsSomething() throws Exception {
        // Initialize the database
        albumRepository.saveAndFlush(album);

        // Get all the albumList where thumbnail contains DEFAULT_THUMBNAIL
        defaultAlbumShouldBeFound("thumbnail.contains=" + DEFAULT_THUMBNAIL);

        // Get all the albumList where thumbnail contains UPDATED_THUMBNAIL
        defaultAlbumShouldNotBeFound("thumbnail.contains=" + UPDATED_THUMBNAIL);
    }

    @Test
    @Transactional
    public void getAllAlbumsByThumbnailNotContainsSomething() throws Exception {
        // Initialize the database
        albumRepository.saveAndFlush(album);

        // Get all the albumList where thumbnail does not contain DEFAULT_THUMBNAIL
        defaultAlbumShouldNotBeFound("thumbnail.doesNotContain=" + DEFAULT_THUMBNAIL);

        // Get all the albumList where thumbnail does not contain UPDATED_THUMBNAIL
        defaultAlbumShouldBeFound("thumbnail.doesNotContain=" + UPDATED_THUMBNAIL);
    }


    @Test
    @Transactional
    public void getAllAlbumsBySongIsEqualToSomething() throws Exception {
        // Initialize the database
        albumRepository.saveAndFlush(album);
        Songs song = SongsResourceIT.createEntity(em);
        em.persist(song);
        em.flush();
        album.addSong(song);
        albumRepository.saveAndFlush(album);
        Long songId = song.getId();

        // Get all the albumList where song equals to songId
        defaultAlbumShouldBeFound("songId.equals=" + songId);

        // Get all the albumList where song equals to songId + 1
        defaultAlbumShouldNotBeFound("songId.equals=" + (songId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultAlbumShouldBeFound(String filter) throws Exception {
        restAlbumMockMvc.perform(get("/api/albums?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(album.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].vocal").value(hasItem(DEFAULT_VOCAL)))
            .andExpect(jsonPath("$.[*].thumbnail").value(hasItem(DEFAULT_THUMBNAIL)));

        // Check, that the count call also returns 1
        restAlbumMockMvc.perform(get("/api/albums/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultAlbumShouldNotBeFound(String filter) throws Exception {
        restAlbumMockMvc.perform(get("/api/albums?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restAlbumMockMvc.perform(get("/api/albums/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingAlbum() throws Exception {
        // Get the album
        restAlbumMockMvc.perform(get("/api/albums/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAlbum() throws Exception {
        // Initialize the database
        albumRepository.saveAndFlush(album);

        int databaseSizeBeforeUpdate = albumRepository.findAll().size();

        // Update the album
        Album updatedAlbum = albumRepository.findById(album.getId()).get();
        // Disconnect from session so that the updates on updatedAlbum are not directly saved in db
        em.detach(updatedAlbum);
        updatedAlbum
            .title(UPDATED_TITLE)
            .description(UPDATED_DESCRIPTION)
            .vocal(UPDATED_VOCAL)
            .thumbnail(UPDATED_THUMBNAIL);
        AlbumDTO albumDTO = albumMapper.toDto(updatedAlbum);

        restAlbumMockMvc.perform(put("/api/albums")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(albumDTO)))
            .andExpect(status().isOk());

        // Validate the Album in the database
        List<Album> albumList = albumRepository.findAll();
        assertThat(albumList).hasSize(databaseSizeBeforeUpdate);
        Album testAlbum = albumList.get(albumList.size() - 1);
        assertThat(testAlbum.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testAlbum.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testAlbum.getVocal()).isEqualTo(UPDATED_VOCAL);
        assertThat(testAlbum.getThumbnail()).isEqualTo(UPDATED_THUMBNAIL);

        // Validate the Album in Elasticsearch
        verify(mockAlbumSearchRepository, times(1)).save(testAlbum);
    }

    @Test
    @Transactional
    public void updateNonExistingAlbum() throws Exception {
        int databaseSizeBeforeUpdate = albumRepository.findAll().size();

        // Create the Album
        AlbumDTO albumDTO = albumMapper.toDto(album);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAlbumMockMvc.perform(put("/api/albums")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(albumDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Album in the database
        List<Album> albumList = albumRepository.findAll();
        assertThat(albumList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Album in Elasticsearch
        verify(mockAlbumSearchRepository, times(0)).save(album);
    }

    @Test
    @Transactional
    public void deleteAlbum() throws Exception {
        // Initialize the database
        albumRepository.saveAndFlush(album);

        int databaseSizeBeforeDelete = albumRepository.findAll().size();

        // Delete the album
        restAlbumMockMvc.perform(delete("/api/albums/{id}", album.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Album> albumList = albumRepository.findAll();
        assertThat(albumList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Album in Elasticsearch
        verify(mockAlbumSearchRepository, times(1)).deleteById(album.getId());
    }

    @Test
    @Transactional
    public void searchAlbum() throws Exception {
        // Initialize the database
        albumRepository.saveAndFlush(album);
        when(mockAlbumSearchRepository.search(queryStringQuery("id:" + album.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(album), PageRequest.of(0, 1), 1));
        // Search the album
        restAlbumMockMvc.perform(get("/api/_search/albums?query=id:" + album.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(album.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].vocal").value(hasItem(DEFAULT_VOCAL)))
            .andExpect(jsonPath("$.[*].thumbnail").value(hasItem(DEFAULT_THUMBNAIL)));
    }
}
