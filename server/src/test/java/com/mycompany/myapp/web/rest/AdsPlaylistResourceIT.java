package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.ServerApp;
import com.mycompany.myapp.domain.AdsPlaylist;
import com.mycompany.myapp.repository.AdsPlaylistRepository;
import com.mycompany.myapp.repository.search.AdsPlaylistSearchRepository;
import com.mycompany.myapp.service.AdsPlaylistService;
import com.mycompany.myapp.service.dto.AdsPlaylistDTO;
import com.mycompany.myapp.service.mapper.AdsPlaylistMapper;
import com.mycompany.myapp.web.rest.errors.ExceptionTranslator;
import com.mycompany.myapp.service.dto.AdsPlaylistCriteria;
import com.mycompany.myapp.service.AdsPlaylistQueryService;

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
 * Integration tests for the {@link AdsPlaylistResource} REST controller.
 */
@SpringBootTest(classes = ServerApp.class)
public class AdsPlaylistResourceIT {

    private static final String DEFAULT_ADS_IMAGE = "AAAAAAAAAA";
    private static final String UPDATED_ADS_IMAGE = "BBBBBBBBBB";

    private static final String DEFAULT_ADS_CONTENT = "AAAAAAAAAA";
    private static final String UPDATED_ADS_CONTENT = "BBBBBBBBBB";

    private static final Integer DEFAULT_PLAYLIST_ID = 1;
    private static final Integer UPDATED_PLAYLIST_ID = 2;
    private static final Integer SMALLER_PLAYLIST_ID = 1 - 1;

    private static final String DEFAULT_PLAYLIST_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_PLAYLIST_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_PLAYLIST_IMAGE = "AAAAAAAAAA";
    private static final String UPDATED_PLAYLIST_IMAGE = "BBBBBBBBBB";

    @Autowired
    private AdsPlaylistRepository adsPlaylistRepository;

    @Autowired
    private AdsPlaylistMapper adsPlaylistMapper;

    @Autowired
    private AdsPlaylistService adsPlaylistService;

    /**
     * This repository is mocked in the com.mycompany.myapp.repository.search test package.
     *
     * @see com.mycompany.myapp.repository.search.AdsPlaylistSearchRepositoryMockConfiguration
     */
    @Autowired
    private AdsPlaylistSearchRepository mockAdsPlaylistSearchRepository;

    @Autowired
    private AdsPlaylistQueryService adsPlaylistQueryService;

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

    private MockMvc restAdsPlaylistMockMvc;

    private AdsPlaylist adsPlaylist;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final AdsPlaylistResource adsPlaylistResource = new AdsPlaylistResource(adsPlaylistService, adsPlaylistQueryService);
        this.restAdsPlaylistMockMvc = MockMvcBuilders.standaloneSetup(adsPlaylistResource)
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
    public static AdsPlaylist createEntity(EntityManager em) {
        AdsPlaylist adsPlaylist = new AdsPlaylist()
            .adsImage(DEFAULT_ADS_IMAGE)
            .adsContent(DEFAULT_ADS_CONTENT)
            .playlistId(DEFAULT_PLAYLIST_ID)
            .playlistTitle(DEFAULT_PLAYLIST_TITLE)
            .playlistImage(DEFAULT_PLAYLIST_IMAGE);
        return adsPlaylist;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AdsPlaylist createUpdatedEntity(EntityManager em) {
        AdsPlaylist adsPlaylist = new AdsPlaylist()
            .adsImage(UPDATED_ADS_IMAGE)
            .adsContent(UPDATED_ADS_CONTENT)
            .playlistId(UPDATED_PLAYLIST_ID)
            .playlistTitle(UPDATED_PLAYLIST_TITLE)
            .playlistImage(UPDATED_PLAYLIST_IMAGE);
        return adsPlaylist;
    }

    @BeforeEach
    public void initTest() {
        adsPlaylist = createEntity(em);
    }

    @Test
    @Transactional
    public void createAdsPlaylist() throws Exception {
        int databaseSizeBeforeCreate = adsPlaylistRepository.findAll().size();

        // Create the AdsPlaylist
        AdsPlaylistDTO adsPlaylistDTO = adsPlaylistMapper.toDto(adsPlaylist);
        restAdsPlaylistMockMvc.perform(post("/api/ads-playlists")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(adsPlaylistDTO)))
            .andExpect(status().isCreated());

        // Validate the AdsPlaylist in the database
        List<AdsPlaylist> adsPlaylistList = adsPlaylistRepository.findAll();
        assertThat(adsPlaylistList).hasSize(databaseSizeBeforeCreate + 1);
        AdsPlaylist testAdsPlaylist = adsPlaylistList.get(adsPlaylistList.size() - 1);
        assertThat(testAdsPlaylist.getAdsImage()).isEqualTo(DEFAULT_ADS_IMAGE);
        assertThat(testAdsPlaylist.getAdsContent()).isEqualTo(DEFAULT_ADS_CONTENT);
        assertThat(testAdsPlaylist.getPlaylistId()).isEqualTo(DEFAULT_PLAYLIST_ID);
        assertThat(testAdsPlaylist.getPlaylistTitle()).isEqualTo(DEFAULT_PLAYLIST_TITLE);
        assertThat(testAdsPlaylist.getPlaylistImage()).isEqualTo(DEFAULT_PLAYLIST_IMAGE);

        // Validate the AdsPlaylist in Elasticsearch
        verify(mockAdsPlaylistSearchRepository, times(1)).save(testAdsPlaylist);
    }

