package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.ServerApp;
import com.mycompany.myapp.domain.Posts;
import com.mycompany.myapp.domain.Pages;
import com.mycompany.myapp.repository.PostsRepository;
import com.mycompany.myapp.repository.search.PostsSearchRepository;
import com.mycompany.myapp.service.PostsService;
import com.mycompany.myapp.service.dto.PostsDTO;
import com.mycompany.myapp.service.mapper.PostsMapper;
import com.mycompany.myapp.web.rest.errors.ExceptionTranslator;
import com.mycompany.myapp.service.dto.PostsCriteria;
import com.mycompany.myapp.service.PostsQueryService;

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
 * Integration tests for the {@link PostsResource} REST controller.
 */
@SpringBootTest(classes = ServerApp.class)
public class PostsResourceIT {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_CONTENT = "AAAAAAAAAA";
    private static final String UPDATED_CONTENT = "BBBBBBBBBB";

    private static final String DEFAULT_COMMENT = "AAAAAAAAAA";
    private static final String UPDATED_COMMENT = "BBBBBBBBBB";

    private static final String DEFAULT_IMAGE = "AAAAAAAAAA";
    private static final String UPDATED_IMAGE = "BBBBBBBBBB";

    private static final Integer DEFAULT_LIKE = 1;
    private static final Integer UPDATED_LIKE = 2;
    private static final Integer SMALLER_LIKE = 1 - 1;

    private static final String DEFAULT_SONG_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_SONG_ADDRESS = "BBBBBBBBBB";

    @Autowired
    private PostsRepository postsRepository;

    @Autowired
    private PostsMapper postsMapper;

    @Autowired
    private PostsService postsService;

    /**
     * This repository is mocked in the com.mycompany.myapp.repository.search test package.
     *
     * @see com.mycompany.myapp.repository.search.PostsSearchRepositoryMockConfiguration
     */
    @Autowired
    private PostsSearchRepository mockPostsSearchRepository;

    @Autowired
    private PostsQueryService postsQueryService;

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

    private MockMvc restPostsMockMvc;

