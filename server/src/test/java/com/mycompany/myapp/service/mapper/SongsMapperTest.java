package com.mycompany.myapp.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;


public class SongsMapperTest {

    private SongsMapper songsMapper;

    @BeforeEach
    public void setUp() {
        songsMapper = new SongsMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 2L;
        assertThat(songsMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(songsMapper.fromId(null)).isNull();
    }
}
