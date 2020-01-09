package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.ServerApp;
import com.mycompany.myapp.domain.AdsSong;
import com.mycompany.myapp.repository.AdsSongRepository;
import com.mycompany.myapp.repository.search.AdsSongSearchRepository;
import com.mycompany.myapp.service.AdsSongService;
import com.mycompany.myapp.service.dto.AdsSongDTO;
import com.mycompany.myapp.service.mapper.AdsSongMapper;
import com.mycompany.myapp.web.rest.errors.ExceptionTranslator;
import com.mycompany.myapp.service.dto.AdsSongCriteria;
import com.mycompany.myapp.service.AdsSongQueryService;

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
 * Integration tests for the {@link AdsSongResource} REST controller.
 */
@SpringBootTest(classes = ServerApp.class)
public class AdsSongResourceIT {

    private static final String DEFAULT_ADS_IMAGE = "AAAAAAAAAA";
    private static final String UPDATED_ADS_IMAGE = "BBBBBBBBBB";

    private static final String DEFAULT_ADS_CONTENT = "AAAAAAAAAA";
    private static final String UPDATED_ADS_CONTENT = "BBBBBBBBBB";

    private static final Integer DEFAULT_SONG_ID = 1;
    private static final Integer UPDATED_SONG_ID = 2;
    private static final Integer SMALLER_SONG_ID = 1 - 1;

    private static final String DEFAULT_SONG_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_SONG_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_SONG_IMAGE = "AAAAAAAAAA";
    private static final String UPDATED_SONG_IMAGE = "BBBBBBBBBB";

    @Autowired
    private AdsSongRepository adsSongRepository;

    @Autowired
    private AdsSongMapper adsSongMapper;

    @Autowired
    private AdsSongService adsSongService;

    /**
     * This repository is mocked in the com.mycompany.myapp.repository.search test package.
     *
     * @see com.mycompany.myapp.repository.search.AdsSongSearchRepositoryMockConfiguration
     */
    @Autowired
    private AdsSongSearchRepository mockAdsSongSearchRepository;

    @Autowired
    private AdsSongQueryService adsSongQueryService;

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

    private MockMvc restAdsSongMockMvc;

