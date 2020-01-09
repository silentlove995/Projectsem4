package com.mycompany.myapp.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;


public class AdsPlaylistMapperTest {

    private AdsPlaylistMapper adsPlaylistMapper;

    @BeforeEach
    public void setUp() {
        adsPlaylistMapper = new AdsPlaylistMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 2L;
        assertThat(adsPlaylistMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(adsPlaylistMapper.fromId(null)).isNull();
    }
}