    @Test
    @Transactional
    public void createAdsPlaylistWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = adsPlaylistRepository.findAll().size();

        // Create the AdsPlaylist with an existing ID
        adsPlaylist.setId(1L);
        AdsPlaylistDTO adsPlaylistDTO = adsPlaylistMapper.toDto(adsPlaylist);

        // An entity with an existing ID cannot be created, so this API call must fail
        restAdsPlaylistMockMvc.perform(post("/api/ads-playlists")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(adsPlaylistDTO)))
            .andExpect(status().isBadRequest());

        // Validate the AdsPlaylist in the database
        List<AdsPlaylist> adsPlaylistList = adsPlaylistRepository.findAll();
        assertThat(adsPlaylistList).hasSize(databaseSizeBeforeCreate);

        // Validate the AdsPlaylist in Elasticsearch
        verify(mockAdsPlaylistSearchRepository, times(0)).save(adsPlaylist);
    }


    @Test
    @Transactional
    public void getAllAdsPlaylists() throws Exception {
        // Initialize the database
        adsPlaylistRepository.saveAndFlush(adsPlaylist);

        // Get all the adsPlaylistList
        restAdsPlaylistMockMvc.perform(get("/api/ads-playlists?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(adsPlaylist.getId().intValue())))
            .andExpect(jsonPath("$.[*].adsImage").value(hasItem(DEFAULT_ADS_IMAGE)))
            .andExpect(jsonPath("$.[*].adsContent").value(hasItem(DEFAULT_ADS_CONTENT)))
            .andExpect(jsonPath("$.[*].playlistId").value(hasItem(DEFAULT_PLAYLIST_ID)))
            .andExpect(jsonPath("$.[*].playlistTitle").value(hasItem(DEFAULT_PLAYLIST_TITLE)))
            .andExpect(jsonPath("$.[*].playlistImage").value(hasItem(DEFAULT_PLAYLIST_IMAGE)));
    }
    
    @Test
    @Transactional
    public void getAdsPlaylist() throws Exception {
        // Initialize the database
        adsPlaylistRepository.saveAndFlush(adsPlaylist);

        // Get the adsPlaylist
        restAdsPlaylistMockMvc.perform(get("/api/ads-playlists/{id}", adsPlaylist.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(adsPlaylist.getId().intValue()))
            .andExpect(jsonPath("$.adsImage").value(DEFAULT_ADS_IMAGE))
            .andExpect(jsonPath("$.adsContent").value(DEFAULT_ADS_CONTENT))
            .andExpect(jsonPath("$.playlistId").value(DEFAULT_PLAYLIST_ID))
            .andExpect(jsonPath("$.playlistTitle").value(DEFAULT_PLAYLIST_TITLE))
            .andExpect(jsonPath("$.playlistImage").value(DEFAULT_PLAYLIST_IMAGE));
    }


    @Test
    @Transactional
    public void getAdsPlaylistsByIdFiltering() throws Exception {
        // Initialize the database
        adsPlaylistRepository.saveAndFlush(adsPlaylist);

        Long id = adsPlaylist.getId();

        defaultAdsPlaylistShouldBeFound("id.equals=" + id);
        defaultAdsPlaylistShouldNotBeFound("id.notEquals=" + id);

        defaultAdsPlaylistShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultAdsPlaylistShouldNotBeFound("id.greaterThan=" + id);

        defaultAdsPlaylistShouldBeFound("id.lessThanOrEqual=" + id);
        defaultAdsPlaylistShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllAdsPlaylistsByAdsImageIsEqualToSomething() throws Exception {
        // Initialize the database
        adsPlaylistRepository.saveAndFlush(adsPlaylist);

        // Get all the adsPlaylistList where adsImage equals to DEFAULT_ADS_IMAGE
        defaultAdsPlaylistShouldBeFound("adsImage.equals=" + DEFAULT_ADS_IMAGE);

        // Get all the adsPlaylistList where adsImage equals to UPDATED_ADS_IMAGE
        defaultAdsPlaylistShouldNotBeFound("adsImage.equals=" + UPDATED_ADS_IMAGE);
    }

    @Test
    @Transactional
    public void getAllAdsPlaylistsByAdsImageIsNotEqualToSomething() throws Exception {
        // Initialize the database
        adsPlaylistRepository.saveAndFlush(adsPlaylist);

        // Get all the adsPlaylistList where adsImage not equals to DEFAULT_ADS_IMAGE
        defaultAdsPlaylistShouldNotBeFound("adsImage.notEquals=" + DEFAULT_ADS_IMAGE);

        // Get all the adsPlaylistList where adsImage not equals to UPDATED_ADS_IMAGE
        defaultAdsPlaylistShouldBeFound("adsImage.notEquals=" + UPDATED_ADS_IMAGE);
    }

    @Test
    @Transactional
    public void getAllAdsPlaylistsByAdsImageIsInShouldWork() throws Exception {
        // Initialize the database
        adsPlaylistRepository.saveAndFlush(adsPlaylist);

        // Get all the adsPlaylistList where adsImage in DEFAULT_ADS_IMAGE or UPDATED_ADS_IMAGE
        defaultAdsPlaylistShouldBeFound("adsImage.in=" + DEFAULT_ADS_IMAGE + "," + UPDATED_ADS_IMAGE);

        // Get all the adsPlaylistList where adsImage equals to UPDATED_ADS_IMAGE
        defaultAdsPlaylistShouldNotBeFound("adsImage.in=" + UPDATED_ADS_IMAGE);
    }

    @Test
    @Transactional
    public void getAllAdsPlaylistsByAdsImageIsNullOrNotNull() throws Exception {
        // Initialize the database
        adsPlaylistRepository.saveAndFlush(adsPlaylist);

        // Get all the adsPlaylistList where adsImage is not null
        defaultAdsPlaylistShouldBeFound("adsImage.specified=true");

        // Get all the adsPlaylistList where adsImage is null
        defaultAdsPlaylistShouldNotBeFound("adsImage.specified=false");
    }
                @Test
    @Transactional
    public void getAllAdsPlaylistsByAdsImageContainsSomething() throws Exception {
        // Initialize the database
        adsPlaylistRepository.saveAndFlush(adsPlaylist);

        // Get all the adsPlaylistList where adsImage contains DEFAULT_ADS_IMAGE
        defaultAdsPlaylistShouldBeFound("adsImage.contains=" + DEFAULT_ADS_IMAGE);

        // Get all the adsPlaylistList where adsImage contains UPDATED_ADS_IMAGE
        defaultAdsPlaylistShouldNotBeFound("adsImage.contains=" + UPDATED_ADS_IMAGE);
    }

    @Test
    @Transactional
    public void getAllAdsPlaylistsByAdsImageNotContainsSomething() throws Exception {
        // Initialize the database
        adsPlaylistRepository.saveAndFlush(adsPlaylist);

        // Get all the adsPlaylistList where adsImage does not contain DEFAULT_ADS_IMAGE
        defaultAdsPlaylistShouldNotBeFound("adsImage.doesNotContain=" + DEFAULT_ADS_IMAGE);

        // Get all the adsPlaylistList where adsImage does not contain UPDATED_ADS_IMAGE
        defaultAdsPlaylistShouldBeFound("adsImage.doesNotContain=" + UPDATED_ADS_IMAGE);
    }


    @Test
    @Transactional
    public void getAllAdsPlaylistsByAdsContentIsEqualToSomething() throws Exception {
        // Initialize the database
        adsPlaylistRepository.saveAndFlush(adsPlaylist);

        // Get all the adsPlaylistList where adsContent equals to DEFAULT_ADS_CONTENT
        defaultAdsPlaylistShouldBeFound("adsContent.equals=" + DEFAULT_ADS_CONTENT);

        // Get all the adsPlaylistList where adsContent equals to UPDATED_ADS_CONTENT
        defaultAdsPlaylistShouldNotBeFound("adsContent.equals=" + UPDATED_ADS_CONTENT);
    }

    @Test
    @Transactional
    public void getAllAdsPlaylistsByAdsContentIsNotEqualToSomething() throws Exception {
        // Initialize the database
        adsPlaylistRepository.saveAndFlush(adsPlaylist);

        // Get all the adsPlaylistList where adsContent not equals to DEFAULT_ADS_CONTENT
        defaultAdsPlaylistShouldNotBeFound("adsContent.notEquals=" + DEFAULT_ADS_CONTENT);

        // Get all the adsPlaylistList where adsContent not equals to UPDATED_ADS_CONTENT
        defaultAdsPlaylistShouldBeFound("adsContent.notEquals=" + UPDATED_ADS_CONTENT);
    }

    @Test
    @Transactional
    public void getAllAdsPlaylistsByAdsContentIsInShouldWork() throws Exception {
        // Initialize the database
        adsPlaylistRepository.saveAndFlush(adsPlaylist);

        // Get all the adsPlaylistList where adsContent in DEFAULT_ADS_CONTENT or UPDATED_ADS_CONTENT
        defaultAdsPlaylistShouldBeFound("adsContent.in=" + DEFAULT_ADS_CONTENT + "," + UPDATED_ADS_CONTENT);

        // Get all the adsPlaylistList where adsContent equals to UPDATED_ADS_CONTENT
        defaultAdsPlaylistShouldNotBeFound("adsContent.in=" + UPDATED_ADS_CONTENT);
    }

    @Test
    @Transactional
    public void getAllAdsPlaylistsByAdsContentIsNullOrNotNull() throws Exception {
        // Initialize the database
        adsPlaylistRepository.saveAndFlush(adsPlaylist);

        // Get all the adsPlaylistList where adsContent is not null
        defaultAdsPlaylistShouldBeFound("adsContent.specified=true");

        // Get all the adsPlaylistList where adsContent is null
        defaultAdsPlaylistShouldNotBeFound("adsContent.specified=false");
    }
                @Test
    @Transactional
    public void getAllAdsPlaylistsByAdsContentContainsSomething() throws Exception {
        // Initialize the database
        adsPlaylistRepository.saveAndFlush(adsPlaylist);

        // Get all the adsPlaylistList where adsContent contains DEFAULT_ADS_CONTENT
        defaultAdsPlaylistShouldBeFound("adsContent.contains=" + DEFAULT_ADS_CONTENT);

        // Get all the adsPlaylistList where adsContent contains UPDATED_ADS_CONTENT
        defaultAdsPlaylistShouldNotBeFound("adsContent.contains=" + UPDATED_ADS_CONTENT);
    }

    @Test
    @Transactional
    public void getAllAdsPlaylistsByAdsContentNotContainsSomething() throws Exception {
        // Initialize the database
        adsPlaylistRepository.saveAndFlush(adsPlaylist);

        // Get all the adsPlaylistList where adsContent does not contain DEFAULT_ADS_CONTENT
        defaultAdsPlaylistShouldNotBeFound("adsContent.doesNotContain=" + DEFAULT_ADS_CONTENT);

        // Get all the adsPlaylistList where adsContent does not contain UPDATED_ADS_CONTENT
        defaultAdsPlaylistShouldBeFound("adsContent.doesNotContain=" + UPDATED_ADS_CONTENT);
    }


    @Test
    @Transactional
    public void getAllAdsPlaylistsByPlaylistIdIsEqualToSomething() throws Exception {
        // Initialize the database
        adsPlaylistRepository.saveAndFlush(adsPlaylist);

        // Get all the adsPlaylistList where playlistId equals to DEFAULT_PLAYLIST_ID
        defaultAdsPlaylistShouldBeFound("playlistId.equals=" + DEFAULT_PLAYLIST_ID);

        // Get all the adsPlaylistList where playlistId equals to UPDATED_PLAYLIST_ID
        defaultAdsPlaylistShouldNotBeFound("playlistId.equals=" + UPDATED_PLAYLIST_ID);
    }

    @Test
    @Transactional
    public void getAllAdsPlaylistsByPlaylistIdIsNotEqualToSomething() throws Exception {
        // Initialize the database
        adsPlaylistRepository.saveAndFlush(adsPlaylist);

        // Get all the adsPlaylistList where playlistId not equals to DEFAULT_PLAYLIST_ID
        defaultAdsPlaylistShouldNotBeFound("playlistId.notEquals=" + DEFAULT_PLAYLIST_ID);

        // Get all the adsPlaylistList where playlistId not equals to UPDATED_PLAYLIST_ID
        defaultAdsPlaylistShouldBeFound("playlistId.notEquals=" + UPDATED_PLAYLIST_ID);
    }

    @Test
    @Transactional
    public void getAllAdsPlaylistsByPlaylistIdIsInShouldWork() throws Exception {
        // Initialize the database
        adsPlaylistRepository.saveAndFlush(adsPlaylist);

        // Get all the adsPlaylistList where playlistId in DEFAULT_PLAYLIST_ID or UPDATED_PLAYLIST_ID
        defaultAdsPlaylistShouldBeFound("playlistId.in=" + DEFAULT_PLAYLIST_ID + "," + UPDATED_PLAYLIST_ID);

        // Get all the adsPlaylistList where playlistId equals to UPDATED_PLAYLIST_ID
        defaultAdsPlaylistShouldNotBeFound("playlistId.in=" + UPDATED_PLAYLIST_ID);
    }

    @Test
    @Transactional
    public void getAllAdsPlaylistsByPlaylistIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        adsPlaylistRepository.saveAndFlush(adsPlaylist);

        // Get all the adsPlaylistList where playlistId is not null
        defaultAdsPlaylistShouldBeFound("playlistId.specified=true");

        // Get all the adsPlaylistList where playlistId is null
        defaultAdsPlaylistShouldNotBeFound("playlistId.specified=false");
    }

    @Test
    @Transactional
    public void getAllAdsPlaylistsByPlaylistIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        adsPlaylistRepository.saveAndFlush(adsPlaylist);

        // Get all the adsPlaylistList where playlistId is greater than or equal to DEFAULT_PLAYLIST_ID
        defaultAdsPlaylistShouldBeFound("playlistId.greaterThanOrEqual=" + DEFAULT_PLAYLIST_ID);

        // Get all the adsPlaylistList where playlistId is greater than or equal to UPDATED_PLAYLIST_ID
        defaultAdsPlaylistShouldNotBeFound("playlistId.greaterThanOrEqual=" + UPDATED_PLAYLIST_ID);
    }

    @Test
    @Transactional
    public void getAllAdsPlaylistsByPlaylistIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        adsPlaylistRepository.saveAndFlush(adsPlaylist);

        // Get all the adsPlaylistList where playlistId is less than or equal to DEFAULT_PLAYLIST_ID
        defaultAdsPlaylistShouldBeFound("playlistId.lessThanOrEqual=" + DEFAULT_PLAYLIST_ID);

        // Get all the adsPlaylistList where playlistId is less than or equal to SMALLER_PLAYLIST_ID
        defaultAdsPlaylistShouldNotBeFound("playlistId.lessThanOrEqual=" + SMALLER_PLAYLIST_ID);
    }

    @Test
    @Transactional
    public void getAllAdsPlaylistsByPlaylistIdIsLessThanSomething() throws Exception {
        // Initialize the database
        adsPlaylistRepository.saveAndFlush(adsPlaylist);

        // Get all the adsPlaylistList where playlistId is less than DEFAULT_PLAYLIST_ID
        defaultAdsPlaylistShouldNotBeFound("playlistId.lessThan=" + DEFAULT_PLAYLIST_ID);

        // Get all the adsPlaylistList where playlistId is less than UPDATED_PLAYLIST_ID
        defaultAdsPlaylistShouldBeFound("playlistId.lessThan=" + UPDATED_PLAYLIST_ID);
    }

    @Test
    @Transactional
    public void getAllAdsPlaylistsByPlaylistIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        adsPlaylistRepository.saveAndFlush(adsPlaylist);

        // Get all the adsPlaylistList where playlistId is greater than DEFAULT_PLAYLIST_ID
        defaultAdsPlaylistShouldNotBeFound("playlistId.greaterThan=" + DEFAULT_PLAYLIST_ID);

        // Get all the adsPlaylistList where playlistId is greater than SMALLER_PLAYLIST_ID
        defaultAdsPlaylistShouldBeFound("playlistId.greaterThan=" + SMALLER_PLAYLIST_ID);
    }


    @Test
    @Transactional
    public void getAllAdsPlaylistsByPlaylistTitleIsEqualToSomething() throws Exception {
        // Initialize the database
        adsPlaylistRepository.saveAndFlush(adsPlaylist);

        // Get all the adsPlaylistList where playlistTitle equals to DEFAULT_PLAYLIST_TITLE
        defaultAdsPlaylistShouldBeFound("playlistTitle.equals=" + DEFAULT_PLAYLIST_TITLE);

        // Get all the adsPlaylistList where playlistTitle equals to UPDATED_PLAYLIST_TITLE
        defaultAdsPlaylistShouldNotBeFound("playlistTitle.equals=" + UPDATED_PLAYLIST_TITLE);
    }

    @Test
    @Transactional
    public void getAllAdsPlaylistsByPlaylistTitleIsNotEqualToSomething() throws Exception {
        // Initialize the database
        adsPlaylistRepository.saveAndFlush(adsPlaylist);

        // Get all the adsPlaylistList where playlistTitle not equals to DEFAULT_PLAYLIST_TITLE
        defaultAdsPlaylistShouldNotBeFound("playlistTitle.notEquals=" + DEFAULT_PLAYLIST_TITLE);

        // Get all the adsPlaylistList where playlistTitle not equals to UPDATED_PLAYLIST_TITLE
        defaultAdsPlaylistShouldBeFound("playlistTitle.notEquals=" + UPDATED_PLAYLIST_TITLE);
    }

    @Test
    @Transactional
    public void getAllAdsPlaylistsByPlaylistTitleIsInShouldWork() throws Exception {
        // Initialize the database
        adsPlaylistRepository.saveAndFlush(adsPlaylist);

        // Get all the adsPlaylistList where playlistTitle in DEFAULT_PLAYLIST_TITLE or UPDATED_PLAYLIST_TITLE
        defaultAdsPlaylistShouldBeFound("playlistTitle.in=" + DEFAULT_PLAYLIST_TITLE + "," + UPDATED_PLAYLIST_TITLE);

        // Get all the adsPlaylistList where playlistTitle equals to UPDATED_PLAYLIST_TITLE
        defaultAdsPlaylistShouldNotBeFound("playlistTitle.in=" + UPDATED_PLAYLIST_TITLE);
    }

    @Test
    @Transactional
    public void getAllAdsPlaylistsByPlaylistTitleIsNullOrNotNull() throws Exception {
        // Initialize the database
        adsPlaylistRepository.saveAndFlush(adsPlaylist);

        // Get all the adsPlaylistList where playlistTitle is not null
        defaultAdsPlaylistShouldBeFound("playlistTitle.specified=true");

        // Get all the adsPlaylistList where playlistTitle is null
        defaultAdsPlaylistShouldNotBeFound("playlistTitle.specified=false");
    }
                @Test
    @Transactional
    public void getAllAdsPlaylistsByPlaylistTitleContainsSomething() throws Exception {
        // Initialize the database
        adsPlaylistRepository.saveAndFlush(adsPlaylist);

        // Get all the adsPlaylistList where playlistTitle contains DEFAULT_PLAYLIST_TITLE
        defaultAdsPlaylistShouldBeFound("playlistTitle.contains=" + DEFAULT_PLAYLIST_TITLE);

        // Get all the adsPlaylistList where playlistTitle contains UPDATED_PLAYLIST_TITLE
        defaultAdsPlaylistShouldNotBeFound("playlistTitle.contains=" + UPDATED_PLAYLIST_TITLE);
    }

    @Test
    @Transactional
    public void getAllAdsPlaylistsByPlaylistTitleNotContainsSomething() throws Exception {
        // Initialize the database
        adsPlaylistRepository.saveAndFlush(adsPlaylist);

        // Get all the adsPlaylistList where playlistTitle does not contain DEFAULT_PLAYLIST_TITLE
        defaultAdsPlaylistShouldNotBeFound("playlistTitle.doesNotContain=" + DEFAULT_PLAYLIST_TITLE);

        // Get all the adsPlaylistList where playlistTitle does not contain UPDATED_PLAYLIST_TITLE
        defaultAdsPlaylistShouldBeFound("playlistTitle.doesNotContain=" + UPDATED_PLAYLIST_TITLE);
    }


    @Test
    @Transactional
    public void getAllAdsPlaylistsByPlaylistImageIsEqualToSomething() throws Exception {
        // Initialize the database
        adsPlaylistRepository.saveAndFlush(adsPlaylist);

        // Get all the adsPlaylistList where playlistImage equals to DEFAULT_PLAYLIST_IMAGE
        defaultAdsPlaylistShouldBeFound("playlistImage.equals=" + DEFAULT_PLAYLIST_IMAGE);

        // Get all the adsPlaylistList where playlistImage equals to UPDATED_PLAYLIST_IMAGE
        defaultAdsPlaylistShouldNotBeFound("playlistImage.equals=" + UPDATED_PLAYLIST_IMAGE);
    }

    @Test
    @Transactional
    public void getAllAdsPlaylistsByPlaylistImageIsNotEqualToSomething() throws Exception {
        // Initialize the database
        adsPlaylistRepository.saveAndFlush(adsPlaylist);

        // Get all the adsPlaylistList where playlistImage not equals to DEFAULT_PLAYLIST_IMAGE
        defaultAdsPlaylistShouldNotBeFound("playlistImage.notEquals=" + DEFAULT_PLAYLIST_IMAGE);

        // Get all the adsPlaylistList where playlistImage not equals to UPDATED_PLAYLIST_IMAGE
        defaultAdsPlaylistShouldBeFound("playlistImage.notEquals=" + UPDATED_PLAYLIST_IMAGE);
    }

    @Test
    @Transactional
    public void getAllAdsPlaylistsByPlaylistImageIsInShouldWork() throws Exception {
        // Initialize the database
        adsPlaylistRepository.saveAndFlush(adsPlaylist);

        // Get all the adsPlaylistList where playlistImage in DEFAULT_PLAYLIST_IMAGE or UPDATED_PLAYLIST_IMAGE
        defaultAdsPlaylistShouldBeFound("playlistImage.in=" + DEFAULT_PLAYLIST_IMAGE + "," + UPDATED_PLAYLIST_IMAGE);

        // Get all the adsPlaylistList where playlistImage equals to UPDATED_PLAYLIST_IMAGE
        defaultAdsPlaylistShouldNotBeFound("playlistImage.in=" + UPDATED_PLAYLIST_IMAGE);
    }

    @Test
    @Transactional
    public void getAllAdsPlaylistsByPlaylistImageIsNullOrNotNull() throws Exception {
        // Initialize the database
        adsPlaylistRepository.saveAndFlush(adsPlaylist);

        // Get all the adsPlaylistList where playlistImage is not null
        defaultAdsPlaylistShouldBeFound("playlistImage.specified=true");

        // Get all the adsPlaylistList where playlistImage is null
        defaultAdsPlaylistShouldNotBeFound("playlistImage.specified=false");
    }
                @Test
    @Transactional
    public void getAllAdsPlaylistsByPlaylistImageContainsSomething() throws Exception {
        // Initialize the database
        adsPlaylistRepository.saveAndFlush(adsPlaylist);

        // Get all the adsPlaylistList where playlistImage contains DEFAULT_PLAYLIST_IMAGE
        defaultAdsPlaylistShouldBeFound("playlistImage.contains=" + DEFAULT_PLAYLIST_IMAGE);

        // Get all the adsPlaylistList where playlistImage contains UPDATED_PLAYLIST_IMAGE
        defaultAdsPlaylistShouldNotBeFound("playlistImage.contains=" + UPDATED_PLAYLIST_IMAGE);
    }

    @Test
    @Transactional
    public void getAllAdsPlaylistsByPlaylistImageNotContainsSomething() throws Exception {
        // Initialize the database
        adsPlaylistRepository.saveAndFlush(adsPlaylist);

        // Get all the adsPlaylistList where playlistImage does not contain DEFAULT_PLAYLIST_IMAGE
        defaultAdsPlaylistShouldNotBeFound("playlistImage.doesNotContain=" + DEFAULT_PLAYLIST_IMAGE);

        // Get all the adsPlaylistList where playlistImage does not contain UPDATED_PLAYLIST_IMAGE
        defaultAdsPlaylistShouldBeFound("playlistImage.doesNotContain=" + UPDATED_PLAYLIST_IMAGE);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultAdsPlaylistShouldBeFound(String filter) throws Exception {
        restAdsPlaylistMockMvc.perform(get("/api/ads-playlists?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(adsPlaylist.getId().intValue())))
            .andExpect(jsonPath("$.[*].adsImage").value(hasItem(DEFAULT_ADS_IMAGE)))
            .andExpect(jsonPath("$.[*].adsContent").value(hasItem(DEFAULT_ADS_CONTENT)))
            .andExpect(jsonPath("$.[*].playlistId").value(hasItem(DEFAULT_PLAYLIST_ID)))
            .andExpect(jsonPath("$.[*].playlistTitle").value(hasItem(DEFAULT_PLAYLIST_TITLE)))
            .andExpect(jsonPath("$.[*].playlistImage").value(hasItem(DEFAULT_PLAYLIST_IMAGE)));

        // Check, that the count call also returns 1
        restAdsPlaylistMockMvc.perform(get("/api/ads-playlists/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultAdsPlaylistShouldNotBeFound(String filter) throws Exception {
        restAdsPlaylistMockMvc.perform(get("/api/ads-playlists?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restAdsPlaylistMockMvc.perform(get("/api/ads-playlists/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingAdsPlaylist() throws Exception {
        // Get the adsPlaylist
        restAdsPlaylistMockMvc.perform(get("/api/ads-playlists/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAdsPlaylist() throws Exception {
        // Initialize the database
        adsPlaylistRepository.saveAndFlush(adsPlaylist);

        int databaseSizeBeforeUpdate = adsPlaylistRepository.findAll().size();

        // Update the adsPlaylist
        AdsPlaylist updatedAdsPlaylist = adsPlaylistRepository.findById(adsPlaylist.getId()).get();
        // Disconnect from session so that the updates on updatedAdsPlaylist are not directly saved in db
        em.detach(updatedAdsPlaylist);
        updatedAdsPlaylist
            .adsImage(UPDATED_ADS_IMAGE)
            .adsContent(UPDATED_ADS_CONTENT)
            .playlistId(UPDATED_PLAYLIST_ID)
            .playlistTitle(UPDATED_PLAYLIST_TITLE)
            .playlistImage(UPDATED_PLAYLIST_IMAGE);
        AdsPlaylistDTO adsPlaylistDTO = adsPlaylistMapper.toDto(updatedAdsPlaylist);

        restAdsPlaylistMockMvc.perform(put("/api/ads-playlists")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(adsPlaylistDTO)))
            .andExpect(status().isOk());

        // Validate the AdsPlaylist in the database
        List<AdsPlaylist> adsPlaylistList = adsPlaylistRepository.findAll();
        assertThat(adsPlaylistList).hasSize(databaseSizeBeforeUpdate);
        AdsPlaylist testAdsPlaylist = adsPlaylistList.get(adsPlaylistList.size() - 1);
        assertThat(testAdsPlaylist.getAdsImage()).isEqualTo(UPDATED_ADS_IMAGE);
        assertThat(testAdsPlaylist.getAdsContent()).isEqualTo(UPDATED_ADS_CONTENT);
        assertThat(testAdsPlaylist.getPlaylistId()).isEqualTo(UPDATED_PLAYLIST_ID);
        assertThat(testAdsPlaylist.getPlaylistTitle()).isEqualTo(UPDATED_PLAYLIST_TITLE);
        assertThat(testAdsPlaylist.getPlaylistImage()).isEqualTo(UPDATED_PLAYLIST_IMAGE);

        // Validate the AdsPlaylist in Elasticsearch
        verify(mockAdsPlaylistSearchRepository, times(1)).save(testAdsPlaylist);
    }

    @Test
    @Transactional
    public void updateNonExistingAdsPlaylist() throws Exception {
        int databaseSizeBeforeUpdate = adsPlaylistRepository.findAll().size();

        // Create the AdsPlaylist
        AdsPlaylistDTO adsPlaylistDTO = adsPlaylistMapper.toDto(adsPlaylist);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAdsPlaylistMockMvc.perform(put("/api/ads-playlists")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(adsPlaylistDTO)))
            .andExpect(status().isBadRequest());

        // Validate the AdsPlaylist in the database
        List<AdsPlaylist> adsPlaylistList = adsPlaylistRepository.findAll();
        assertThat(adsPlaylistList).hasSize(databaseSizeBeforeUpdate);

        // Validate the AdsPlaylist in Elasticsearch
        verify(mockAdsPlaylistSearchRepository, times(0)).save(adsPlaylist);
    }

    @Test
    @Transactional
    public void deleteAdsPlaylist() throws Exception {
        // Initialize the database
        adsPlaylistRepository.saveAndFlush(adsPlaylist);

        int databaseSizeBeforeDelete = adsPlaylistRepository.findAll().size();

        // Delete the adsPlaylist
        restAdsPlaylistMockMvc.perform(delete("/api/ads-playlists/{id}", adsPlaylist.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<AdsPlaylist> adsPlaylistList = adsPlaylistRepository.findAll();
        assertThat(adsPlaylistList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the AdsPlaylist in Elasticsearch
        verify(mockAdsPlaylistSearchRepository, times(1)).deleteById(adsPlaylist.getId());
    }

    @Test
    @Transactional
    public void searchAdsPlaylist() throws Exception {
        // Initialize the database
        adsPlaylistRepository.saveAndFlush(adsPlaylist);
        when(mockAdsPlaylistSearchRepository.search(queryStringQuery("id:" + adsPlaylist.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(adsPlaylist), PageRequest.of(0, 1), 1));
        // Search the adsPlaylist
        restAdsPlaylistMockMvc.perform(get("/api/_search/ads-playlists?query=id:" + adsPlaylist.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(adsPlaylist.getId().intValue())))
            .andExpect(jsonPath("$.[*].adsImage").value(hasItem(DEFAULT_ADS_IMAGE)))
            .andExpect(jsonPath("$.[*].adsContent").value(hasItem(DEFAULT_ADS_CONTENT)))
            .andExpect(jsonPath("$.[*].playlistId").value(hasItem(DEFAULT_PLAYLIST_ID)))
            .andExpect(jsonPath("$.[*].playlistTitle").value(hasItem(DEFAULT_PLAYLIST_TITLE)))
            .andExpect(jsonPath("$.[*].playlistImage").value(hasItem(DEFAULT_PLAYLIST_IMAGE)));
    }
}
