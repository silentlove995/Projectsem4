package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.ServerApp;
import com.mycompany.myapp.domain.Songs;
import com.mycompany.myapp.domain.AdsSong;
import com.mycompany.myapp.domain.Playlist;
import com.mycompany.myapp.domain.Album;
import com.mycompany.myapp.domain.Favorite;
import com.mycompany.myapp.repository.SongsRepository;
import com.mycompany.myapp.repository.search.SongsSearchRepository;
import com.mycompany.myapp.service.SongsService;
import com.mycompany.myapp.service.dto.SongsDTO;
import com.mycompany.myapp.service.mapper.SongsMapper;
import com.mycompany.myapp.web.rest.errors.ExceptionTranslator;
import com.mycompany.myapp.service.dto.SongsCriteria;
import com.mycompany.myapp.service.SongsQueryService;

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
import org.springframework.util.Base64Utils;
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

import com.mycompany.myapp.domain.enumeration.Genre;
import com.mycompany.myapp.domain.enumeration.Country;
/**
 * Integration tests for the {@link SongsResource} REST controller.
 */
@SpringBootTest(classes = ServerApp.class)
public class SongsResourceIT {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final Genre DEFAULT_GENRE = Genre.Drama;
    private static final Genre UPDATED_GENRE = Genre.Vibrant;

    private static final String DEFAULT_VOCAL = "AAAAAAAAAA";
    private static final String UPDATED_VOCAL = "BBBBBBBBBB";

    private static final Country DEFAULT_COUNTRY = Country.China;
    private static final Country UPDATED_COUNTRY = Country.Japan;

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_SONG_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_SONG_ADDRESS = "BBBBBBBBBB";

    private static final String DEFAULT_LYRIC = "AAAAAAAAAA";
    private static final String UPDATED_LYRIC = "BBBBBBBBBB";

    private static final String DEFAULT_AVATAR = "AAAAAAAAAA";
    private static final String UPDATED_AVATAR = "BBBBBBBBBB";

    private static final Integer DEFAULT_LISTEN_COUNT = 1;
    private static final Integer UPDATED_LISTEN_COUNT = 2;
    private static final Integer SMALLER_LISTEN_COUNT = 1 - 1;

    private static final Integer DEFAULT_FAVORITE_COUNT = 1;
    private static final Integer UPDATED_FAVORITE_COUNT = 2;
    private static final Integer SMALLER_FAVORITE_COUNT = 1 - 1;

    @Autowired
    private SongsRepository songsRepository;

    @Autowired
    private SongsMapper songsMapper;

    @Autowired
    private SongsService songsService;

    /**
     * This repository is mocked in the com.mycompany.myapp.repository.search test package.
     *
     * @see com.mycompany.myapp.repository.search.SongsSearchRepositoryMockConfiguration
     */
    @Autowired
    private SongsSearchRepository mockSongsSearchRepository;

    @Autowired
    private SongsQueryService songsQueryService;

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

    private MockMvc restSongsMockMvc;

