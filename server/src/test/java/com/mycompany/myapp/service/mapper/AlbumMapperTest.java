package com.mycompany.myapp.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;


public class AlbumMapperTest {

    private AlbumMapper albumMapper;

    @BeforeEach
    public void setUp() {
        albumMapper = new AlbumMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 2L;
        assertThat(albumMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(albumMapper.fromId(null)).isNull();
    }
}
