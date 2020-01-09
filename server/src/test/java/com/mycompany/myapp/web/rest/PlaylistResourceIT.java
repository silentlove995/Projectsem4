package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.ServerApp;
import com.mycompany.myapp.domain.Playlist;
import com.mycompany.myapp.domain.AdsPlaylist;
import com.mycompany.myapp.domain.Songs;
import com.mycompany.myapp.repository.PlaylistRepository;
import com.mycompany.myapp.repository.search.PlaylistSearchRepository;
import com.mycompany.myapp.service.PlaylistService;
import com.mycompany.myapp.service.dto.PlaylistDTO;
import com.mycompany.myapp.service.mapper.PlaylistMapper;
import com.mycompany.myapp.web.rest.errors.ExceptionTranslator;
import com.mycompany.myapp.service.dto.PlaylistCriteria;
import com.mycompany.myapp.service.PlaylistQueryService;

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
 * Integration tests for the {@link PlaylistResource} REST controller.
 */
@SpringBootTest(classes = ServerApp.class)
public class PlaylistResourceIT {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_VOCAL = "AAAAAAAAAA";
    private static final String UPDATED_VOCAL = "BBBBBBBBBB";

    private static final String DEFAULT_THUMBNAIL = "AAAAAAAAAA";
    private static final String UPDATED_THUMBNAIL = "BBBBBBBBBB";

    @Autowired
    private PlaylistRepository playlistRepository;

    @Autowired
    private PlaylistMapper playlistMapper;

    @Autowired
    private PlaylistService playlistService;

    /**
     * This repository is mocked in the com.mycompany.myapp.repository.search test package.
     *
     * @see com.mycompany.myapp.repository.search.PlaylistSearchRepositoryMockConfiguration
     */
    @Autowired
    private PlaylistSearchRepository mockPlaylistSearchRepository;

    @Autowired
    private PlaylistQueryService playlistQueryService;

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

    private MockMvc restPlaylistMockMvc;

    private Playlist playlist;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final PlaylistResource playlistResource = new PlaylistResource(playlistService, playlistQueryService);
        this.restPlaylistMockMvc = MockMvcBuilders.standaloneSetup(playlistResource)
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
    public static Playlist createEntity(EntityManager em) {
        Playlist playlist = new Playlist()
            .title(DEFAULT_TITLE)
            .description(DEFAULT_DESCRIPTION)
            .vocal(DEFAULT_VOCAL)
            .thumbnail(DEFAULT_THUMBNAIL);
        return playlist;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Playlist createUpdatedEntity(EntityManager em) {
        Playlist playlist = new Playlist()
            .title(UPDATED_TITLE)
            .description(UPDATED_DESCRIPTION)
            .vocal(UPDATED_VOCAL)
            .thumbnail(UPDATED_THUMBNAIL);
        return playlist;
    }

    @BeforeEach
    public void initTest() {
        playlist = createEntity(em);
    }

    @Test
    @Transactional
    public void createPlaylist() throws Exception {
        int databaseSizeBeforeCreate = playlistRepository.findAll().size();

        // Create the Playlist
        PlaylistDTO playlistDTO = playlistMapper.toDto(playlist);
        restPlaylistMockMvc.perform(post("/api/playlists")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(playlistDTO)))
            .andExpect(status().isCreated());

        // Validate the Playlist in the database
        List<Playlist> playlistList = playlistRepository.findAll();
        assertThat(playlistList).hasSize(databaseSizeBeforeCreate + 1);
        Playlist testPlaylist = playlistList.get(playlistList.size() - 1);
        assertThat(testPlaylist.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testPlaylist.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testPlaylist.getVocal()).isEqualTo(DEFAULT_VOCAL);
        assertThat(testPlaylist.getThumbnail()).isEqualTo(DEFAULT_THUMBNAIL);

        // Validate the Playlist in Elasticsearch
        verify(mockPlaylistSearchRepository, times(1)).save(testPlaylist);
    }

    @Test
    @Transactional
    public void createPlaylistWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = playlistRepository.findAll().size();

