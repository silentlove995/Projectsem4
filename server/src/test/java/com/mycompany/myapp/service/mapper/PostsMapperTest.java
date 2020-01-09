package com.mycompany.myapp.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;


public class PostsMapperTest {

    private PostsMapper postsMapper;

    @BeforeEach
    public void setUp() {
        postsMapper = new PostsMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 2L;
        assertThat(postsMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(postsMapper.fromId(null)).isNull();
    }
}