    private Songs songs;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final SongsResource songsResource = new SongsResource(songsService, songsQueryService);
        this.restSongsMockMvc = MockMvcBuilders.standaloneSetup(songsResource)
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
    public static Songs createEntity(EntityManager em) {
        Songs songs = new Songs()
            .title(DEFAULT_TITLE)
            .genre(DEFAULT_GENRE)
            .vocal(DEFAULT_VOCAL)
            .country(DEFAULT_COUNTRY)
            .description(DEFAULT_DESCRIPTION)
            .songAddress(DEFAULT_SONG_ADDRESS)
            .lyric(DEFAULT_LYRIC)
            .avatar(DEFAULT_AVATAR)
            .listenCount(DEFAULT_LISTEN_COUNT)
            .favoriteCount(DEFAULT_FAVORITE_COUNT);
        return songs;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Songs createUpdatedEntity(EntityManager em) {
        Songs songs = new Songs()
            .title(UPDATED_TITLE)
            .genre(UPDATED_GENRE)
            .vocal(UPDATED_VOCAL)
            .country(UPDATED_COUNTRY)
            .description(UPDATED_DESCRIPTION)
            .songAddress(UPDATED_SONG_ADDRESS)
            .lyric(UPDATED_LYRIC)
            .avatar(UPDATED_AVATAR)
            .listenCount(UPDATED_LISTEN_COUNT)
            .favoriteCount(UPDATED_FAVORITE_COUNT);
        return songs;
    }

    @BeforeEach
    public void initTest() {
        songs = createEntity(em);
    }

    @Test
    @Transactional
    public void createSongs() throws Exception {
        int databaseSizeBeforeCreate = songsRepository.findAll().size();

        // Create the Songs
        SongsDTO songsDTO = songsMapper.toDto(songs);
        restSongsMockMvc.perform(post("/api/songs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(songsDTO)))
            .andExpect(status().isCreated());

        // Validate the Songs in the database
        List<Songs> songsList = songsRepository.findAll();
        assertThat(songsList).hasSize(databaseSizeBeforeCreate + 1);
        Songs testSongs = songsList.get(songsList.size() - 1);
        assertThat(testSongs.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testSongs.getGenre()).isEqualTo(DEFAULT_GENRE);
        assertThat(testSongs.getVocal()).isEqualTo(DEFAULT_VOCAL);
        assertThat(testSongs.getCountry()).isEqualTo(DEFAULT_COUNTRY);
        assertThat(testSongs.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testSongs.getSongAddress()).isEqualTo(DEFAULT_SONG_ADDRESS);
        assertThat(testSongs.getLyric()).isEqualTo(DEFAULT_LYRIC);
        assertThat(testSongs.getAvatar()).isEqualTo(DEFAULT_AVATAR);
        assertThat(testSongs.getListenCount()).isEqualTo(DEFAULT_LISTEN_COUNT);
        assertThat(testSongs.getFavoriteCount()).isEqualTo(DEFAULT_FAVORITE_COUNT);

        // Validate the Songs in Elasticsearch
        verify(mockSongsSearchRepository, times(1)).save(testSongs);
    }

    @Test
    @Transactional
    public void createSongsWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = songsRepository.findAll().size();

        // Create the Songs with an existing ID
        songs.setId(1L);
        SongsDTO songsDTO = songsMapper.toDto(songs);

        // An entity with an existing ID cannot be created, so this API call must fail
        restSongsMockMvc.perform(post("/api/songs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(songsDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Songs in the database
        List<Songs> songsList = songsRepository.findAll();
        assertThat(songsList).hasSize(databaseSizeBeforeCreate);

        // Validate the Songs in Elasticsearch
        verify(mockSongsSearchRepository, times(0)).save(songs);
    }


    @Test
    @Transactional
    public void getAllSongs() throws Exception {
        // Initialize the database
        songsRepository.saveAndFlush(songs);

        // Get all the songsList
        restSongsMockMvc.perform(get("/api/songs?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(songs.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].genre").value(hasItem(DEFAULT_GENRE.toString())))
            .andExpect(jsonPath("$.[*].vocal").value(hasItem(DEFAULT_VOCAL)))
            .andExpect(jsonPath("$.[*].country").value(hasItem(DEFAULT_COUNTRY.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].songAddress").value(hasItem(DEFAULT_SONG_ADDRESS)))
            .andExpect(jsonPath("$.[*].lyric").value(hasItem(DEFAULT_LYRIC.toString())))
            .andExpect(jsonPath("$.[*].avatar").value(hasItem(DEFAULT_AVATAR)))
            .andExpect(jsonPath("$.[*].listenCount").value(hasItem(DEFAULT_LISTEN_COUNT)))
            .andExpect(jsonPath("$.[*].favoriteCount").value(hasItem(DEFAULT_FAVORITE_COUNT)));
    }
    
    @Test
    @Transactional
    public void getSongs() throws Exception {
        // Initialize the database
        songsRepository.saveAndFlush(songs);

        // Get the songs
        restSongsMockMvc.perform(get("/api/songs/{id}", songs.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(songs.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.genre").value(DEFAULT_GENRE.toString()))
            .andExpect(jsonPath("$.vocal").value(DEFAULT_VOCAL))
            .andExpect(jsonPath("$.country").value(DEFAULT_COUNTRY.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.songAddress").value(DEFAULT_SONG_ADDRESS))
            .andExpect(jsonPath("$.lyric").value(DEFAULT_LYRIC.toString()))
            .andExpect(jsonPath("$.avatar").value(DEFAULT_AVATAR))
            .andExpect(jsonPath("$.listenCount").value(DEFAULT_LISTEN_COUNT))
            .andExpect(jsonPath("$.favoriteCount").value(DEFAULT_FAVORITE_COUNT));
    }


    @Test
    @Transactional
    public void getSongsByIdFiltering() throws Exception {
        // Initialize the database
        songsRepository.saveAndFlush(songs);

        Long id = songs.getId();

        defaultSongsShouldBeFound("id.equals=" + id);
        defaultSongsShouldNotBeFound("id.notEquals=" + id);

        defaultSongsShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultSongsShouldNotBeFound("id.greaterThan=" + id);

        defaultSongsShouldBeFound("id.lessThanOrEqual=" + id);
        defaultSongsShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllSongsByTitleIsEqualToSomething() throws Exception {
        // Initialize the database
        songsRepository.saveAndFlush(songs);

        // Get all the songsList where title equals to DEFAULT_TITLE
        defaultSongsShouldBeFound("title.equals=" + DEFAULT_TITLE);

        // Get all the songsList where title equals to UPDATED_TITLE
        defaultSongsShouldNotBeFound("title.equals=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    public void getAllSongsByTitleIsNotEqualToSomething() throws Exception {
        // Initialize the database
        songsRepository.saveAndFlush(songs);

        // Get all the songsList where title not equals to DEFAULT_TITLE
        defaultSongsShouldNotBeFound("title.notEquals=" + DEFAULT_TITLE);

        // Get all the songsList where title not equals to UPDATED_TITLE
        defaultSongsShouldBeFound("title.notEquals=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    public void getAllSongsByTitleIsInShouldWork() throws Exception {
        // Initialize the database
        songsRepository.saveAndFlush(songs);

        // Get all the songsList where title in DEFAULT_TITLE or UPDATED_TITLE
        defaultSongsShouldBeFound("title.in=" + DEFAULT_TITLE + "," + UPDATED_TITLE);

        // Get all the songsList where title equals to UPDATED_TITLE
        defaultSongsShouldNotBeFound("title.in=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    public void getAllSongsByTitleIsNullOrNotNull() throws Exception {
        // Initialize the database
        songsRepository.saveAndFlush(songs);

        // Get all the songsList where title is not null
        defaultSongsShouldBeFound("title.specified=true");

        // Get all the songsList where title is null
        defaultSongsShouldNotBeFound("title.specified=false");
    }
                @Test
    @Transactional
    public void getAllSongsByTitleContainsSomething() throws Exception {
        // Initialize the database
        songsRepository.saveAndFlush(songs);

        // Get all the songsList where title contains DEFAULT_TITLE
        defaultSongsShouldBeFound("title.contains=" + DEFAULT_TITLE);

        // Get all the songsList where title contains UPDATED_TITLE
        defaultSongsShouldNotBeFound("title.contains=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    public void getAllSongsByTitleNotContainsSomething() throws Exception {
        // Initialize the database
        songsRepository.saveAndFlush(songs);

        // Get all the songsList where title does not contain DEFAULT_TITLE
        defaultSongsShouldNotBeFound("title.doesNotContain=" + DEFAULT_TITLE);

        // Get all the songsList where title does not contain UPDATED_TITLE
        defaultSongsShouldBeFound("title.doesNotContain=" + UPDATED_TITLE);
    }


    @Test
    @Transactional
    public void getAllSongsByGenreIsEqualToSomething() throws Exception {
        // Initialize the database
        songsRepository.saveAndFlush(songs);

        // Get all the songsList where genre equals to DEFAULT_GENRE
        defaultSongsShouldBeFound("genre.equals=" + DEFAULT_GENRE);

        // Get all the songsList where genre equals to UPDATED_GENRE
        defaultSongsShouldNotBeFound("genre.equals=" + UPDATED_GENRE);
    }

    @Test
    @Transactional
    public void getAllSongsByGenreIsNotEqualToSomething() throws Exception {
        // Initialize the database
        songsRepository.saveAndFlush(songs);

        // Get all the songsList where genre not equals to DEFAULT_GENRE
        defaultSongsShouldNotBeFound("genre.notEquals=" + DEFAULT_GENRE);

        // Get all the songsList where genre not equals to UPDATED_GENRE
        defaultSongsShouldBeFound("genre.notEquals=" + UPDATED_GENRE);
    }

    @Test
    @Transactional
    public void getAllSongsByGenreIsInShouldWork() throws Exception {
        // Initialize the database
        songsRepository.saveAndFlush(songs);

        // Get all the songsList where genre in DEFAULT_GENRE or UPDATED_GENRE
        defaultSongsShouldBeFound("genre.in=" + DEFAULT_GENRE + "," + UPDATED_GENRE);

        // Get all the songsList where genre equals to UPDATED_GENRE
        defaultSongsShouldNotBeFound("genre.in=" + UPDATED_GENRE);
    }

    @Test
    @Transactional
    public void getAllSongsByGenreIsNullOrNotNull() throws Exception {
        // Initialize the database
        songsRepository.saveAndFlush(songs);

        // Get all the songsList where genre is not null
        defaultSongsShouldBeFound("genre.specified=true");

        // Get all the songsList where genre is null
        defaultSongsShouldNotBeFound("genre.specified=false");
    }

    @Test
    @Transactional
    public void getAllSongsByVocalIsEqualToSomething() throws Exception {
        // Initialize the database
        songsRepository.saveAndFlush(songs);

        // Get all the songsList where vocal equals to DEFAULT_VOCAL
        defaultSongsShouldBeFound("vocal.equals=" + DEFAULT_VOCAL);

        // Get all the songsList where vocal equals to UPDATED_VOCAL
        defaultSongsShouldNotBeFound("vocal.equals=" + UPDATED_VOCAL);
    }

    @Test
    @Transactional
    public void getAllSongsByVocalIsNotEqualToSomething() throws Exception {
        // Initialize the database
        songsRepository.saveAndFlush(songs);

        // Get all the songsList where vocal not equals to DEFAULT_VOCAL
        defaultSongsShouldNotBeFound("vocal.notEquals=" + DEFAULT_VOCAL);

        // Get all the songsList where vocal not equals to UPDATED_VOCAL
        defaultSongsShouldBeFound("vocal.notEquals=" + UPDATED_VOCAL);
    }

    @Test
    @Transactional
    public void getAllSongsByVocalIsInShouldWork() throws Exception {
        // Initialize the database
        songsRepository.saveAndFlush(songs);

        // Get all the songsList where vocal in DEFAULT_VOCAL or UPDATED_VOCAL
        defaultSongsShouldBeFound("vocal.in=" + DEFAULT_VOCAL + "," + UPDATED_VOCAL);

        // Get all the songsList where vocal equals to UPDATED_VOCAL
        defaultSongsShouldNotBeFound("vocal.in=" + UPDATED_VOCAL);
    }

    @Test
    @Transactional
    public void getAllSongsByVocalIsNullOrNotNull() throws Exception {
        // Initialize the database
        songsRepository.saveAndFlush(songs);

        // Get all the songsList where vocal is not null
        defaultSongsShouldBeFound("vocal.specified=true");

        // Get all the songsList where vocal is null
        defaultSongsShouldNotBeFound("vocal.specified=false");
    }
                @Test
    @Transactional
    public void getAllSongsByVocalContainsSomething() throws Exception {
        // Initialize the database
        songsRepository.saveAndFlush(songs);

        // Get all the songsList where vocal contains DEFAULT_VOCAL
        defaultSongsShouldBeFound("vocal.contains=" + DEFAULT_VOCAL);

        // Get all the songsList where vocal contains UPDATED_VOCAL
        defaultSongsShouldNotBeFound("vocal.contains=" + UPDATED_VOCAL);
    }

    @Test
    @Transactional
    public void getAllSongsByVocalNotContainsSomething() throws Exception {
        // Initialize the database
        songsRepository.saveAndFlush(songs);

        // Get all the songsList where vocal does not contain DEFAULT_VOCAL
        defaultSongsShouldNotBeFound("vocal.doesNotContain=" + DEFAULT_VOCAL);

        // Get all the songsList where vocal does not contain UPDATED_VOCAL
        defaultSongsShouldBeFound("vocal.doesNotContain=" + UPDATED_VOCAL);
    }


    @Test
    @Transactional
    public void getAllSongsByCountryIsEqualToSomething() throws Exception {
        // Initialize the database
        songsRepository.saveAndFlush(songs);

        // Get all the songsList where country equals to DEFAULT_COUNTRY
        defaultSongsShouldBeFound("country.equals=" + DEFAULT_COUNTRY);

        // Get all the songsList where country equals to UPDATED_COUNTRY
        defaultSongsShouldNotBeFound("country.equals=" + UPDATED_COUNTRY);
    }

    @Test
    @Transactional
    public void getAllSongsByCountryIsNotEqualToSomething() throws Exception {
        // Initialize the database
        songsRepository.saveAndFlush(songs);

        // Get all the songsList where country not equals to DEFAULT_COUNTRY
        defaultSongsShouldNotBeFound("country.notEquals=" + DEFAULT_COUNTRY);

        // Get all the songsList where country not equals to UPDATED_COUNTRY
        defaultSongsShouldBeFound("country.notEquals=" + UPDATED_COUNTRY);
    }

    @Test
    @Transactional
    public void getAllSongsByCountryIsInShouldWork() throws Exception {
        // Initialize the database
        songsRepository.saveAndFlush(songs);

        // Get all the songsList where country in DEFAULT_COUNTRY or UPDATED_COUNTRY
        defaultSongsShouldBeFound("country.in=" + DEFAULT_COUNTRY + "," + UPDATED_COUNTRY);

        // Get all the songsList where country equals to UPDATED_COUNTRY
        defaultSongsShouldNotBeFound("country.in=" + UPDATED_COUNTRY);
    }

    @Test
    @Transactional
    public void getAllSongsByCountryIsNullOrNotNull() throws Exception {
        // Initialize the database
        songsRepository.saveAndFlush(songs);

        // Get all the songsList where country is not null
        defaultSongsShouldBeFound("country.specified=true");

        // Get all the songsList where country is null
        defaultSongsShouldNotBeFound("country.specified=false");
    }

    @Test
    @Transactional
    public void getAllSongsByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        songsRepository.saveAndFlush(songs);

        // Get all the songsList where description equals to DEFAULT_DESCRIPTION
        defaultSongsShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the songsList where description equals to UPDATED_DESCRIPTION
        defaultSongsShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllSongsByDescriptionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        songsRepository.saveAndFlush(songs);

        // Get all the songsList where description not equals to DEFAULT_DESCRIPTION
        defaultSongsShouldNotBeFound("description.notEquals=" + DEFAULT_DESCRIPTION);

        // Get all the songsList where description not equals to UPDATED_DESCRIPTION
        defaultSongsShouldBeFound("description.notEquals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllSongsByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        songsRepository.saveAndFlush(songs);

        // Get all the songsList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultSongsShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the songsList where description equals to UPDATED_DESCRIPTION
        defaultSongsShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllSongsByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        songsRepository.saveAndFlush(songs);

        // Get all the songsList where description is not null
        defaultSongsShouldBeFound("description.specified=true");

        // Get all the songsList where description is null
        defaultSongsShouldNotBeFound("description.specified=false");
    }
                @Test
    @Transactional
    public void getAllSongsByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        songsRepository.saveAndFlush(songs);

        // Get all the songsList where description contains DEFAULT_DESCRIPTION
        defaultSongsShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the songsList where description contains UPDATED_DESCRIPTION
        defaultSongsShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllSongsByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        songsRepository.saveAndFlush(songs);

        // Get all the songsList where description does not contain DEFAULT_DESCRIPTION
        defaultSongsShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the songsList where description does not contain UPDATED_DESCRIPTION
        defaultSongsShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }


    @Test
    @Transactional
    public void getAllSongsBySongAddressIsEqualToSomething() throws Exception {
        // Initialize the database
        songsRepository.saveAndFlush(songs);

        // Get all the songsList where songAddress equals to DEFAULT_SONG_ADDRESS
        defaultSongsShouldBeFound("songAddress.equals=" + DEFAULT_SONG_ADDRESS);

        // Get all the songsList where songAddress equals to UPDATED_SONG_ADDRESS
        defaultSongsShouldNotBeFound("songAddress.equals=" + UPDATED_SONG_ADDRESS);
    }

    @Test
    @Transactional
    public void getAllSongsBySongAddressIsNotEqualToSomething() throws Exception {
        // Initialize the database
        songsRepository.saveAndFlush(songs);

        // Get all the songsList where songAddress not equals to DEFAULT_SONG_ADDRESS
        defaultSongsShouldNotBeFound("songAddress.notEquals=" + DEFAULT_SONG_ADDRESS);

        // Get all the songsList where songAddress not equals to UPDATED_SONG_ADDRESS
        defaultSongsShouldBeFound("songAddress.notEquals=" + UPDATED_SONG_ADDRESS);
    }

    @Test
    @Transactional
    public void getAllSongsBySongAddressIsInShouldWork() throws Exception {
        // Initialize the database
        songsRepository.saveAndFlush(songs);

        // Get all the songsList where songAddress in DEFAULT_SONG_ADDRESS or UPDATED_SONG_ADDRESS
        defaultSongsShouldBeFound("songAddress.in=" + DEFAULT_SONG_ADDRESS + "," + UPDATED_SONG_ADDRESS);

        // Get all the songsList where songAddress equals to UPDATED_SONG_ADDRESS
        defaultSongsShouldNotBeFound("songAddress.in=" + UPDATED_SONG_ADDRESS);
    }

    @Test
    @Transactional
    public void getAllSongsBySongAddressIsNullOrNotNull() throws Exception {
        // Initialize the database
        songsRepository.saveAndFlush(songs);

        // Get all the songsList where songAddress is not null
        defaultSongsShouldBeFound("songAddress.specified=true");

        // Get all the songsList where songAddress is null
        defaultSongsShouldNotBeFound("songAddress.specified=false");
    }
                @Test
    @Transactional
    public void getAllSongsBySongAddressContainsSomething() throws Exception {
        // Initialize the database
        songsRepository.saveAndFlush(songs);

        // Get all the songsList where songAddress contains DEFAULT_SONG_ADDRESS
        defaultSongsShouldBeFound("songAddress.contains=" + DEFAULT_SONG_ADDRESS);

        // Get all the songsList where songAddress contains UPDATED_SONG_ADDRESS
        defaultSongsShouldNotBeFound("songAddress.contains=" + UPDATED_SONG_ADDRESS);
    }

    @Test
    @Transactional
    public void getAllSongsBySongAddressNotContainsSomething() throws Exception {
        // Initialize the database
        songsRepository.saveAndFlush(songs);

        // Get all the songsList where songAddress does not contain DEFAULT_SONG_ADDRESS
        defaultSongsShouldNotBeFound("songAddress.doesNotContain=" + DEFAULT_SONG_ADDRESS);

        // Get all the songsList where songAddress does not contain UPDATED_SONG_ADDRESS
        defaultSongsShouldBeFound("songAddress.doesNotContain=" + UPDATED_SONG_ADDRESS);
    }


    @Test
    @Transactional
    public void getAllSongsByAvatarIsEqualToSomething() throws Exception {
        // Initialize the database
        songsRepository.saveAndFlush(songs);

        // Get all the songsList where avatar equals to DEFAULT_AVATAR
        defaultSongsShouldBeFound("avatar.equals=" + DEFAULT_AVATAR);

        // Get all the songsList where avatar equals to UPDATED_AVATAR
        defaultSongsShouldNotBeFound("avatar.equals=" + UPDATED_AVATAR);
    }

    @Test
    @Transactional
    public void getAllSongsByAvatarIsNotEqualToSomething() throws Exception {
        // Initialize the database
        songsRepository.saveAndFlush(songs);

        // Get all the songsList where avatar not equals to DEFAULT_AVATAR
        defaultSongsShouldNotBeFound("avatar.notEquals=" + DEFAULT_AVATAR);

        // Get all the songsList where avatar not equals to UPDATED_AVATAR
        defaultSongsShouldBeFound("avatar.notEquals=" + UPDATED_AVATAR);
    }

    @Test
    @Transactional
    public void getAllSongsByAvatarIsInShouldWork() throws Exception {
        // Initialize the database
        songsRepository.saveAndFlush(songs);

        // Get all the songsList where avatar in DEFAULT_AVATAR or UPDATED_AVATAR
        defaultSongsShouldBeFound("avatar.in=" + DEFAULT_AVATAR + "," + UPDATED_AVATAR);

        // Get all the songsList where avatar equals to UPDATED_AVATAR
        defaultSongsShouldNotBeFound("avatar.in=" + UPDATED_AVATAR);
    }

    @Test
    @Transactional
    public void getAllSongsByAvatarIsNullOrNotNull() throws Exception {
        // Initialize the database
        songsRepository.saveAndFlush(songs);

        // Get all the songsList where avatar is not null
        defaultSongsShouldBeFound("avatar.specified=true");

        // Get all the songsList where avatar is null
        defaultSongsShouldNotBeFound("avatar.specified=false");
    }
                @Test
    @Transactional
    public void getAllSongsByAvatarContainsSomething() throws Exception {
        // Initialize the database
        songsRepository.saveAndFlush(songs);

        // Get all the songsList where avatar contains DEFAULT_AVATAR
        defaultSongsShouldBeFound("avatar.contains=" + DEFAULT_AVATAR);

        // Get all the songsList where avatar contains UPDATED_AVATAR
        defaultSongsShouldNotBeFound("avatar.contains=" + UPDATED_AVATAR);
    }

    @Test
    @Transactional
    public void getAllSongsByAvatarNotContainsSomething() throws Exception {
        // Initialize the database
        songsRepository.saveAndFlush(songs);

        // Get all the songsList where avatar does not contain DEFAULT_AVATAR
        defaultSongsShouldNotBeFound("avatar.doesNotContain=" + DEFAULT_AVATAR);

        // Get all the songsList where avatar does not contain UPDATED_AVATAR
        defaultSongsShouldBeFound("avatar.doesNotContain=" + UPDATED_AVATAR);
    }


    @Test
    @Transactional
    public void getAllSongsByListenCountIsEqualToSomething() throws Exception {
        // Initialize the database
        songsRepository.saveAndFlush(songs);

        // Get all the songsList where listenCount equals to DEFAULT_LISTEN_COUNT
        defaultSongsShouldBeFound("listenCount.equals=" + DEFAULT_LISTEN_COUNT);

        // Get all the songsList where listenCount equals to UPDATED_LISTEN_COUNT
        defaultSongsShouldNotBeFound("listenCount.equals=" + UPDATED_LISTEN_COUNT);
    }

    @Test
    @Transactional
    public void getAllSongsByListenCountIsNotEqualToSomething() throws Exception {
        // Initialize the database
        songsRepository.saveAndFlush(songs);

        // Get all the songsList where listenCount not equals to DEFAULT_LISTEN_COUNT
        defaultSongsShouldNotBeFound("listenCount.notEquals=" + DEFAULT_LISTEN_COUNT);

        // Get all the songsList where listenCount not equals to UPDATED_LISTEN_COUNT
        defaultSongsShouldBeFound("listenCount.notEquals=" + UPDATED_LISTEN_COUNT);
    }

    @Test
    @Transactional
    public void getAllSongsByListenCountIsInShouldWork() throws Exception {
        // Initialize the database
        songsRepository.saveAndFlush(songs);

        // Get all the songsList where listenCount in DEFAULT_LISTEN_COUNT or UPDATED_LISTEN_COUNT
        defaultSongsShouldBeFound("listenCount.in=" + DEFAULT_LISTEN_COUNT + "," + UPDATED_LISTEN_COUNT);

        // Get all the songsList where listenCount equals to UPDATED_LISTEN_COUNT
        defaultSongsShouldNotBeFound("listenCount.in=" + UPDATED_LISTEN_COUNT);
    }

    @Test
    @Transactional
    public void getAllSongsByListenCountIsNullOrNotNull() throws Exception {
        // Initialize the database
        songsRepository.saveAndFlush(songs);

        // Get all the songsList where listenCount is not null
        defaultSongsShouldBeFound("listenCount.specified=true");

        // Get all the songsList where listenCount is null
        defaultSongsShouldNotBeFound("listenCount.specified=false");
    }

    @Test
    @Transactional
    public void getAllSongsByListenCountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        songsRepository.saveAndFlush(songs);

        // Get all the songsList where listenCount is greater than or equal to DEFAULT_LISTEN_COUNT
        defaultSongsShouldBeFound("listenCount.greaterThanOrEqual=" + DEFAULT_LISTEN_COUNT);

        // Get all the songsList where listenCount is greater than or equal to UPDATED_LISTEN_COUNT
        defaultSongsShouldNotBeFound("listenCount.greaterThanOrEqual=" + UPDATED_LISTEN_COUNT);
    }

    @Test
    @Transactional
    public void getAllSongsByListenCountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        songsRepository.saveAndFlush(songs);

        // Get all the songsList where listenCount is less than or equal to DEFAULT_LISTEN_COUNT
        defaultSongsShouldBeFound("listenCount.lessThanOrEqual=" + DEFAULT_LISTEN_COUNT);

        // Get all the songsList where listenCount is less than or equal to SMALLER_LISTEN_COUNT
        defaultSongsShouldNotBeFound("listenCount.lessThanOrEqual=" + SMALLER_LISTEN_COUNT);
    }

    @Test
    @Transactional
    public void getAllSongsByListenCountIsLessThanSomething() throws Exception {
        // Initialize the database
        songsRepository.saveAndFlush(songs);

        // Get all the songsList where listenCount is less than DEFAULT_LISTEN_COUNT
        defaultSongsShouldNotBeFound("listenCount.lessThan=" + DEFAULT_LISTEN_COUNT);

        // Get all the songsList where listenCount is less than UPDATED_LISTEN_COUNT
        defaultSongsShouldBeFound("listenCount.lessThan=" + UPDATED_LISTEN_COUNT);
    }

    @Test
    @Transactional
    public void getAllSongsByListenCountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        songsRepository.saveAndFlush(songs);

        // Get all the songsList where listenCount is greater than DEFAULT_LISTEN_COUNT
        defaultSongsShouldNotBeFound("listenCount.greaterThan=" + DEFAULT_LISTEN_COUNT);

        // Get all the songsList where listenCount is greater than SMALLER_LISTEN_COUNT
        defaultSongsShouldBeFound("listenCount.greaterThan=" + SMALLER_LISTEN_COUNT);
    }


    @Test
    @Transactional
    public void getAllSongsByFavoriteCountIsEqualToSomething() throws Exception {
        // Initialize the database
        songsRepository.saveAndFlush(songs);

        // Get all the songsList where favoriteCount equals to DEFAULT_FAVORITE_COUNT
        defaultSongsShouldBeFound("favoriteCount.equals=" + DEFAULT_FAVORITE_COUNT);

        // Get all the songsList where favoriteCount equals to UPDATED_FAVORITE_COUNT
        defaultSongsShouldNotBeFound("favoriteCount.equals=" + UPDATED_FAVORITE_COUNT);
    }

    @Test
    @Transactional
    public void getAllSongsByFavoriteCountIsNotEqualToSomething() throws Exception {
        // Initialize the database
        songsRepository.saveAndFlush(songs);

        // Get all the songsList where favoriteCount not equals to DEFAULT_FAVORITE_COUNT
        defaultSongsShouldNotBeFound("favoriteCount.notEquals=" + DEFAULT_FAVORITE_COUNT);

        // Get all the songsList where favoriteCount not equals to UPDATED_FAVORITE_COUNT
        defaultSongsShouldBeFound("favoriteCount.notEquals=" + UPDATED_FAVORITE_COUNT);
    }

    @Test
    @Transactional
    public void getAllSongsByFavoriteCountIsInShouldWork() throws Exception {
        // Initialize the database
        songsRepository.saveAndFlush(songs);

        // Get all the songsList where favoriteCount in DEFAULT_FAVORITE_COUNT or UPDATED_FAVORITE_COUNT
        defaultSongsShouldBeFound("favoriteCount.in=" + DEFAULT_FAVORITE_COUNT + "," + UPDATED_FAVORITE_COUNT);

        // Get all the songsList where favoriteCount equals to UPDATED_FAVORITE_COUNT
        defaultSongsShouldNotBeFound("favoriteCount.in=" + UPDATED_FAVORITE_COUNT);
    }

    @Test
    @Transactional
    public void getAllSongsByFavoriteCountIsNullOrNotNull() throws Exception {
        // Initialize the database
        songsRepository.saveAndFlush(songs);

        // Get all the songsList where favoriteCount is not null
        defaultSongsShouldBeFound("favoriteCount.specified=true");

        // Get all the songsList where favoriteCount is null
        defaultSongsShouldNotBeFound("favoriteCount.specified=false");
    }

    @Test
    @Transactional
    public void getAllSongsByFavoriteCountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        songsRepository.saveAndFlush(songs);

        // Get all the songsList where favoriteCount is greater than or equal to DEFAULT_FAVORITE_COUNT
        defaultSongsShouldBeFound("favoriteCount.greaterThanOrEqual=" + DEFAULT_FAVORITE_COUNT);

        // Get all the songsList where favoriteCount is greater than or equal to UPDATED_FAVORITE_COUNT
        defaultSongsShouldNotBeFound("favoriteCount.greaterThanOrEqual=" + UPDATED_FAVORITE_COUNT);
    }

    @Test
    @Transactional
    public void getAllSongsByFavoriteCountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        songsRepository.saveAndFlush(songs);

        // Get all the songsList where favoriteCount is less than or equal to DEFAULT_FAVORITE_COUNT
        defaultSongsShouldBeFound("favoriteCount.lessThanOrEqual=" + DEFAULT_FAVORITE_COUNT);

        // Get all the songsList where favoriteCount is less than or equal to SMALLER_FAVORITE_COUNT
        defaultSongsShouldNotBeFound("favoriteCount.lessThanOrEqual=" + SMALLER_FAVORITE_COUNT);
    }

    @Test
    @Transactional
    public void getAllSongsByFavoriteCountIsLessThanSomething() throws Exception {
        // Initialize the database
        songsRepository.saveAndFlush(songs);

        // Get all the songsList where favoriteCount is less than DEFAULT_FAVORITE_COUNT
        defaultSongsShouldNotBeFound("favoriteCount.lessThan=" + DEFAULT_FAVORITE_COUNT);

        // Get all the songsList where favoriteCount is less than UPDATED_FAVORITE_COUNT
        defaultSongsShouldBeFound("favoriteCount.lessThan=" + UPDATED_FAVORITE_COUNT);
    }

    @Test
    @Transactional
    public void getAllSongsByFavoriteCountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        songsRepository.saveAndFlush(songs);

        // Get all the songsList where favoriteCount is greater than DEFAULT_FAVORITE_COUNT
        defaultSongsShouldNotBeFound("favoriteCount.greaterThan=" + DEFAULT_FAVORITE_COUNT);

        // Get all the songsList where favoriteCount is greater than SMALLER_FAVORITE_COUNT
        defaultSongsShouldBeFound("favoriteCount.greaterThan=" + SMALLER_FAVORITE_COUNT);
    }


    @Test
    @Transactional
    public void getAllSongsByAdsIsEqualToSomething() throws Exception {
        // Initialize the database
        songsRepository.saveAndFlush(songs);
        AdsSong ads = AdsSongResourceIT.createEntity(em);
        em.persist(ads);
        em.flush();
        songs.setAds(ads);
        songsRepository.saveAndFlush(songs);
        Long adsId = ads.getId();

        // Get all the songsList where ads equals to adsId
        defaultSongsShouldBeFound("adsId.equals=" + adsId);

        // Get all the songsList where ads equals to adsId + 1
        defaultSongsShouldNotBeFound("adsId.equals=" + (adsId + 1));
    }


    @Test
    @Transactional
    public void getAllSongsByPlaylistIsEqualToSomething() throws Exception {
        // Initialize the database
        songsRepository.saveAndFlush(songs);
        Playlist playlist = PlaylistResourceIT.createEntity(em);
        em.persist(playlist);
        em.flush();
        songs.setPlaylist(playlist);
        songsRepository.saveAndFlush(songs);
        Long playlistId = playlist.getId();

        // Get all the songsList where playlist equals to playlistId
        defaultSongsShouldBeFound("playlistId.equals=" + playlistId);

        // Get all the songsList where playlist equals to playlistId + 1
        defaultSongsShouldNotBeFound("playlistId.equals=" + (playlistId + 1));
    }


    @Test
    @Transactional
    public void getAllSongsByAlbumIsEqualToSomething() throws Exception {
        // Initialize the database
        songsRepository.saveAndFlush(songs);
        Album album = AlbumResourceIT.createEntity(em);
        em.persist(album);
        em.flush();
        songs.setAlbum(album);
        songsRepository.saveAndFlush(songs);
        Long albumId = album.getId();

        // Get all the songsList where album equals to albumId
        defaultSongsShouldBeFound("albumId.equals=" + albumId);

        // Get all the songsList where album equals to albumId + 1
        defaultSongsShouldNotBeFound("albumId.equals=" + (albumId + 1));
    }


    @Test
    @Transactional
    public void getAllSongsByFavoriteIsEqualToSomething() throws Exception {
        // Initialize the database
        songsRepository.saveAndFlush(songs);
        Favorite favorite = FavoriteResourceIT.createEntity(em);
        em.persist(favorite);
        em.flush();
        songs.setFavorite(favorite);
        songsRepository.saveAndFlush(songs);
        Long favoriteId = favorite.getId();

        // Get all the songsList where favorite equals to favoriteId
        defaultSongsShouldBeFound("favoriteId.equals=" + favoriteId);

        // Get all the songsList where favorite equals to favoriteId + 1
        defaultSongsShouldNotBeFound("favoriteId.equals=" + (favoriteId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultSongsShouldBeFound(String filter) throws Exception {
        restSongsMockMvc.perform(get("/api/songs?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(songs.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].genre").value(hasItem(DEFAULT_GENRE.toString())))
            .andExpect(jsonPath("$.[*].vocal").value(hasItem(DEFAULT_VOCAL)))
            .andExpect(jsonPath("$.[*].country").value(hasItem(DEFAULT_COUNTRY.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].songAddress").value(hasItem(DEFAULT_SONG_ADDRESS)))
            .andExpect(jsonPath("$.[*].lyric").value(hasItem(DEFAULT_LYRIC.toString())))
            .andExpect(jsonPath("$.[*].avatar").value(hasItem(DEFAULT_AVATAR)))
            .andExpect(jsonPath("$.[*].listenCount").value(hasItem(DEFAULT_LISTEN_COUNT)))
            .andExpect(jsonPath("$.[*].favoriteCount").value(hasItem(DEFAULT_FAVORITE_COUNT)));

        // Check, that the count call also returns 1
        restSongsMockMvc.perform(get("/api/songs/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultSongsShouldNotBeFound(String filter) throws Exception {
        restSongsMockMvc.perform(get("/api/songs?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restSongsMockMvc.perform(get("/api/songs/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingSongs() throws Exception {
        // Get the songs
        restSongsMockMvc.perform(get("/api/songs/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSongs() throws Exception {
        // Initialize the database
        songsRepository.saveAndFlush(songs);

        int databaseSizeBeforeUpdate = songsRepository.findAll().size();

        // Update the songs
        Songs updatedSongs = songsRepository.findById(songs.getId()).get();
        // Disconnect from session so that the updates on updatedSongs are not directly saved in db
        em.detach(updatedSongs);
        updatedSongs
            .title(UPDATED_TITLE)
            .genre(UPDATED_GENRE)
            .vocal(UPDATED_VOCAL)
            .country(UPDATED_COUNTRY)
            .description(UPDATED_DESCRIPTION)
            .songAddress(UPDATED_SONG_ADDRESS)
            .lyric(UPDATED_LYRIC)
            .avatar(UPDATED_AVATAR)
            .listenCount(UPDATED_LISTEN_COUNT)
            .favoriteCount(UPDATED_FAVORITE_COUNT);
        SongsDTO songsDTO = songsMapper.toDto(updatedSongs);

        restSongsMockMvc.perform(put("/api/songs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(songsDTO)))
            .andExpect(status().isOk());

        // Validate the Songs in the database
        List<Songs> songsList = songsRepository.findAll();
        assertThat(songsList).hasSize(databaseSizeBeforeUpdate);
        Songs testSongs = songsList.get(songsList.size() - 1);
        assertThat(testSongs.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testSongs.getGenre()).isEqualTo(UPDATED_GENRE);
        assertThat(testSongs.getVocal()).isEqualTo(UPDATED_VOCAL);
        assertThat(testSongs.getCountry()).isEqualTo(UPDATED_COUNTRY);
        assertThat(testSongs.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testSongs.getSongAddress()).isEqualTo(UPDATED_SONG_ADDRESS);
        assertThat(testSongs.getLyric()).isEqualTo(UPDATED_LYRIC);
        assertThat(testSongs.getAvatar()).isEqualTo(UPDATED_AVATAR);
        assertThat(testSongs.getListenCount()).isEqualTo(UPDATED_LISTEN_COUNT);
        assertThat(testSongs.getFavoriteCount()).isEqualTo(UPDATED_FAVORITE_COUNT);

        // Validate the Songs in Elasticsearch
        verify(mockSongsSearchRepository, times(1)).save(testSongs);
    }

    @Test
    @Transactional
    public void updateNonExistingSongs() throws Exception {
        int databaseSizeBeforeUpdate = songsRepository.findAll().size();

        // Create the Songs
        SongsDTO songsDTO = songsMapper.toDto(songs);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSongsMockMvc.perform(put("/api/songs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(songsDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Songs in the database
        List<Songs> songsList = songsRepository.findAll();
        assertThat(songsList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Songs in Elasticsearch
        verify(mockSongsSearchRepository, times(0)).save(songs);
    }

    @Test
    @Transactional
    public void deleteSongs() throws Exception {
        // Initialize the database
        songsRepository.saveAndFlush(songs);

        int databaseSizeBeforeDelete = songsRepository.findAll().size();

        // Delete the songs
        restSongsMockMvc.perform(delete("/api/songs/{id}", songs.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Songs> songsList = songsRepository.findAll();
        assertThat(songsList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Songs in Elasticsearch
        verify(mockSongsSearchRepository, times(1)).deleteById(songs.getId());
    }

    @Test
    @Transactional
    public void searchSongs() throws Exception {
        // Initialize the database
        songsRepository.saveAndFlush(songs);
        when(mockSongsSearchRepository.search(queryStringQuery("id:" + songs.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(songs), PageRequest.of(0, 1), 1));
        // Search the songs
        restSongsMockMvc.perform(get("/api/_search/songs?query=id:" + songs.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(songs.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].genre").value(hasItem(DEFAULT_GENRE.toString())))
            .andExpect(jsonPath("$.[*].vocal").value(hasItem(DEFAULT_VOCAL)))
            .andExpect(jsonPath("$.[*].country").value(hasItem(DEFAULT_COUNTRY.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].songAddress").value(hasItem(DEFAULT_SONG_ADDRESS)))
            .andExpect(jsonPath("$.[*].lyric").value(hasItem(DEFAULT_LYRIC.toString())))
            .andExpect(jsonPath("$.[*].avatar").value(hasItem(DEFAULT_AVATAR)))
            .andExpect(jsonPath("$.[*].listenCount").value(hasItem(DEFAULT_LISTEN_COUNT)))
            .andExpect(jsonPath("$.[*].favoriteCount").value(hasItem(DEFAULT_FAVORITE_COUNT)));
    }
}