        // Create the Playlist with an existing ID
        playlist.setId(1L);
        PlaylistDTO playlistDTO = playlistMapper.toDto(playlist);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPlaylistMockMvc.perform(post("/api/playlists")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(playlistDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Playlist in the database
        List<Playlist> playlistList = playlistRepository.findAll();
        assertThat(playlistList).hasSize(databaseSizeBeforeCreate);

        // Validate the Playlist in Elasticsearch
        verify(mockPlaylistSearchRepository, times(0)).save(playlist);
    }


    @Test
    @Transactional
    public void getAllPlaylists() throws Exception {
        // Initialize the database
        playlistRepository.saveAndFlush(playlist);

        // Get all the playlistList
        restPlaylistMockMvc.perform(get("/api/playlists?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(playlist.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].vocal").value(hasItem(DEFAULT_VOCAL)))
            .andExpect(jsonPath("$.[*].thumbnail").value(hasItem(DEFAULT_THUMBNAIL)));
    }
    
    @Test
    @Transactional
    public void getPlaylist() throws Exception {
        // Initialize the database
        playlistRepository.saveAndFlush(playlist);

        // Get the playlist
        restPlaylistMockMvc.perform(get("/api/playlists/{id}", playlist.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(playlist.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.vocal").value(DEFAULT_VOCAL))
            .andExpect(jsonPath("$.thumbnail").value(DEFAULT_THUMBNAIL));
    }


    @Test
    @Transactional
    public void getPlaylistsByIdFiltering() throws Exception {
        // Initialize the database
        playlistRepository.saveAndFlush(playlist);

        Long id = playlist.getId();

        defaultPlaylistShouldBeFound("id.equals=" + id);
        defaultPlaylistShouldNotBeFound("id.notEquals=" + id);

        defaultPlaylistShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultPlaylistShouldNotBeFound("id.greaterThan=" + id);

        defaultPlaylistShouldBeFound("id.lessThanOrEqual=" + id);
        defaultPlaylistShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllPlaylistsByTitleIsEqualToSomething() throws Exception {
        // Initialize the database
        playlistRepository.saveAndFlush(playlist);

        // Get all the playlistList where title equals to DEFAULT_TITLE
        defaultPlaylistShouldBeFound("title.equals=" + DEFAULT_TITLE);

        // Get all the playlistList where title equals to UPDATED_TITLE
        defaultPlaylistShouldNotBeFound("title.equals=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    public void getAllPlaylistsByTitleIsNotEqualToSomething() throws Exception {
        // Initialize the database
        playlistRepository.saveAndFlush(playlist);

        // Get all the playlistList where title not equals to DEFAULT_TITLE
        defaultPlaylistShouldNotBeFound("title.notEquals=" + DEFAULT_TITLE);

        // Get all the playlistList where title not equals to UPDATED_TITLE
        defaultPlaylistShouldBeFound("title.notEquals=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    public void getAllPlaylistsByTitleIsInShouldWork() throws Exception {
        // Initialize the database
        playlistRepository.saveAndFlush(playlist);

        // Get all the playlistList where title in DEFAULT_TITLE or UPDATED_TITLE
        defaultPlaylistShouldBeFound("title.in=" + DEFAULT_TITLE + "," + UPDATED_TITLE);

        // Get all the playlistList where title equals to UPDATED_TITLE
        defaultPlaylistShouldNotBeFound("title.in=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    public void getAllPlaylistsByTitleIsNullOrNotNull() throws Exception {
        // Initialize the database
        playlistRepository.saveAndFlush(playlist);

        // Get all the playlistList where title is not null
        defaultPlaylistShouldBeFound("title.specified=true");

        // Get all the playlistList where title is null
        defaultPlaylistShouldNotBeFound("title.specified=false");
    }
                @Test
    @Transactional
    public void getAllPlaylistsByTitleContainsSomething() throws Exception {
        // Initialize the database
        playlistRepository.saveAndFlush(playlist);

        // Get all the playlistList where title contains DEFAULT_TITLE
        defaultPlaylistShouldBeFound("title.contains=" + DEFAULT_TITLE);

        // Get all the playlistList where title contains UPDATED_TITLE
        defaultPlaylistShouldNotBeFound("title.contains=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    public void getAllPlaylistsByTitleNotContainsSomething() throws Exception {
        // Initialize the database
        playlistRepository.saveAndFlush(playlist);

        // Get all the playlistList where title does not contain DEFAULT_TITLE
        defaultPlaylistShouldNotBeFound("title.doesNotContain=" + DEFAULT_TITLE);

        // Get all the playlistList where title does not contain UPDATED_TITLE
        defaultPlaylistShouldBeFound("title.doesNotContain=" + UPDATED_TITLE);
    }


    @Test
    @Transactional
    public void getAllPlaylistsByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        playlistRepository.saveAndFlush(playlist);

        // Get all the playlistList where description equals to DEFAULT_DESCRIPTION
        defaultPlaylistShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the playlistList where description equals to UPDATED_DESCRIPTION
        defaultPlaylistShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllPlaylistsByDescriptionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        playlistRepository.saveAndFlush(playlist);

        // Get all the playlistList where description not equals to DEFAULT_DESCRIPTION
        defaultPlaylistShouldNotBeFound("description.notEquals=" + DEFAULT_DESCRIPTION);

        // Get all the playlistList where description not equals to UPDATED_DESCRIPTION
        defaultPlaylistShouldBeFound("description.notEquals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllPlaylistsByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        playlistRepository.saveAndFlush(playlist);

        // Get all the playlistList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultPlaylistShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the playlistList where description equals to UPDATED_DESCRIPTION
        defaultPlaylistShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllPlaylistsByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        playlistRepository.saveAndFlush(playlist);

        // Get all the playlistList where description is not null
        defaultPlaylistShouldBeFound("description.specified=true");

        // Get all the playlistList where description is null
        defaultPlaylistShouldNotBeFound("description.specified=false");
    }
                @Test
    @Transactional
    public void getAllPlaylistsByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        playlistRepository.saveAndFlush(playlist);

        // Get all the playlistList where description contains DEFAULT_DESCRIPTION
        defaultPlaylistShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the playlistList where description contains UPDATED_DESCRIPTION
        defaultPlaylistShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllPlaylistsByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        playlistRepository.saveAndFlush(playlist);

        // Get all the playlistList where description does not contain DEFAULT_DESCRIPTION
        defaultPlaylistShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the playlistList where description does not contain UPDATED_DESCRIPTION
        defaultPlaylistShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }


    @Test
    @Transactional
    public void getAllPlaylistsByVocalIsEqualToSomething() throws Exception {
        // Initialize the database
        playlistRepository.saveAndFlush(playlist);

        // Get all the playlistList where vocal equals to DEFAULT_VOCAL
        defaultPlaylistShouldBeFound("vocal.equals=" + DEFAULT_VOCAL);

        // Get all the playlistList where vocal equals to UPDATED_VOCAL
        defaultPlaylistShouldNotBeFound("vocal.equals=" + UPDATED_VOCAL);
    }

    @Test
    @Transactional
    public void getAllPlaylistsByVocalIsNotEqualToSomething() throws Exception {
        // Initialize the database
        playlistRepository.saveAndFlush(playlist);

        // Get all the playlistList where vocal not equals to DEFAULT_VOCAL
        defaultPlaylistShouldNotBeFound("vocal.notEquals=" + DEFAULT_VOCAL);

        // Get all the playlistList where vocal not equals to UPDATED_VOCAL
        defaultPlaylistShouldBeFound("vocal.notEquals=" + UPDATED_VOCAL);
    }

    @Test
    @Transactional
    public void getAllPlaylistsByVocalIsInShouldWork() throws Exception {
        // Initialize the database
        playlistRepository.saveAndFlush(playlist);

        // Get all the playlistList where vocal in DEFAULT_VOCAL or UPDATED_VOCAL
        defaultPlaylistShouldBeFound("vocal.in=" + DEFAULT_VOCAL + "," + UPDATED_VOCAL);

        // Get all the playlistList where vocal equals to UPDATED_VOCAL
        defaultPlaylistShouldNotBeFound("vocal.in=" + UPDATED_VOCAL);
    }

    @Test
    @Transactional
    public void getAllPlaylistsByVocalIsNullOrNotNull() throws Exception {
        // Initialize the database
        playlistRepository.saveAndFlush(playlist);

        // Get all the playlistList where vocal is not null
        defaultPlaylistShouldBeFound("vocal.specified=true");

        // Get all the playlistList where vocal is null
        defaultPlaylistShouldNotBeFound("vocal.specified=false");
    }
                @Test
    @Transactional
    public void getAllPlaylistsByVocalContainsSomething() throws Exception {
        // Initialize the database
        playlistRepository.saveAndFlush(playlist);

        // Get all the playlistList where vocal contains DEFAULT_VOCAL
        defaultPlaylistShouldBeFound("vocal.contains=" + DEFAULT_VOCAL);

        // Get all the playlistList where vocal contains UPDATED_VOCAL
        defaultPlaylistShouldNotBeFound("vocal.contains=" + UPDATED_VOCAL);
    }

    @Test
    @Transactional
    public void getAllPlaylistsByVocalNotContainsSomething() throws Exception {
        // Initialize the database
        playlistRepository.saveAndFlush(playlist);

        // Get all the playlistList where vocal does not contain DEFAULT_VOCAL
        defaultPlaylistShouldNotBeFound("vocal.doesNotContain=" + DEFAULT_VOCAL);

        // Get all the playlistList where vocal does not contain UPDATED_VOCAL
        defaultPlaylistShouldBeFound("vocal.doesNotContain=" + UPDATED_VOCAL);
    }


    @Test
    @Transactional
    public void getAllPlaylistsByThumbnailIsEqualToSomething() throws Exception {
        // Initialize the database
        playlistRepository.saveAndFlush(playlist);

        // Get all the playlistList where thumbnail equals to DEFAULT_THUMBNAIL
        defaultPlaylistShouldBeFound("thumbnail.equals=" + DEFAULT_THUMBNAIL);

        // Get all the playlistList where thumbnail equals to UPDATED_THUMBNAIL
        defaultPlaylistShouldNotBeFound("thumbnail.equals=" + UPDATED_THUMBNAIL);
    }

    @Test
    @Transactional
    public void getAllPlaylistsByThumbnailIsNotEqualToSomething() throws Exception {
        // Initialize the database
        playlistRepository.saveAndFlush(playlist);

        // Get all the playlistList where thumbnail not equals to DEFAULT_THUMBNAIL
        defaultPlaylistShouldNotBeFound("thumbnail.notEquals=" + DEFAULT_THUMBNAIL);

        // Get all the playlistList where thumbnail not equals to UPDATED_THUMBNAIL
        defaultPlaylistShouldBeFound("thumbnail.notEquals=" + UPDATED_THUMBNAIL);
    }

    @Test
    @Transactional
    public void getAllPlaylistsByThumbnailIsInShouldWork() throws Exception {
        // Initialize the database
        playlistRepository.saveAndFlush(playlist);

        // Get all the playlistList where thumbnail in DEFAULT_THUMBNAIL or UPDATED_THUMBNAIL
        defaultPlaylistShouldBeFound("thumbnail.in=" + DEFAULT_THUMBNAIL + "," + UPDATED_THUMBNAIL);

        // Get all the playlistList where thumbnail equals to UPDATED_THUMBNAIL
        defaultPlaylistShouldNotBeFound("thumbnail.in=" + UPDATED_THUMBNAIL);
    }

    @Test
    @Transactional
    public void getAllPlaylistsByThumbnailIsNullOrNotNull() throws Exception {
        // Initialize the database
        playlistRepository.saveAndFlush(playlist);

        // Get all the playlistList where thumbnail is not null
        defaultPlaylistShouldBeFound("thumbnail.specified=true");

        // Get all the playlistList where thumbnail is null
        defaultPlaylistShouldNotBeFound("thumbnail.specified=false");
    }
                @Test
    @Transactional
    public void getAllPlaylistsByThumbnailContainsSomething() throws Exception {
        // Initialize the database
        playlistRepository.saveAndFlush(playlist);

        // Get all the playlistList where thumbnail contains DEFAULT_THUMBNAIL
        defaultPlaylistShouldBeFound("thumbnail.contains=" + DEFAULT_THUMBNAIL);

        // Get all the playlistList where thumbnail contains UPDATED_THUMBNAIL
        defaultPlaylistShouldNotBeFound("thumbnail.contains=" + UPDATED_THUMBNAIL);
    }

    @Test
    @Transactional
    public void getAllPlaylistsByThumbnailNotContainsSomething() throws Exception {
        // Initialize the database
        playlistRepository.saveAndFlush(playlist);

        // Get all the playlistList where thumbnail does not contain DEFAULT_THUMBNAIL
        defaultPlaylistShouldNotBeFound("thumbnail.doesNotContain=" + DEFAULT_THUMBNAIL);

        // Get all the playlistList where thumbnail does not contain UPDATED_THUMBNAIL
        defaultPlaylistShouldBeFound("thumbnail.doesNotContain=" + UPDATED_THUMBNAIL);
    }


    @Test
    @Transactional
    public void getAllPlaylistsByAdsIsEqualToSomething() throws Exception {
        // Initialize the database
        playlistRepository.saveAndFlush(playlist);
        AdsPlaylist ads = AdsPlaylistResourceIT.createEntity(em);
        em.persist(ads);
        em.flush();
        playlist.setAds(ads);
        playlistRepository.saveAndFlush(playlist);
        Long adsId = ads.getId();

        // Get all the playlistList where ads equals to adsId
        defaultPlaylistShouldBeFound("adsId.equals=" + adsId);

        // Get all the playlistList where ads equals to adsId + 1
        defaultPlaylistShouldNotBeFound("adsId.equals=" + (adsId + 1));
    }


    @Test
    @Transactional
    public void getAllPlaylistsBySongIsEqualToSomething() throws Exception {
        // Initialize the database
        playlistRepository.saveAndFlush(playlist);
        Songs song = SongsResourceIT.createEntity(em);
        em.persist(song);
        em.flush();
        playlist.addSong(song);
        playlistRepository.saveAndFlush(playlist);
        Long songId = song.getId();

        // Get all the playlistList where song equals to songId
        defaultPlaylistShouldBeFound("songId.equals=" + songId);

        // Get all the playlistList where song equals to songId + 1
        defaultPlaylistShouldNotBeFound("songId.equals=" + (songId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultPlaylistShouldBeFound(String filter) throws Exception {
        restPlaylistMockMvc.perform(get("/api/playlists?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(playlist.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].vocal").value(hasItem(DEFAULT_VOCAL)))
            .andExpect(jsonPath("$.[*].thumbnail").value(hasItem(DEFAULT_THUMBNAIL)));

        // Check, that the count call also returns 1
        restPlaylistMockMvc.perform(get("/api/playlists/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultPlaylistShouldNotBeFound(String filter) throws Exception {
        restPlaylistMockMvc.perform(get("/api/playlists?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restPlaylistMockMvc.perform(get("/api/playlists/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingPlaylist() throws Exception {
        // Get the playlist
        restPlaylistMockMvc.perform(get("/api/playlists/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePlaylist() throws Exception {
        // Initialize the database
        playlistRepository.saveAndFlush(playlist);

        int databaseSizeBeforeUpdate = playlistRepository.findAll().size();

        // Update the playlist
        Playlist updatedPlaylist = playlistRepository.findById(playlist.getId()).get();
        // Disconnect from session so that the updates on updatedPlaylist are not directly saved in db
        em.detach(updatedPlaylist);
        updatedPlaylist
            .title(UPDATED_TITLE)
            .description(UPDATED_DESCRIPTION)
            .vocal(UPDATED_VOCAL)
            .thumbnail(UPDATED_THUMBNAIL);
        PlaylistDTO playlistDTO = playlistMapper.toDto(updatedPlaylist);

        restPlaylistMockMvc.perform(put("/api/playlists")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(playlistDTO)))
            .andExpect(status().isOk());

        // Validate the Playlist in the database
        List<Playlist> playlistList = playlistRepository.findAll();
        assertThat(playlistList).hasSize(databaseSizeBeforeUpdate);
        Playlist testPlaylist = playlistList.get(playlistList.size() - 1);
        assertThat(testPlaylist.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testPlaylist.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testPlaylist.getVocal()).isEqualTo(UPDATED_VOCAL);
        assertThat(testPlaylist.getThumbnail()).isEqualTo(UPDATED_THUMBNAIL);

        // Validate the Playlist in Elasticsearch
        verify(mockPlaylistSearchRepository, times(1)).save(testPlaylist);
    }

    @Test
    @Transactional
    public void updateNonExistingPlaylist() throws Exception {
        int databaseSizeBeforeUpdate = playlistRepository.findAll().size();

        // Create the Playlist
        PlaylistDTO playlistDTO = playlistMapper.toDto(playlist);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPlaylistMockMvc.perform(put("/api/playlists")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(playlistDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Playlist in the database
        List<Playlist> playlistList = playlistRepository.findAll();
        assertThat(playlistList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Playlist in Elasticsearch
        verify(mockPlaylistSearchRepository, times(0)).save(playlist);
    }

    @Test
    @Transactional
    public void deletePlaylist() throws Exception {
        // Initialize the database
        playlistRepository.saveAndFlush(playlist);

        int databaseSizeBeforeDelete = playlistRepository.findAll().size();

        // Delete the playlist
        restPlaylistMockMvc.perform(delete("/api/playlists/{id}", playlist.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Playlist> playlistList = playlistRepository.findAll();
        assertThat(playlistList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Playlist in Elasticsearch
        verify(mockPlaylistSearchRepository, times(1)).deleteById(playlist.getId());
    }

    @Test
    @Transactional
    public void searchPlaylist() throws Exception {
        // Initialize the database
        playlistRepository.saveAndFlush(playlist);
        when(mockPlaylistSearchRepository.search(queryStringQuery("id:" + playlist.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(playlist), PageRequest.of(0, 1), 1));
        // Search the playlist
        restPlaylistMockMvc.perform(get("/api/_search/playlists?query=id:" + playlist.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(playlist.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].vocal").value(hasItem(DEFAULT_VOCAL)))
            .andExpect(jsonPath("$.[*].thumbnail").value(hasItem(DEFAULT_THUMBNAIL)));
    }
}
