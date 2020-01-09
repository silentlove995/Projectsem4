package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.ServerApp;
import com.mycompany.myapp.domain.Pages;
import com.mycompany.myapp.domain.Posts;
import com.mycompany.myapp.repository.PagesRepository;
import com.mycompany.myapp.repository.search.PagesSearchRepository;
import com.mycompany.myapp.service.PagesService;
import com.mycompany.myapp.service.dto.PagesDTO;
import com.mycompany.myapp.service.mapper.PagesMapper;
import com.mycompany.myapp.web.rest.errors.ExceptionTranslator;
import com.mycompany.myapp.service.dto.PagesCriteria;
import com.mycompany.myapp.service.PagesQueryService;

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
 * Integration tests for the {@link PagesResource} REST controller.
 */
@SpringBootTest(classes = ServerApp.class)
public class PagesResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_AVATAR = "AAAAAAAAAA";
    private static final String UPDATED_AVATAR = "BBBBBBBBBB";

    private static final String DEFAULT_IDOL = "AAAAAAAAAA";
    private static final String UPDATED_IDOL = "BBBBBBBBBB";

    @Autowired
    private PagesRepository pagesRepository;

    @Autowired
    private PagesMapper pagesMapper;

    @Autowired
    private PagesService pagesService;

    /**
     * This repository is mocked in the com.mycompany.myapp.repository.search test package.
     *
     * @see com.mycompany.myapp.repository.search.PagesSearchRepositoryMockConfiguration
     */
    @Autowired
    private PagesSearchRepository mockPagesSearchRepository;

    @Autowired
    private PagesQueryService pagesQueryService;

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

    private MockMvc restPagesMockMvc;

    private Pages pages;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final PagesResource pagesResource = new PagesResource(pagesService, pagesQueryService);
        this.restPagesMockMvc = MockMvcBuilders.standaloneSetup(pagesResource)
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
    public static Pages createEntity(EntityManager em) {
        Pages pages = new Pages()
            .name(DEFAULT_NAME)
            .avatar(DEFAULT_AVATAR)
            .idol(DEFAULT_IDOL);
        return pages;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Pages createUpdatedEntity(EntityManager em) {
        Pages pages = new Pages()
            .name(UPDATED_NAME)
            .avatar(UPDATED_AVATAR)
            .idol(UPDATED_IDOL);
        return pages;
    }

    @BeforeEach
    public void initTest() {
        pages = createEntity(em);
    }

    @Test
    @Transactional
    public void createPages() throws Exception {
        int databaseSizeBeforeCreate = pagesRepository.findAll().size();

        // Create the Pages
        PagesDTO pagesDTO = pagesMapper.toDto(pages);
        restPagesMockMvc.perform(post("/api/pages")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(pagesDTO)))
            .andExpect(status().isCreated());

        // Validate the Pages in the database
        List<Pages> pagesList = pagesRepository.findAll();
        assertThat(pagesList).hasSize(databaseSizeBeforeCreate + 1);
        Pages testPages = pagesList.get(pagesList.size() - 1);
        assertThat(testPages.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testPages.getAvatar()).isEqualTo(DEFAULT_AVATAR);
        assertThat(testPages.getIdol()).isEqualTo(DEFAULT_IDOL);

        // Validate the Pages in Elasticsearch
        verify(mockPagesSearchRepository, times(1)).save(testPages);
    }

    @Test
    @Transactional
    public void createPagesWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = pagesRepository.findAll().size();

        // Create the Pages with an existing ID
        pages.setId(1L);
        PagesDTO pagesDTO = pagesMapper.toDto(pages);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPagesMockMvc.perform(post("/api/pages")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(pagesDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Pages in the database
        List<Pages> pagesList = pagesRepository.findAll();
        assertThat(pagesList).hasSize(databaseSizeBeforeCreate);

        // Validate the Pages in Elasticsearch
        verify(mockPagesSearchRepository, times(0)).save(pages);
    }


    @Test
    @Transactional
    public void getAllPages() throws Exception {
        // Initialize the database
        pagesRepository.saveAndFlush(pages);

        // Get all the pagesList
        restPagesMockMvc.perform(get("/api/pages?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(pages.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].avatar").value(hasItem(DEFAULT_AVATAR)))
            .andExpect(jsonPath("$.[*].idol").value(hasItem(DEFAULT_IDOL)));
    }
    
    @Test
    @Transactional
    public void getPages() throws Exception {
        // Initialize the database
        pagesRepository.saveAndFlush(pages);

        // Get the pages
        restPagesMockMvc.perform(get("/api/pages/{id}", pages.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(pages.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.avatar").value(DEFAULT_AVATAR))
            .andExpect(jsonPath("$.idol").value(DEFAULT_IDOL));
    }


    @Test
    @Transactional
    public void getPagesByIdFiltering() throws Exception {
        // Initialize the database
        pagesRepository.saveAndFlush(pages);

        Long id = pages.getId();

        defaultPagesShouldBeFound("id.equals=" + id);
        defaultPagesShouldNotBeFound("id.notEquals=" + id);

        defaultPagesShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultPagesShouldNotBeFound("id.greaterThan=" + id);

        defaultPagesShouldBeFound("id.lessThanOrEqual=" + id);
        defaultPagesShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllPagesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        pagesRepository.saveAndFlush(pages);

        // Get all the pagesList where name equals to DEFAULT_NAME
        defaultPagesShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the pagesList where name equals to UPDATED_NAME
        defaultPagesShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllPagesByNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        pagesRepository.saveAndFlush(pages);

        // Get all the pagesList where name not equals to DEFAULT_NAME
        defaultPagesShouldNotBeFound("name.notEquals=" + DEFAULT_NAME);

        // Get all the pagesList where name not equals to UPDATED_NAME
        defaultPagesShouldBeFound("name.notEquals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllPagesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        pagesRepository.saveAndFlush(pages);

        // Get all the pagesList where name in DEFAULT_NAME or UPDATED_NAME
        defaultPagesShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the pagesList where name equals to UPDATED_NAME
        defaultPagesShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllPagesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        pagesRepository.saveAndFlush(pages);

        // Get all the pagesList where name is not null
        defaultPagesShouldBeFound("name.specified=true");

        // Get all the pagesList where name is null
        defaultPagesShouldNotBeFound("name.specified=false");
    }
                @Test
    @Transactional
    public void getAllPagesByNameContainsSomething() throws Exception {
        // Initialize the database
        pagesRepository.saveAndFlush(pages);

        // Get all the pagesList where name contains DEFAULT_NAME
        defaultPagesShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the pagesList where name contains UPDATED_NAME
        defaultPagesShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllPagesByNameNotContainsSomething() throws Exception {
        // Initialize the database
        pagesRepository.saveAndFlush(pages);

        // Get all the pagesList where name does not contain DEFAULT_NAME
        defaultPagesShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the pagesList where name does not contain UPDATED_NAME
        defaultPagesShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }


    @Test
    @Transactional
    public void getAllPagesByAvatarIsEqualToSomething() throws Exception {
        // Initialize the database
        pagesRepository.saveAndFlush(pages);

        // Get all the pagesList where avatar equals to DEFAULT_AVATAR
        defaultPagesShouldBeFound("avatar.equals=" + DEFAULT_AVATAR);

        // Get all the pagesList where avatar equals to UPDATED_AVATAR
        defaultPagesShouldNotBeFound("avatar.equals=" + UPDATED_AVATAR);
    }

    @Test
    @Transactional
    public void getAllPagesByAvatarIsNotEqualToSomething() throws Exception {
        // Initialize the database
        pagesRepository.saveAndFlush(pages);

        // Get all the pagesList where avatar not equals to DEFAULT_AVATAR
        defaultPagesShouldNotBeFound("avatar.notEquals=" + DEFAULT_AVATAR);

        // Get all the pagesList where avatar not equals to UPDATED_AVATAR
        defaultPagesShouldBeFound("avatar.notEquals=" + UPDATED_AVATAR);
    }

    @Test
    @Transactional
    public void getAllPagesByAvatarIsInShouldWork() throws Exception {
        // Initialize the database
        pagesRepository.saveAndFlush(pages);

        // Get all the pagesList where avatar in DEFAULT_AVATAR or UPDATED_AVATAR
        defaultPagesShouldBeFound("avatar.in=" + DEFAULT_AVATAR + "," + UPDATED_AVATAR);

        // Get all the pagesList where avatar equals to UPDATED_AVATAR
        defaultPagesShouldNotBeFound("avatar.in=" + UPDATED_AVATAR);
    }

    @Test
    @Transactional
    public void getAllPagesByAvatarIsNullOrNotNull() throws Exception {
        // Initialize the database
        pagesRepository.saveAndFlush(pages);

        // Get all the pagesList where avatar is not null
        defaultPagesShouldBeFound("avatar.specified=true");

        // Get all the pagesList where avatar is null
        defaultPagesShouldNotBeFound("avatar.specified=false");
    }
                @Test
    @Transactional
    public void getAllPagesByAvatarContainsSomething() throws Exception {
        // Initialize the database
        pagesRepository.saveAndFlush(pages);

        // Get all the pagesList where avatar contains DEFAULT_AVATAR
        defaultPagesShouldBeFound("avatar.contains=" + DEFAULT_AVATAR);

        // Get all the pagesList where avatar contains UPDATED_AVATAR
        defaultPagesShouldNotBeFound("avatar.contains=" + UPDATED_AVATAR);
    }

    @Test
    @Transactional
    public void getAllPagesByAvatarNotContainsSomething() throws Exception {
        // Initialize the database
        pagesRepository.saveAndFlush(pages);

        // Get all the pagesList where avatar does not contain DEFAULT_AVATAR
        defaultPagesShouldNotBeFound("avatar.doesNotContain=" + DEFAULT_AVATAR);

        // Get all the pagesList where avatar does not contain UPDATED_AVATAR
        defaultPagesShouldBeFound("avatar.doesNotContain=" + UPDATED_AVATAR);
    }


    @Test
    @Transactional
    public void getAllPagesByIdolIsEqualToSomething() throws Exception {
        // Initialize the database
        pagesRepository.saveAndFlush(pages);

        // Get all the pagesList where idol equals to DEFAULT_IDOL
        defaultPagesShouldBeFound("idol.equals=" + DEFAULT_IDOL);

        // Get all the pagesList where idol equals to UPDATED_IDOL
        defaultPagesShouldNotBeFound("idol.equals=" + UPDATED_IDOL);
    }

    @Test
    @Transactional
    public void getAllPagesByIdolIsNotEqualToSomething() throws Exception {
        // Initialize the database
        pagesRepository.saveAndFlush(pages);

        // Get all the pagesList where idol not equals to DEFAULT_IDOL
        defaultPagesShouldNotBeFound("idol.notEquals=" + DEFAULT_IDOL);

        // Get all the pagesList where idol not equals to UPDATED_IDOL
        defaultPagesShouldBeFound("idol.notEquals=" + UPDATED_IDOL);
    }

    @Test
    @Transactional
    public void getAllPagesByIdolIsInShouldWork() throws Exception {
        // Initialize the database
        pagesRepository.saveAndFlush(pages);

        // Get all the pagesList where idol in DEFAULT_IDOL or UPDATED_IDOL
        defaultPagesShouldBeFound("idol.in=" + DEFAULT_IDOL + "," + UPDATED_IDOL);

        // Get all the pagesList where idol equals to UPDATED_IDOL
        defaultPagesShouldNotBeFound("idol.in=" + UPDATED_IDOL);
    }

    @Test
    @Transactional
    public void getAllPagesByIdolIsNullOrNotNull() throws Exception {
        // Initialize the database
        pagesRepository.saveAndFlush(pages);

        // Get all the pagesList where idol is not null
        defaultPagesShouldBeFound("idol.specified=true");

        // Get all the pagesList where idol is null
        defaultPagesShouldNotBeFound("idol.specified=false");
    }
                @Test
    @Transactional
    public void getAllPagesByIdolContainsSomething() throws Exception {
        // Initialize the database
        pagesRepository.saveAndFlush(pages);

        // Get all the pagesList where idol contains DEFAULT_IDOL
        defaultPagesShouldBeFound("idol.contains=" + DEFAULT_IDOL);

        // Get all the pagesList where idol contains UPDATED_IDOL
        defaultPagesShouldNotBeFound("idol.contains=" + UPDATED_IDOL);
    }

    @Test
    @Transactional
    public void getAllPagesByIdolNotContainsSomething() throws Exception {
        // Initialize the database
        pagesRepository.saveAndFlush(pages);

        // Get all the pagesList where idol does not contain DEFAULT_IDOL
        defaultPagesShouldNotBeFound("idol.doesNotContain=" + DEFAULT_IDOL);

        // Get all the pagesList where idol does not contain UPDATED_IDOL
        defaultPagesShouldBeFound("idol.doesNotContain=" + UPDATED_IDOL);
    }


    @Test
    @Transactional
    public void getAllPagesByTitleIsEqualToSomething() throws Exception {
        // Initialize the database
        pagesRepository.saveAndFlush(pages);
        Posts title = PostsResourceIT.createEntity(em);
        em.persist(title);
        em.flush();
        pages.addTitle(title);
        pagesRepository.saveAndFlush(pages);
        Long titleId = title.getId();

        // Get all the pagesList where title equals to titleId
        defaultPagesShouldBeFound("titleId.equals=" + titleId);

        // Get all the pagesList where title equals to titleId + 1
        defaultPagesShouldNotBeFound("titleId.equals=" + (titleId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultPagesShouldBeFound(String filter) throws Exception {
        restPagesMockMvc.perform(get("/api/pages?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(pages.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].avatar").value(hasItem(DEFAULT_AVATAR)))
            .andExpect(jsonPath("$.[*].idol").value(hasItem(DEFAULT_IDOL)));

        // Check, that the count call also returns 1
        restPagesMockMvc.perform(get("/api/pages/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultPagesShouldNotBeFound(String filter) throws Exception {
        restPagesMockMvc.perform(get("/api/pages?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restPagesMockMvc.perform(get("/api/pages/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingPages() throws Exception {
        // Get the pages
        restPagesMockMvc.perform(get("/api/pages/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePages() throws Exception {
        // Initialize the database
        pagesRepository.saveAndFlush(pages);

        int databaseSizeBeforeUpdate = pagesRepository.findAll().size();

        // Update the pages
        Pages updatedPages = pagesRepository.findById(pages.getId()).get();
        // Disconnect from session so that the updates on updatedPages are not directly saved in db
        em.detach(updatedPages);
        updatedPages
            .name(UPDATED_NAME)
            .avatar(UPDATED_AVATAR)
            .idol(UPDATED_IDOL);
        PagesDTO pagesDTO = pagesMapper.toDto(updatedPages);

        restPagesMockMvc.perform(put("/api/pages")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(pagesDTO)))
            .andExpect(status().isOk());

        // Validate the Pages in the database
        List<Pages> pagesList = pagesRepository.findAll();
        assertThat(pagesList).hasSize(databaseSizeBeforeUpdate);
        Pages testPages = pagesList.get(pagesList.size() - 1);
        assertThat(testPages.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testPages.getAvatar()).isEqualTo(UPDATED_AVATAR);
        assertThat(testPages.getIdol()).isEqualTo(UPDATED_IDOL);

        // Validate the Pages in Elasticsearch
        verify(mockPagesSearchRepository, times(1)).save(testPages);
    }

    @Test
    @Transactional
    public void updateNonExistingPages() throws Exception {
        int databaseSizeBeforeUpdate = pagesRepository.findAll().size();

        // Create the Pages
        PagesDTO pagesDTO = pagesMapper.toDto(pages);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPagesMockMvc.perform(put("/api/pages")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(pagesDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Pages in the database
        List<Pages> pagesList = pagesRepository.findAll();
        assertThat(pagesList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Pages in Elasticsearch
        verify(mockPagesSearchRepository, times(0)).save(pages);
    }

    @Test
    @Transactional
    public void deletePages() throws Exception {
        // Initialize the database
        pagesRepository.saveAndFlush(pages);

        int databaseSizeBeforeDelete = pagesRepository.findAll().size();

        // Delete the pages
        restPagesMockMvc.perform(delete("/api/pages/{id}", pages.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Pages> pagesList = pagesRepository.findAll();
        assertThat(pagesList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Pages in Elasticsearch
        verify(mockPagesSearchRepository, times(1)).deleteById(pages.getId());
    }

    @Test
    @Transactional
    public void searchPages() throws Exception {
        // Initialize the database
        pagesRepository.saveAndFlush(pages);
        when(mockPagesSearchRepository.search(queryStringQuery("id:" + pages.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(pages), PageRequest.of(0, 1), 1));
        // Search the pages
        restPagesMockMvc.perform(get("/api/_search/pages?query=id:" + pages.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(pages.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].avatar").value(hasItem(DEFAULT_AVATAR)))
            .andExpect(jsonPath("$.[*].idol").value(hasItem(DEFAULT_IDOL)));
    }
}