    private Posts posts;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final PostsResource postsResource = new PostsResource(postsService, postsQueryService);
        this.restPostsMockMvc = MockMvcBuilders.standaloneSetup(postsResource)
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
    public static Posts createEntity(EntityManager em) {
        Posts posts = new Posts()
            .title(DEFAULT_TITLE)
            .content(DEFAULT_CONTENT)
            .comment(DEFAULT_COMMENT)
            .image(DEFAULT_IMAGE)
            .like(DEFAULT_LIKE)
            .songAddress(DEFAULT_SONG_ADDRESS);
        return posts;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Posts createUpdatedEntity(EntityManager em) {
        Posts posts = new Posts()
            .title(UPDATED_TITLE)
            .content(UPDATED_CONTENT)
            .comment(UPDATED_COMMENT)
            .image(UPDATED_IMAGE)
            .like(UPDATED_LIKE)
            .songAddress(UPDATED_SONG_ADDRESS);
        return posts;
    }

    @BeforeEach
    public void initTest() {
        posts = createEntity(em);
    }

    @Test
    @Transactional
    public void createPosts() throws Exception {
        int databaseSizeBeforeCreate = postsRepository.findAll().size();

        // Create the Posts
        PostsDTO postsDTO = postsMapper.toDto(posts);
        restPostsMockMvc.perform(post("/api/posts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(postsDTO)))
            .andExpect(status().isCreated());

        // Validate the Posts in the database
        List<Posts> postsList = postsRepository.findAll();
        assertThat(postsList).hasSize(databaseSizeBeforeCreate + 1);
        Posts testPosts = postsList.get(postsList.size() - 1);
        assertThat(testPosts.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testPosts.getContent()).isEqualTo(DEFAULT_CONTENT);
        assertThat(testPosts.getComment()).isEqualTo(DEFAULT_COMMENT);
        assertThat(testPosts.getImage()).isEqualTo(DEFAULT_IMAGE);
        assertThat(testPosts.getLike()).isEqualTo(DEFAULT_LIKE);
        assertThat(testPosts.getSongAddress()).isEqualTo(DEFAULT_SONG_ADDRESS);

        // Validate the Posts in Elasticsearch
        verify(mockPostsSearchRepository, times(1)).save(testPosts);
    }

    @Test
    @Transactional
    public void createPostsWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = postsRepository.findAll().size();

        // Create the Posts with an existing ID
        posts.setId(1L);
        PostsDTO postsDTO = postsMapper.toDto(posts);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPostsMockMvc.perform(post("/api/posts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(postsDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Posts in the database
        List<Posts> postsList = postsRepository.findAll();
        assertThat(postsList).hasSize(databaseSizeBeforeCreate);

        // Validate the Posts in Elasticsearch
        verify(mockPostsSearchRepository, times(0)).save(posts);
    }


    @Test
    @Transactional
    public void getAllPosts() throws Exception {
        // Initialize the database
        postsRepository.saveAndFlush(posts);

        // Get all the postsList
        restPostsMockMvc.perform(get("/api/posts?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(posts.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].content").value(hasItem(DEFAULT_CONTENT)))
            .andExpect(jsonPath("$.[*].comment").value(hasItem(DEFAULT_COMMENT)))
            .andExpect(jsonPath("$.[*].image").value(hasItem(DEFAULT_IMAGE)))
            .andExpect(jsonPath("$.[*].like").value(hasItem(DEFAULT_LIKE)))
            .andExpect(jsonPath("$.[*].songAddress").value(hasItem(DEFAULT_SONG_ADDRESS)));
    }
    
    @Test
    @Transactional
    public void getPosts() throws Exception {
        // Initialize the database
        postsRepository.saveAndFlush(posts);

        // Get the posts
        restPostsMockMvc.perform(get("/api/posts/{id}", posts.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(posts.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.content").value(DEFAULT_CONTENT))
            .andExpect(jsonPath("$.comment").value(DEFAULT_COMMENT))
            .andExpect(jsonPath("$.image").value(DEFAULT_IMAGE))
            .andExpect(jsonPath("$.like").value(DEFAULT_LIKE))
            .andExpect(jsonPath("$.songAddress").value(DEFAULT_SONG_ADDRESS));
    }


    @Test
    @Transactional
    public void getPostsByIdFiltering() throws Exception {
        // Initialize the database
        postsRepository.saveAndFlush(posts);

        Long id = posts.getId();

        defaultPostsShouldBeFound("id.equals=" + id);
        defaultPostsShouldNotBeFound("id.notEquals=" + id);

        defaultPostsShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultPostsShouldNotBeFound("id.greaterThan=" + id);

        defaultPostsShouldBeFound("id.lessThanOrEqual=" + id);
        defaultPostsShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllPostsByTitleIsEqualToSomething() throws Exception {
        // Initialize the database
        postsRepository.saveAndFlush(posts);

        // Get all the postsList where title equals to DEFAULT_TITLE
        defaultPostsShouldBeFound("title.equals=" + DEFAULT_TITLE);

        // Get all the postsList where title equals to UPDATED_TITLE
        defaultPostsShouldNotBeFound("title.equals=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    public void getAllPostsByTitleIsNotEqualToSomething() throws Exception {
        // Initialize the database
        postsRepository.saveAndFlush(posts);

        // Get all the postsList where title not equals to DEFAULT_TITLE
        defaultPostsShouldNotBeFound("title.notEquals=" + DEFAULT_TITLE);

        // Get all the postsList where title not equals to UPDATED_TITLE
        defaultPostsShouldBeFound("title.notEquals=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    public void getAllPostsByTitleIsInShouldWork() throws Exception {
        // Initialize the database
        postsRepository.saveAndFlush(posts);

        // Get all the postsList where title in DEFAULT_TITLE or UPDATED_TITLE
        defaultPostsShouldBeFound("title.in=" + DEFAULT_TITLE + "," + UPDATED_TITLE);

        // Get all the postsList where title equals to UPDATED_TITLE
        defaultPostsShouldNotBeFound("title.in=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    public void getAllPostsByTitleIsNullOrNotNull() throws Exception {
        // Initialize the database
        postsRepository.saveAndFlush(posts);

        // Get all the postsList where title is not null
        defaultPostsShouldBeFound("title.specified=true");

        // Get all the postsList where title is null
        defaultPostsShouldNotBeFound("title.specified=false");
    }
                @Test
    @Transactional
    public void getAllPostsByTitleContainsSomething() throws Exception {
        // Initialize the database
        postsRepository.saveAndFlush(posts);

        // Get all the postsList where title contains DEFAULT_TITLE
        defaultPostsShouldBeFound("title.contains=" + DEFAULT_TITLE);

        // Get all the postsList where title contains UPDATED_TITLE
        defaultPostsShouldNotBeFound("title.contains=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    public void getAllPostsByTitleNotContainsSomething() throws Exception {
        // Initialize the database
        postsRepository.saveAndFlush(posts);

        // Get all the postsList where title does not contain DEFAULT_TITLE
        defaultPostsShouldNotBeFound("title.doesNotContain=" + DEFAULT_TITLE);

        // Get all the postsList where title does not contain UPDATED_TITLE
        defaultPostsShouldBeFound("title.doesNotContain=" + UPDATED_TITLE);
    }


    @Test
    @Transactional
    public void getAllPostsByContentIsEqualToSomething() throws Exception {
        // Initialize the database
        postsRepository.saveAndFlush(posts);

        // Get all the postsList where content equals to DEFAULT_CONTENT
        defaultPostsShouldBeFound("content.equals=" + DEFAULT_CONTENT);

        // Get all the postsList where content equals to UPDATED_CONTENT
        defaultPostsShouldNotBeFound("content.equals=" + UPDATED_CONTENT);
    }

    @Test
    @Transactional
    public void getAllPostsByContentIsNotEqualToSomething() throws Exception {
        // Initialize the database
        postsRepository.saveAndFlush(posts);

        // Get all the postsList where content not equals to DEFAULT_CONTENT
        defaultPostsShouldNotBeFound("content.notEquals=" + DEFAULT_CONTENT);

        // Get all the postsList where content not equals to UPDATED_CONTENT
        defaultPostsShouldBeFound("content.notEquals=" + UPDATED_CONTENT);
    }

    @Test
    @Transactional
    public void getAllPostsByContentIsInShouldWork() throws Exception {
        // Initialize the database
        postsRepository.saveAndFlush(posts);

        // Get all the postsList where content in DEFAULT_CONTENT or UPDATED_CONTENT
        defaultPostsShouldBeFound("content.in=" + DEFAULT_CONTENT + "," + UPDATED_CONTENT);

        // Get all the postsList where content equals to UPDATED_CONTENT
        defaultPostsShouldNotBeFound("content.in=" + UPDATED_CONTENT);
    }

    @Test
    @Transactional
    public void getAllPostsByContentIsNullOrNotNull() throws Exception {
        // Initialize the database
        postsRepository.saveAndFlush(posts);

        // Get all the postsList where content is not null
        defaultPostsShouldBeFound("content.specified=true");

        // Get all the postsList where content is null
        defaultPostsShouldNotBeFound("content.specified=false");
    }
                @Test
    @Transactional
    public void getAllPostsByContentContainsSomething() throws Exception {
        // Initialize the database
        postsRepository.saveAndFlush(posts);

        // Get all the postsList where content contains DEFAULT_CONTENT
        defaultPostsShouldBeFound("content.contains=" + DEFAULT_CONTENT);

        // Get all the postsList where content contains UPDATED_CONTENT
        defaultPostsShouldNotBeFound("content.contains=" + UPDATED_CONTENT);
    }

    @Test
    @Transactional
    public void getAllPostsByContentNotContainsSomething() throws Exception {
        // Initialize the database
        postsRepository.saveAndFlush(posts);

        // Get all the postsList where content does not contain DEFAULT_CONTENT
        defaultPostsShouldNotBeFound("content.doesNotContain=" + DEFAULT_CONTENT);

        // Get all the postsList where content does not contain UPDATED_CONTENT
        defaultPostsShouldBeFound("content.doesNotContain=" + UPDATED_CONTENT);
    }


    @Test
    @Transactional
    public void getAllPostsByCommentIsEqualToSomething() throws Exception {
        // Initialize the database
        postsRepository.saveAndFlush(posts);

        // Get all the postsList where comment equals to DEFAULT_COMMENT
        defaultPostsShouldBeFound("comment.equals=" + DEFAULT_COMMENT);

        // Get all the postsList where comment equals to UPDATED_COMMENT
        defaultPostsShouldNotBeFound("comment.equals=" + UPDATED_COMMENT);
    }

    @Test
    @Transactional
    public void getAllPostsByCommentIsNotEqualToSomething() throws Exception {
        // Initialize the database
        postsRepository.saveAndFlush(posts);

        // Get all the postsList where comment not equals to DEFAULT_COMMENT
        defaultPostsShouldNotBeFound("comment.notEquals=" + DEFAULT_COMMENT);

        // Get all the postsList where comment not equals to UPDATED_COMMENT
        defaultPostsShouldBeFound("comment.notEquals=" + UPDATED_COMMENT);
    }

    @Test
    @Transactional
    public void getAllPostsByCommentIsInShouldWork() throws Exception {
        // Initialize the database
        postsRepository.saveAndFlush(posts);

        // Get all the postsList where comment in DEFAULT_COMMENT or UPDATED_COMMENT
        defaultPostsShouldBeFound("comment.in=" + DEFAULT_COMMENT + "," + UPDATED_COMMENT);

        // Get all the postsList where comment equals to UPDATED_COMMENT
        defaultPostsShouldNotBeFound("comment.in=" + UPDATED_COMMENT);
    }

    @Test
    @Transactional
    public void getAllPostsByCommentIsNullOrNotNull() throws Exception {
        // Initialize the database
        postsRepository.saveAndFlush(posts);

        // Get all the postsList where comment is not null
        defaultPostsShouldBeFound("comment.specified=true");

        // Get all the postsList where comment is null
        defaultPostsShouldNotBeFound("comment.specified=false");
    }
                @Test
    @Transactional
    public void getAllPostsByCommentContainsSomething() throws Exception {
        // Initialize the database
        postsRepository.saveAndFlush(posts);

        // Get all the postsList where comment contains DEFAULT_COMMENT
        defaultPostsShouldBeFound("comment.contains=" + DEFAULT_COMMENT);

        // Get all the postsList where comment contains UPDATED_COMMENT
        defaultPostsShouldNotBeFound("comment.contains=" + UPDATED_COMMENT);
    }

    @Test
    @Transactional
    public void getAllPostsByCommentNotContainsSomething() throws Exception {
        // Initialize the database
        postsRepository.saveAndFlush(posts);

        // Get all the postsList where comment does not contain DEFAULT_COMMENT
        defaultPostsShouldNotBeFound("comment.doesNotContain=" + DEFAULT_COMMENT);

        // Get all the postsList where comment does not contain UPDATED_COMMENT
        defaultPostsShouldBeFound("comment.doesNotContain=" + UPDATED_COMMENT);
    }


    @Test
    @Transactional
    public void getAllPostsByImageIsEqualToSomething() throws Exception {
        // Initialize the database
        postsRepository.saveAndFlush(posts);

        // Get all the postsList where image equals to DEFAULT_IMAGE
        defaultPostsShouldBeFound("image.equals=" + DEFAULT_IMAGE);

        // Get all the postsList where image equals to UPDATED_IMAGE
        defaultPostsShouldNotBeFound("image.equals=" + UPDATED_IMAGE);
    }

    @Test
    @Transactional
    public void getAllPostsByImageIsNotEqualToSomething() throws Exception {
        // Initialize the database
        postsRepository.saveAndFlush(posts);

        // Get all the postsList where image not equals to DEFAULT_IMAGE
        defaultPostsShouldNotBeFound("image.notEquals=" + DEFAULT_IMAGE);

        // Get all the postsList where image not equals to UPDATED_IMAGE
        defaultPostsShouldBeFound("image.notEquals=" + UPDATED_IMAGE);
    }

    @Test
    @Transactional
    public void getAllPostsByImageIsInShouldWork() throws Exception {
        // Initialize the database
        postsRepository.saveAndFlush(posts);

        // Get all the postsList where image in DEFAULT_IMAGE or UPDATED_IMAGE
        defaultPostsShouldBeFound("image.in=" + DEFAULT_IMAGE + "," + UPDATED_IMAGE);

        // Get all the postsList where image equals to UPDATED_IMAGE
        defaultPostsShouldNotBeFound("image.in=" + UPDATED_IMAGE);
    }

    @Test
    @Transactional
    public void getAllPostsByImageIsNullOrNotNull() throws Exception {
        // Initialize the database
        postsRepository.saveAndFlush(posts);

        // Get all the postsList where image is not null
        defaultPostsShouldBeFound("image.specified=true");

        // Get all the postsList where image is null
        defaultPostsShouldNotBeFound("image.specified=false");
    }
                @Test
    @Transactional
    public void getAllPostsByImageContainsSomething() throws Exception {
        // Initialize the database
        postsRepository.saveAndFlush(posts);

        // Get all the postsList where image contains DEFAULT_IMAGE
        defaultPostsShouldBeFound("image.contains=" + DEFAULT_IMAGE);

        // Get all the postsList where image contains UPDATED_IMAGE
        defaultPostsShouldNotBeFound("image.contains=" + UPDATED_IMAGE);
    }

    @Test
    @Transactional
    public void getAllPostsByImageNotContainsSomething() throws Exception {
        // Initialize the database
        postsRepository.saveAndFlush(posts);

        // Get all the postsList where image does not contain DEFAULT_IMAGE
        defaultPostsShouldNotBeFound("image.doesNotContain=" + DEFAULT_IMAGE);

        // Get all the postsList where image does not contain UPDATED_IMAGE
        defaultPostsShouldBeFound("image.doesNotContain=" + UPDATED_IMAGE);
    }


    @Test
    @Transactional
    public void getAllPostsByLikeIsEqualToSomething() throws Exception {
        // Initialize the database
        postsRepository.saveAndFlush(posts);

        // Get all the postsList where like equals to DEFAULT_LIKE
        defaultPostsShouldBeFound("like.equals=" + DEFAULT_LIKE);

        // Get all the postsList where like equals to UPDATED_LIKE
        defaultPostsShouldNotBeFound("like.equals=" + UPDATED_LIKE);
    }

    @Test
    @Transactional
    public void getAllPostsByLikeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        postsRepository.saveAndFlush(posts);

        // Get all the postsList where like not equals to DEFAULT_LIKE
        defaultPostsShouldNotBeFound("like.notEquals=" + DEFAULT_LIKE);

        // Get all the postsList where like not equals to UPDATED_LIKE
        defaultPostsShouldBeFound("like.notEquals=" + UPDATED_LIKE);
    }

    @Test
    @Transactional
    public void getAllPostsByLikeIsInShouldWork() throws Exception {
        // Initialize the database
        postsRepository.saveAndFlush(posts);

        // Get all the postsList where like in DEFAULT_LIKE or UPDATED_LIKE
        defaultPostsShouldBeFound("like.in=" + DEFAULT_LIKE + "," + UPDATED_LIKE);

        // Get all the postsList where like equals to UPDATED_LIKE
        defaultPostsShouldNotBeFound("like.in=" + UPDATED_LIKE);
    }

    @Test
    @Transactional
    public void getAllPostsByLikeIsNullOrNotNull() throws Exception {
        // Initialize the database
        postsRepository.saveAndFlush(posts);

        // Get all the postsList where like is not null
        defaultPostsShouldBeFound("like.specified=true");

        // Get all the postsList where like is null
        defaultPostsShouldNotBeFound("like.specified=false");
    }

    @Test
    @Transactional
    public void getAllPostsByLikeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        postsRepository.saveAndFlush(posts);

        // Get all the postsList where like is greater than or equal to DEFAULT_LIKE
        defaultPostsShouldBeFound("like.greaterThanOrEqual=" + DEFAULT_LIKE);

        // Get all the postsList where like is greater than or equal to UPDATED_LIKE
        defaultPostsShouldNotBeFound("like.greaterThanOrEqual=" + UPDATED_LIKE);
    }

    @Test
    @Transactional
    public void getAllPostsByLikeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        postsRepository.saveAndFlush(posts);

        // Get all the postsList where like is less than or equal to DEFAULT_LIKE
        defaultPostsShouldBeFound("like.lessThanOrEqual=" + DEFAULT_LIKE);

        // Get all the postsList where like is less than or equal to SMALLER_LIKE
        defaultPostsShouldNotBeFound("like.lessThanOrEqual=" + SMALLER_LIKE);
    }

    @Test
    @Transactional
    public void getAllPostsByLikeIsLessThanSomething() throws Exception {
        // Initialize the database
        postsRepository.saveAndFlush(posts);

        // Get all the postsList where like is less than DEFAULT_LIKE
        defaultPostsShouldNotBeFound("like.lessThan=" + DEFAULT_LIKE);

        // Get all the postsList where like is less than UPDATED_LIKE
        defaultPostsShouldBeFound("like.lessThan=" + UPDATED_LIKE);
    }

    @Test
    @Transactional
    public void getAllPostsByLikeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        postsRepository.saveAndFlush(posts);

        // Get all the postsList where like is greater than DEFAULT_LIKE
        defaultPostsShouldNotBeFound("like.greaterThan=" + DEFAULT_LIKE);

        // Get all the postsList where like is greater than SMALLER_LIKE
        defaultPostsShouldBeFound("like.greaterThan=" + SMALLER_LIKE);
    }


    @Test
    @Transactional
    public void getAllPostsBySongAddressIsEqualToSomething() throws Exception {
        // Initialize the database
        postsRepository.saveAndFlush(posts);

        // Get all the postsList where songAddress equals to DEFAULT_SONG_ADDRESS
        defaultPostsShouldBeFound("songAddress.equals=" + DEFAULT_SONG_ADDRESS);

        // Get all the postsList where songAddress equals to UPDATED_SONG_ADDRESS
        defaultPostsShouldNotBeFound("songAddress.equals=" + UPDATED_SONG_ADDRESS);
    }

    @Test
    @Transactional
    public void getAllPostsBySongAddressIsNotEqualToSomething() throws Exception {
        // Initialize the database
        postsRepository.saveAndFlush(posts);

        // Get all the postsList where songAddress not equals to DEFAULT_SONG_ADDRESS
        defaultPostsShouldNotBeFound("songAddress.notEquals=" + DEFAULT_SONG_ADDRESS);

        // Get all the postsList where songAddress not equals to UPDATED_SONG_ADDRESS
        defaultPostsShouldBeFound("songAddress.notEquals=" + UPDATED_SONG_ADDRESS);
    }

    @Test
    @Transactional
    public void getAllPostsBySongAddressIsInShouldWork() throws Exception {
        // Initialize the database
        postsRepository.saveAndFlush(posts);

        // Get all the postsList where songAddress in DEFAULT_SONG_ADDRESS or UPDATED_SONG_ADDRESS
        defaultPostsShouldBeFound("songAddress.in=" + DEFAULT_SONG_ADDRESS + "," + UPDATED_SONG_ADDRESS);

        // Get all the postsList where songAddress equals to UPDATED_SONG_ADDRESS
        defaultPostsShouldNotBeFound("songAddress.in=" + UPDATED_SONG_ADDRESS);
    }

    @Test
    @Transactional
    public void getAllPostsBySongAddressIsNullOrNotNull() throws Exception {
        // Initialize the database
        postsRepository.saveAndFlush(posts);

        // Get all the postsList where songAddress is not null
        defaultPostsShouldBeFound("songAddress.specified=true");

        // Get all the postsList where songAddress is null
        defaultPostsShouldNotBeFound("songAddress.specified=false");
    }
                @Test
    @Transactional
    public void getAllPostsBySongAddressContainsSomething() throws Exception {
        // Initialize the database
        postsRepository.saveAndFlush(posts);

        // Get all the postsList where songAddress contains DEFAULT_SONG_ADDRESS
        defaultPostsShouldBeFound("songAddress.contains=" + DEFAULT_SONG_ADDRESS);

        // Get all the postsList where songAddress contains UPDATED_SONG_ADDRESS
        defaultPostsShouldNotBeFound("songAddress.contains=" + UPDATED_SONG_ADDRESS);
    }

    @Test
    @Transactional
    public void getAllPostsBySongAddressNotContainsSomething() throws Exception {
        // Initialize the database
        postsRepository.saveAndFlush(posts);

        // Get all the postsList where songAddress does not contain DEFAULT_SONG_ADDRESS
        defaultPostsShouldNotBeFound("songAddress.doesNotContain=" + DEFAULT_SONG_ADDRESS);

        // Get all the postsList where songAddress does not contain UPDATED_SONG_ADDRESS
        defaultPostsShouldBeFound("songAddress.doesNotContain=" + UPDATED_SONG_ADDRESS);
    }


    @Test
    @Transactional
    public void getAllPostsByPagesIsEqualToSomething() throws Exception {
        // Initialize the database
        postsRepository.saveAndFlush(posts);
        Pages pages = PagesResourceIT.createEntity(em);
        em.persist(pages);
        em.flush();
        posts.setPages(pages);
        postsRepository.saveAndFlush(posts);
        Long pagesId = pages.getId();

        // Get all the postsList where pages equals to pagesId
        defaultPostsShouldBeFound("pagesId.equals=" + pagesId);

        // Get all the postsList where pages equals to pagesId + 1
        defaultPostsShouldNotBeFound("pagesId.equals=" + (pagesId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultPostsShouldBeFound(String filter) throws Exception {
        restPostsMockMvc.perform(get("/api/posts?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(posts.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].content").value(hasItem(DEFAULT_CONTENT)))
            .andExpect(jsonPath("$.[*].comment").value(hasItem(DEFAULT_COMMENT)))
            .andExpect(jsonPath("$.[*].image").value(hasItem(DEFAULT_IMAGE)))
            .andExpect(jsonPath("$.[*].like").value(hasItem(DEFAULT_LIKE)))
            .andExpect(jsonPath("$.[*].songAddress").value(hasItem(DEFAULT_SONG_ADDRESS)));

        // Check, that the count call also returns 1
        restPostsMockMvc.perform(get("/api/posts/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultPostsShouldNotBeFound(String filter) throws Exception {
        restPostsMockMvc.perform(get("/api/posts?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restPostsMockMvc.perform(get("/api/posts/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingPosts() throws Exception {
        // Get the posts
        restPostsMockMvc.perform(get("/api/posts/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePosts() throws Exception {
        // Initialize the database
        postsRepository.saveAndFlush(posts);

        int databaseSizeBeforeUpdate = postsRepository.findAll().size();

        // Update the posts
        Posts updatedPosts = postsRepository.findById(posts.getId()).get();
        // Disconnect from session so that the updates on updatedPosts are not directly saved in db
        em.detach(updatedPosts);
        updatedPosts
            .title(UPDATED_TITLE)
            .content(UPDATED_CONTENT)
            .comment(UPDATED_COMMENT)
            .image(UPDATED_IMAGE)
            .like(UPDATED_LIKE)
            .songAddress(UPDATED_SONG_ADDRESS);
        PostsDTO postsDTO = postsMapper.toDto(updatedPosts);

        restPostsMockMvc.perform(put("/api/posts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(postsDTO)))
            .andExpect(status().isOk());

        // Validate the Posts in the database
        List<Posts> postsList = postsRepository.findAll();
        assertThat(postsList).hasSize(databaseSizeBeforeUpdate);
        Posts testPosts = postsList.get(postsList.size() - 1);
        assertThat(testPosts.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testPosts.getContent()).isEqualTo(UPDATED_CONTENT);
        assertThat(testPosts.getComment()).isEqualTo(UPDATED_COMMENT);
        assertThat(testPosts.getImage()).isEqualTo(UPDATED_IMAGE);
        assertThat(testPosts.getLike()).isEqualTo(UPDATED_LIKE);
        assertThat(testPosts.getSongAddress()).isEqualTo(UPDATED_SONG_ADDRESS);

        // Validate the Posts in Elasticsearch
        verify(mockPostsSearchRepository, times(1)).save(testPosts);
    }

    @Test
    @Transactional
    public void updateNonExistingPosts() throws Exception {
        int databaseSizeBeforeUpdate = postsRepository.findAll().size();

        // Create the Posts
        PostsDTO postsDTO = postsMapper.toDto(posts);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPostsMockMvc.perform(put("/api/posts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(postsDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Posts in the database
        List<Posts> postsList = postsRepository.findAll();
        assertThat(postsList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Posts in Elasticsearch
        verify(mockPostsSearchRepository, times(0)).save(posts);
    }

    @Test
    @Transactional
    public void deletePosts() throws Exception {
        // Initialize the database
        postsRepository.saveAndFlush(posts);

        int databaseSizeBeforeDelete = postsRepository.findAll().size();

        // Delete the posts
        restPostsMockMvc.perform(delete("/api/posts/{id}", posts.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Posts> postsList = postsRepository.findAll();
        assertThat(postsList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Posts in Elasticsearch
        verify(mockPostsSearchRepository, times(1)).deleteById(posts.getId());
    }

    @Test
    @Transactional
    public void searchPosts() throws Exception {
        // Initialize the database
        postsRepository.saveAndFlush(posts);
        when(mockPostsSearchRepository.search(queryStringQuery("id:" + posts.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(posts), PageRequest.of(0, 1), 1));
        // Search the posts
        restPostsMockMvc.perform(get("/api/_search/posts?query=id:" + posts.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(posts.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].content").value(hasItem(DEFAULT_CONTENT)))
            .andExpect(jsonPath("$.[*].comment").value(hasItem(DEFAULT_COMMENT)))
            .andExpect(jsonPath("$.[*].image").value(hasItem(DEFAULT_IMAGE)))
            .andExpect(jsonPath("$.[*].like").value(hasItem(DEFAULT_LIKE)))
            .andExpect(jsonPath("$.[*].songAddress").value(hasItem(DEFAULT_SONG_ADDRESS)));
    }
}
