package com.mycompany.myapp.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;


public class FavoriteMapperTest {

    private FavoriteMapper favoriteMapper;

    @BeforeEach
    public void setUp() {
        favoriteMapper = new FavoriteMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 2L;
        assertThat(favoriteMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(favoriteMapper.fromId(null)).isNull();
    }
}