    private AdsSong adsSong;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final AdsSongResource adsSongResource = new AdsSongResource(adsSongService, adsSongQueryService);
        this.restAdsSongMockMvc = MockMvcBuilders.standaloneSetup(adsSongResource)
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
    public static AdsSong createEntity(EntityManager em) {
        AdsSong adsSong = new AdsSong()
            .adsImage(DEFAULT_ADS_IMAGE)
            .adsContent(DEFAULT_ADS_CONTENT)
            .songId(DEFAULT_SONG_ID)
            .songTitle(DEFAULT_SONG_TITLE)
            .songImage(DEFAULT_SONG_IMAGE);
        return adsSong;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AdsSong createUpdatedEntity(EntityManager em) {
        AdsSong adsSong = new AdsSong()
            .adsImage(UPDATED_ADS_IMAGE)
            .adsContent(UPDATED_ADS_CONTENT)
            .songId(UPDATED_SONG_ID)
            .songTitle(UPDATED_SONG_TITLE)
            .songImage(UPDATED_SONG_IMAGE);
        return adsSong;
    }

    @BeforeEach
    public void initTest() {
        adsSong = createEntity(em);
    }

    @Test
    @Transactional
    public void createAdsSong() throws Exception {
        int databaseSizeBeforeCreate = adsSongRepository.findAll().size();

        // Create the AdsSong
        AdsSongDTO adsSongDTO = adsSongMapper.toDto(adsSong);
        restAdsSongMockMvc.perform(post("/api/ads-songs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(adsSongDTO)))
            .andExpect(status().isCreated());

        // Validate the AdsSong in the database
        List<AdsSong> adsSongList = adsSongRepository.findAll();
        assertThat(adsSongList).hasSize(databaseSizeBeforeCreate + 1);
        AdsSong testAdsSong = adsSongList.get(adsSongList.size() - 1);
        assertThat(testAdsSong.getAdsImage()).isEqualTo(DEFAULT_ADS_IMAGE);
        assertThat(testAdsSong.getAdsContent()).isEqualTo(DEFAULT_ADS_CONTENT);
        assertThat(testAdsSong.getSongId()).isEqualTo(DEFAULT_SONG_ID);
        assertThat(testAdsSong.getSongTitle()).isEqualTo(DEFAULT_SONG_TITLE);
        assertThat(testAdsSong.getSongImage()).isEqualTo(DEFAULT_SONG_IMAGE);

        // Validate the AdsSong in Elasticsearch
        verify(mockAdsSongSearchRepository, times(1)).save(testAdsSong);
    }

    @Test
    @Transactional
    public void createAdsSongWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = adsSongRepository.findAll().size();

        // Create the AdsSong with an existing ID
        adsSong.setId(1L);
        AdsSongDTO adsSongDTO = adsSongMapper.toDto(adsSong);

        // An entity with an existing ID cannot be created, so this API call must fail
        restAdsSongMockMvc.perform(post("/api/ads-songs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(adsSongDTO)))
            .andExpect(status().isBadRequest());

        // Validate the AdsSong in the database
        List<AdsSong> adsSongList = adsSongRepository.findAll();
        assertThat(adsSongList).hasSize(databaseSizeBeforeCreate);

        // Validate the AdsSong in Elasticsearch
        verify(mockAdsSongSearchRepository, times(0)).save(adsSong);
    }


    @Test
    @Transactional
    public void getAllAdsSongs() throws Exception {
        // Initialize the database
        adsSongRepository.saveAndFlush(adsSong);

        // Get all the adsSongList
        restAdsSongMockMvc.perform(get("/api/ads-songs?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(adsSong.getId().intValue())))
            .andExpect(jsonPath("$.[*].adsImage").value(hasItem(DEFAULT_ADS_IMAGE)))
            .andExpect(jsonPath("$.[*].adsContent").value(hasItem(DEFAULT_ADS_CONTENT)))
            .andExpect(jsonPath("$.[*].songId").value(hasItem(DEFAULT_SONG_ID)))
            .andExpect(jsonPath("$.[*].songTitle").value(hasItem(DEFAULT_SONG_TITLE)))
            .andExpect(jsonPath("$.[*].songImage").value(hasItem(DEFAULT_SONG_IMAGE)));
    }
    
    @Test
    @Transactional
    public void getAdsSong() throws Exception {
        // Initialize the database
        adsSongRepository.saveAndFlush(adsSong);

        // Get the adsSong
        restAdsSongMockMvc.perform(get("/api/ads-songs/{id}", adsSong.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(adsSong.getId().intValue()))
            .andExpect(jsonPath("$.adsImage").value(DEFAULT_ADS_IMAGE))
            .andExpect(jsonPath("$.adsContent").value(DEFAULT_ADS_CONTENT))
            .andExpect(jsonPath("$.songId").value(DEFAULT_SONG_ID))
            .andExpect(jsonPath("$.songTitle").value(DEFAULT_SONG_TITLE))
            .andExpect(jsonPath("$.songImage").value(DEFAULT_SONG_IMAGE));
    }


    @Test
    @Transactional
    public void getAdsSongsByIdFiltering() throws Exception {
        // Initialize the database
        adsSongRepository.saveAndFlush(adsSong);

        Long id = adsSong.getId();

        defaultAdsSongShouldBeFound("id.equals=" + id);
        defaultAdsSongShouldNotBeFound("id.notEquals=" + id);

        defaultAdsSongShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultAdsSongShouldNotBeFound("id.greaterThan=" + id);

        defaultAdsSongShouldBeFound("id.lessThanOrEqual=" + id);
        defaultAdsSongShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllAdsSongsByAdsImageIsEqualToSomething() throws Exception {
        // Initialize the database
        adsSongRepository.saveAndFlush(adsSong);

        // Get all the adsSongList where adsImage equals to DEFAULT_ADS_IMAGE
        defaultAdsSongShouldBeFound("adsImage.equals=" + DEFAULT_ADS_IMAGE);

        // Get all the adsSongList where adsImage equals to UPDATED_ADS_IMAGE
        defaultAdsSongShouldNotBeFound("adsImage.equals=" + UPDATED_ADS_IMAGE);
    }

    @Test
    @Transactional
    public void getAllAdsSongsByAdsImageIsNotEqualToSomething() throws Exception {
        // Initialize the database
        adsSongRepository.saveAndFlush(adsSong);

        // Get all the adsSongList where adsImage not equals to DEFAULT_ADS_IMAGE
        defaultAdsSongShouldNotBeFound("adsImage.notEquals=" + DEFAULT_ADS_IMAGE);

        // Get all the adsSongList where adsImage not equals to UPDATED_ADS_IMAGE
        defaultAdsSongShouldBeFound("adsImage.notEquals=" + UPDATED_ADS_IMAGE);
    }

    @Test
    @Transactional
    public void getAllAdsSongsByAdsImageIsInShouldWork() throws Exception {
        // Initialize the database
        adsSongRepository.saveAndFlush(adsSong);

        // Get all the adsSongList where adsImage in DEFAULT_ADS_IMAGE or UPDATED_ADS_IMAGE
        defaultAdsSongShouldBeFound("adsImage.in=" + DEFAULT_ADS_IMAGE + "," + UPDATED_ADS_IMAGE);

        // Get all the adsSongList where adsImage equals to UPDATED_ADS_IMAGE
        defaultAdsSongShouldNotBeFound("adsImage.in=" + UPDATED_ADS_IMAGE);
    }

    @Test
    @Transactional
    public void getAllAdsSongsByAdsImageIsNullOrNotNull() throws Exception {
        // Initialize the database
        adsSongRepository.saveAndFlush(adsSong);

        // Get all the adsSongList where adsImage is not null
        defaultAdsSongShouldBeFound("adsImage.specified=true");

        // Get all the adsSongList where adsImage is null
        defaultAdsSongShouldNotBeFound("adsImage.specified=false");
    }
                @Test
    @Transactional
    public void getAllAdsSongsByAdsImageContainsSomething() throws Exception {
        // Initialize the database
        adsSongRepository.saveAndFlush(adsSong);

        // Get all the adsSongList where adsImage contains DEFAULT_ADS_IMAGE
        defaultAdsSongShouldBeFound("adsImage.contains=" + DEFAULT_ADS_IMAGE);

        // Get all the adsSongList where adsImage contains UPDATED_ADS_IMAGE
        defaultAdsSongShouldNotBeFound("adsImage.contains=" + UPDATED_ADS_IMAGE);
    }

    @Test
    @Transactional
    public void getAllAdsSongsByAdsImageNotContainsSomething() throws Exception {
        // Initialize the database
        adsSongRepository.saveAndFlush(adsSong);

        // Get all the adsSongList where adsImage does not contain DEFAULT_ADS_IMAGE
        defaultAdsSongShouldNotBeFound("adsImage.doesNotContain=" + DEFAULT_ADS_IMAGE);

        // Get all the adsSongList where adsImage does not contain UPDATED_ADS_IMAGE
        defaultAdsSongShouldBeFound("adsImage.doesNotContain=" + UPDATED_ADS_IMAGE);
    }


    @Test
    @Transactional
    public void getAllAdsSongsByAdsContentIsEqualToSomething() throws Exception {
        // Initialize the database
        adsSongRepository.saveAndFlush(adsSong);

        // Get all the adsSongList where adsContent equals to DEFAULT_ADS_CONTENT
        defaultAdsSongShouldBeFound("adsContent.equals=" + DEFAULT_ADS_CONTENT);

        // Get all the adsSongList where adsContent equals to UPDATED_ADS_CONTENT
        defaultAdsSongShouldNotBeFound("adsContent.equals=" + UPDATED_ADS_CONTENT);
    }

    @Test
    @Transactional
    public void getAllAdsSongsByAdsContentIsNotEqualToSomething() throws Exception {
        // Initialize the database
        adsSongRepository.saveAndFlush(adsSong);

        // Get all the adsSongList where adsContent not equals to DEFAULT_ADS_CONTENT
        defaultAdsSongShouldNotBeFound("adsContent.notEquals=" + DEFAULT_ADS_CONTENT);

        // Get all the adsSongList where adsContent not equals to UPDATED_ADS_CONTENT
        defaultAdsSongShouldBeFound("adsContent.notEquals=" + UPDATED_ADS_CONTENT);
    }

    @Test
    @Transactional
    public void getAllAdsSongsByAdsContentIsInShouldWork() throws Exception {
        // Initialize the database
        adsSongRepository.saveAndFlush(adsSong);

        // Get all the adsSongList where adsContent in DEFAULT_ADS_CONTENT or UPDATED_ADS_CONTENT
        defaultAdsSongShouldBeFound("adsContent.in=" + DEFAULT_ADS_CONTENT + "," + UPDATED_ADS_CONTENT);

        // Get all the adsSongList where adsContent equals to UPDATED_ADS_CONTENT
        defaultAdsSongShouldNotBeFound("adsContent.in=" + UPDATED_ADS_CONTENT);
    }

    @Test
    @Transactional
    public void getAllAdsSongsByAdsContentIsNullOrNotNull() throws Exception {
        // Initialize the database
        adsSongRepository.saveAndFlush(adsSong);

        // Get all the adsSongList where adsContent is not null
        defaultAdsSongShouldBeFound("adsContent.specified=true");

        // Get all the adsSongList where adsContent is null
        defaultAdsSongShouldNotBeFound("adsContent.specified=false");
    }
                @Test
    @Transactional
    public void getAllAdsSongsByAdsContentContainsSomething() throws Exception {
        // Initialize the database
        adsSongRepository.saveAndFlush(adsSong);

        // Get all the adsSongList where adsContent contains DEFAULT_ADS_CONTENT
        defaultAdsSongShouldBeFound("adsContent.contains=" + DEFAULT_ADS_CONTENT);

        // Get all the adsSongList where adsContent contains UPDATED_ADS_CONTENT
        defaultAdsSongShouldNotBeFound("adsContent.contains=" + UPDATED_ADS_CONTENT);
    }

    @Test
    @Transactional
    public void getAllAdsSongsByAdsContentNotContainsSomething() throws Exception {
        // Initialize the database
        adsSongRepository.saveAndFlush(adsSong);

        // Get all the adsSongList where adsContent does not contain DEFAULT_ADS_CONTENT
        defaultAdsSongShouldNotBeFound("adsContent.doesNotContain=" + DEFAULT_ADS_CONTENT);

        // Get all the adsSongList where adsContent does not contain UPDATED_ADS_CONTENT
        defaultAdsSongShouldBeFound("adsContent.doesNotContain=" + UPDATED_ADS_CONTENT);
    }


    @Test
    @Transactional
    public void getAllAdsSongsBySongIdIsEqualToSomething() throws Exception {
        // Initialize the database
        adsSongRepository.saveAndFlush(adsSong);

        // Get all the adsSongList where songId equals to DEFAULT_SONG_ID
        defaultAdsSongShouldBeFound("songId.equals=" + DEFAULT_SONG_ID);

        // Get all the adsSongList where songId equals to UPDATED_SONG_ID
        defaultAdsSongShouldNotBeFound("songId.equals=" + UPDATED_SONG_ID);
    }

    @Test
    @Transactional
    public void getAllAdsSongsBySongIdIsNotEqualToSomething() throws Exception {
        // Initialize the database
        adsSongRepository.saveAndFlush(adsSong);

        // Get all the adsSongList where songId not equals to DEFAULT_SONG_ID
        defaultAdsSongShouldNotBeFound("songId.notEquals=" + DEFAULT_SONG_ID);

        // Get all the adsSongList where songId not equals to UPDATED_SONG_ID
        defaultAdsSongShouldBeFound("songId.notEquals=" + UPDATED_SONG_ID);
    }

    @Test
    @Transactional
    public void getAllAdsSongsBySongIdIsInShouldWork() throws Exception {
        // Initialize the database
        adsSongRepository.saveAndFlush(adsSong);

        // Get all the adsSongList where songId in DEFAULT_SONG_ID or UPDATED_SONG_ID
        defaultAdsSongShouldBeFound("songId.in=" + DEFAULT_SONG_ID + "," + UPDATED_SONG_ID);

        // Get all the adsSongList where songId equals to UPDATED_SONG_ID
        defaultAdsSongShouldNotBeFound("songId.in=" + UPDATED_SONG_ID);
    }

    @Test
    @Transactional
    public void getAllAdsSongsBySongIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        adsSongRepository.saveAndFlush(adsSong);

        // Get all the adsSongList where songId is not null
        defaultAdsSongShouldBeFound("songId.specified=true");

        // Get all the adsSongList where songId is null
        defaultAdsSongShouldNotBeFound("songId.specified=false");
    }

    @Test
    @Transactional
    public void getAllAdsSongsBySongIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        adsSongRepository.saveAndFlush(adsSong);

        // Get all the adsSongList where songId is greater than or equal to DEFAULT_SONG_ID
        defaultAdsSongShouldBeFound("songId.greaterThanOrEqual=" + DEFAULT_SONG_ID);

        // Get all the adsSongList where songId is greater than or equal to UPDATED_SONG_ID
        defaultAdsSongShouldNotBeFound("songId.greaterThanOrEqual=" + UPDATED_SONG_ID);
    }

    @Test
    @Transactional
    public void getAllAdsSongsBySongIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        adsSongRepository.saveAndFlush(adsSong);

        // Get all the adsSongList where songId is less than or equal to DEFAULT_SONG_ID
        defaultAdsSongShouldBeFound("songId.lessThanOrEqual=" + DEFAULT_SONG_ID);

        // Get all the adsSongList where songId is less than or equal to SMALLER_SONG_ID
        defaultAdsSongShouldNotBeFound("songId.lessThanOrEqual=" + SMALLER_SONG_ID);
    }

    @Test
    @Transactional
    public void getAllAdsSongsBySongIdIsLessThanSomething() throws Exception {
        // Initialize the database
        adsSongRepository.saveAndFlush(adsSong);

        // Get all the adsSongList where songId is less than DEFAULT_SONG_ID
        defaultAdsSongShouldNotBeFound("songId.lessThan=" + DEFAULT_SONG_ID);

        // Get all the adsSongList where songId is less than UPDATED_SONG_ID
        defaultAdsSongShouldBeFound("songId.lessThan=" + UPDATED_SONG_ID);
    }

    @Test
    @Transactional
    public void getAllAdsSongsBySongIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        adsSongRepository.saveAndFlush(adsSong);

        // Get all the adsSongList where songId is greater than DEFAULT_SONG_ID
        defaultAdsSongShouldNotBeFound("songId.greaterThan=" + DEFAULT_SONG_ID);

        // Get all the adsSongList where songId is greater than SMALLER_SONG_ID
        defaultAdsSongShouldBeFound("songId.greaterThan=" + SMALLER_SONG_ID);
    }


    @Test
    @Transactional
    public void getAllAdsSongsBySongTitleIsEqualToSomething() throws Exception {
        // Initialize the database
        adsSongRepository.saveAndFlush(adsSong);

        // Get all the adsSongList where songTitle equals to DEFAULT_SONG_TITLE
        defaultAdsSongShouldBeFound("songTitle.equals=" + DEFAULT_SONG_TITLE);

        // Get all the adsSongList where songTitle equals to UPDATED_SONG_TITLE
        defaultAdsSongShouldNotBeFound("songTitle.equals=" + UPDATED_SONG_TITLE);
    }

    @Test
    @Transactional
    public void getAllAdsSongsBySongTitleIsNotEqualToSomething() throws Exception {
        // Initialize the database
        adsSongRepository.saveAndFlush(adsSong);

        // Get all the adsSongList where songTitle not equals to DEFAULT_SONG_TITLE
        defaultAdsSongShouldNotBeFound("songTitle.notEquals=" + DEFAULT_SONG_TITLE);

        // Get all the adsSongList where songTitle not equals to UPDATED_SONG_TITLE
        defaultAdsSongShouldBeFound("songTitle.notEquals=" + UPDATED_SONG_TITLE);
    }

    @Test
    @Transactional
    public void getAllAdsSongsBySongTitleIsInShouldWork() throws Exception {
        // Initialize the database
        adsSongRepository.saveAndFlush(adsSong);

        // Get all the adsSongList where songTitle in DEFAULT_SONG_TITLE or UPDATED_SONG_TITLE
        defaultAdsSongShouldBeFound("songTitle.in=" + DEFAULT_SONG_TITLE + "," + UPDATED_SONG_TITLE);

        // Get all the adsSongList where songTitle equals to UPDATED_SONG_TITLE
        defaultAdsSongShouldNotBeFound("songTitle.in=" + UPDATED_SONG_TITLE);
    }

    @Test
    @Transactional
    public void getAllAdsSongsBySongTitleIsNullOrNotNull() throws Exception {
        // Initialize the database
        adsSongRepository.saveAndFlush(adsSong);

        // Get all the adsSongList where songTitle is not null
        defaultAdsSongShouldBeFound("songTitle.specified=true");

        // Get all the adsSongList where songTitle is null
        defaultAdsSongShouldNotBeFound("songTitle.specified=false");
    }
                @Test
    @Transactional
    public void getAllAdsSongsBySongTitleContainsSomething() throws Exception {
        // Initialize the database
        adsSongRepository.saveAndFlush(adsSong);

        // Get all the adsSongList where songTitle contains DEFAULT_SONG_TITLE
        defaultAdsSongShouldBeFound("songTitle.contains=" + DEFAULT_SONG_TITLE);

        // Get all the adsSongList where songTitle contains UPDATED_SONG_TITLE
        defaultAdsSongShouldNotBeFound("songTitle.contains=" + UPDATED_SONG_TITLE);
    }

    @Test
    @Transactional
    public void getAllAdsSongsBySongTitleNotContainsSomething() throws Exception {
        // Initialize the database
        adsSongRepository.saveAndFlush(adsSong);

        // Get all the adsSongList where songTitle does not contain DEFAULT_SONG_TITLE
        defaultAdsSongShouldNotBeFound("songTitle.doesNotContain=" + DEFAULT_SONG_TITLE);

        // Get all the adsSongList where songTitle does not contain UPDATED_SONG_TITLE
        defaultAdsSongShouldBeFound("songTitle.doesNotContain=" + UPDATED_SONG_TITLE);
    }


    @Test
    @Transactional
    public void getAllAdsSongsBySongImageIsEqualToSomething() throws Exception {
        // Initialize the database
        adsSongRepository.saveAndFlush(adsSong);

        // Get all the adsSongList where songImage equals to DEFAULT_SONG_IMAGE
        defaultAdsSongShouldBeFound("songImage.equals=" + DEFAULT_SONG_IMAGE);

        // Get all the adsSongList where songImage equals to UPDATED_SONG_IMAGE
        defaultAdsSongShouldNotBeFound("songImage.equals=" + UPDATED_SONG_IMAGE);
    }

    @Test
    @Transactional
    public void getAllAdsSongsBySongImageIsNotEqualToSomething() throws Exception {
        // Initialize the database
        adsSongRepository.saveAndFlush(adsSong);

        // Get all the adsSongList where songImage not equals to DEFAULT_SONG_IMAGE
        defaultAdsSongShouldNotBeFound("songImage.notEquals=" + DEFAULT_SONG_IMAGE);

        // Get all the adsSongList where songImage not equals to UPDATED_SONG_IMAGE
        defaultAdsSongShouldBeFound("songImage.notEquals=" + UPDATED_SONG_IMAGE);
    }

    @Test
    @Transactional
    public void getAllAdsSongsBySongImageIsInShouldWork() throws Exception {
        // Initialize the database
        adsSongRepository.saveAndFlush(adsSong);

        // Get all the adsSongList where songImage in DEFAULT_SONG_IMAGE or UPDATED_SONG_IMAGE
        defaultAdsSongShouldBeFound("songImage.in=" + DEFAULT_SONG_IMAGE + "," + UPDATED_SONG_IMAGE);

        // Get all the adsSongList where songImage equals to UPDATED_SONG_IMAGE
        defaultAdsSongShouldNotBeFound("songImage.in=" + UPDATED_SONG_IMAGE);
    }

    @Test
    @Transactional
    public void getAllAdsSongsBySongImageIsNullOrNotNull() throws Exception {
        // Initialize the database
        adsSongRepository.saveAndFlush(adsSong);

        // Get all the adsSongList where songImage is not null
        defaultAdsSongShouldBeFound("songImage.specified=true");

        // Get all the adsSongList where songImage is null
        defaultAdsSongShouldNotBeFound("songImage.specified=false");
    }
                @Test
    @Transactional
    public void getAllAdsSongsBySongImageContainsSomething() throws Exception {
        // Initialize the database
        adsSongRepository.saveAndFlush(adsSong);

        // Get all the adsSongList where songImage contains DEFAULT_SONG_IMAGE
        defaultAdsSongShouldBeFound("songImage.contains=" + DEFAULT_SONG_IMAGE);

        // Get all the adsSongList where songImage contains UPDATED_SONG_IMAGE
        defaultAdsSongShouldNotBeFound("songImage.contains=" + UPDATED_SONG_IMAGE);
    }

    @Test
    @Transactional
    public void getAllAdsSongsBySongImageNotContainsSomething() throws Exception {
        // Initialize the database
        adsSongRepository.saveAndFlush(adsSong);

        // Get all the adsSongList where songImage does not contain DEFAULT_SONG_IMAGE
        defaultAdsSongShouldNotBeFound("songImage.doesNotContain=" + DEFAULT_SONG_IMAGE);

        // Get all the adsSongList where songImage does not contain UPDATED_SONG_IMAGE
        defaultAdsSongShouldBeFound("songImage.doesNotContain=" + UPDATED_SONG_IMAGE);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultAdsSongShouldBeFound(String filter) throws Exception {
        restAdsSongMockMvc.perform(get("/api/ads-songs?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(adsSong.getId().intValue())))
            .andExpect(jsonPath("$.[*].adsImage").value(hasItem(DEFAULT_ADS_IMAGE)))
            .andExpect(jsonPath("$.[*].adsContent").value(hasItem(DEFAULT_ADS_CONTENT)))
            .andExpect(jsonPath("$.[*].songId").value(hasItem(DEFAULT_SONG_ID)))
            .andExpect(jsonPath("$.[*].songTitle").value(hasItem(DEFAULT_SONG_TITLE)))
            .andExpect(jsonPath("$.[*].songImage").value(hasItem(DEFAULT_SONG_IMAGE)));

        // Check, that the count call also returns 1
        restAdsSongMockMvc.perform(get("/api/ads-songs/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultAdsSongShouldNotBeFound(String filter) throws Exception {
        restAdsSongMockMvc.perform(get("/api/ads-songs?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restAdsSongMockMvc.perform(get("/api/ads-songs/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingAdsSong() throws Exception {
        // Get the adsSong
        restAdsSongMockMvc.perform(get("/api/ads-songs/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAdsSong() throws Exception {
        // Initialize the database
        adsSongRepository.saveAndFlush(adsSong);

        int databaseSizeBeforeUpdate = adsSongRepository.findAll().size();

        // Update the adsSong
        AdsSong updatedAdsSong = adsSongRepository.findById(adsSong.getId()).get();
        // Disconnect from session so that the updates on updatedAdsSong are not directly saved in db
        em.detach(updatedAdsSong);
        updatedAdsSong
            .adsImage(UPDATED_ADS_IMAGE)
            .adsContent(UPDATED_ADS_CONTENT)
            .songId(UPDATED_SONG_ID)
            .songTitle(UPDATED_SONG_TITLE)
            .songImage(UPDATED_SONG_IMAGE);
        AdsSongDTO adsSongDTO = adsSongMapper.toDto(updatedAdsSong);

        restAdsSongMockMvc.perform(put("/api/ads-songs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(adsSongDTO)))
            .andExpect(status().isOk());

        // Validate the AdsSong in the database
        List<AdsSong> adsSongList = adsSongRepository.findAll();
        assertThat(adsSongList).hasSize(databaseSizeBeforeUpdate);
        AdsSong testAdsSong = adsSongList.get(adsSongList.size() - 1);
        assertThat(testAdsSong.getAdsImage()).isEqualTo(UPDATED_ADS_IMAGE);
        assertThat(testAdsSong.getAdsContent()).isEqualTo(UPDATED_ADS_CONTENT);
        assertThat(testAdsSong.getSongId()).isEqualTo(UPDATED_SONG_ID);
        assertThat(testAdsSong.getSongTitle()).isEqualTo(UPDATED_SONG_TITLE);
        assertThat(testAdsSong.getSongImage()).isEqualTo(UPDATED_SONG_IMAGE);

        // Validate the AdsSong in Elasticsearch
        verify(mockAdsSongSearchRepository, times(1)).save(testAdsSong);
    }

    @Test
    @Transactional
    public void updateNonExistingAdsSong() throws Exception {
        int databaseSizeBeforeUpdate = adsSongRepository.findAll().size();

        // Create the AdsSong
        AdsSongDTO adsSongDTO = adsSongMapper.toDto(adsSong);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAdsSongMockMvc.perform(put("/api/ads-songs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(adsSongDTO)))
            .andExpect(status().isBadRequest());

        // Validate the AdsSong in the database
        List<AdsSong> adsSongList = adsSongRepository.findAll();
        assertThat(adsSongList).hasSize(databaseSizeBeforeUpdate);

        // Validate the AdsSong in Elasticsearch
        verify(mockAdsSongSearchRepository, times(0)).save(adsSong);
    }

    @Test
    @Transactional
    public void deleteAdsSong() throws Exception {
        // Initialize the database
        adsSongRepository.saveAndFlush(adsSong);

        int databaseSizeBeforeDelete = adsSongRepository.findAll().size();

        // Delete the adsSong
        restAdsSongMockMvc.perform(delete("/api/ads-songs/{id}", adsSong.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<AdsSong> adsSongList = adsSongRepository.findAll();
        assertThat(adsSongList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the AdsSong in Elasticsearch
        verify(mockAdsSongSearchRepository, times(1)).deleteById(adsSong.getId());
    }

    @Test
    @Transactional
    public void searchAdsSong() throws Exception {
        // Initialize the database
        adsSongRepository.saveAndFlush(adsSong);
        when(mockAdsSongSearchRepository.search(queryStringQuery("id:" + adsSong.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(adsSong), PageRequest.of(0, 1), 1));
        // Search the adsSong
        restAdsSongMockMvc.perform(get("/api/_search/ads-songs?query=id:" + adsSong.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(adsSong.getId().intValue())))
            .andExpect(jsonPath("$.[*].adsImage").value(hasItem(DEFAULT_ADS_IMAGE)))
            .andExpect(jsonPath("$.[*].adsContent").value(hasItem(DEFAULT_ADS_CONTENT)))
            .andExpect(jsonPath("$.[*].songId").value(hasItem(DEFAULT_SONG_ID)))
            .andExpect(jsonPath("$.[*].songTitle").value(hasItem(DEFAULT_SONG_TITLE)))
            .andExpect(jsonPath("$.[*].songImage").value(hasItem(DEFAULT_SONG_IMAGE)));
    }
}
